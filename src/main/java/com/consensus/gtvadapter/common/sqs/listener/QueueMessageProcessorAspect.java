package com.consensus.gtvadapter.common.sqs.listener;

import com.consensus.common.logging.LoggingTraceProperties;
import com.consensus.common.sqs.*;
import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Map.Entry;

import static com.consensus.common.sqs.CCSIQueueMessageStatus.NOOP;
import static com.consensus.common.sqs.CCSIQueueMessageStatus.UNHANDLED_EXCEPTION;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Aspect
@Component
@ConditionalOnBean(QueueMessageProcessor.class)
public class QueueMessageProcessorAspect {

    @Autowired(required = false)
    private MeterRegistry meterRegistry;
    @Autowired
    private LoggingTraceProperties loggingTraceProperties;

    @Pointcut("execution(public * com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor.process(..))")
    public void messageProcessor() {
        // pointcut
    }

    @Around("messageProcessor()")
    public Object messageProcessor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Class<?> declaringType = methodSignature.getDeclaringType();

        Logger logger = LoggerFactory.getLogger(declaringType);
        StopWatch stopWatch = new StopWatch(declaringType.getSimpleName() + "-" + methodSignature.getName());
        stopWatch.setKeepTaskList(false);
        stopWatch.start(methodSignature.getName());

        CCSIQueueMessageContext sqsMessage = (CCSIQueueMessageContext) proceedingJoinPoint.getArgs()[0];
        QueueMessageProcessor processor = (QueueMessageProcessor) proceedingJoinPoint.getThis();

        createContext(sqsMessage, processor);

        CCSIQueueMessageResult result = CCSIQueueMessageResult.builder()
                .status(NOOP)
                .logMessage("Event Was Not Processed")
                .build();

        try {
            result = (CCSIQueueMessageResult) proceedingJoinPoint.proceed();
        } catch (Exception e) {
            result = CCSIQueueMessageResult.builder()
                    .status(UNHANDLED_EXCEPTION)
                    .exception(e)
                    .build();
        } finally {
            stopWatch.stop();
            recordMetrics(processor.getQueueProperties(), result, sqsMessage);
            CCSIQueueTraceContextHolder.updateResult(result, sqsMessage,
                    processor.getQueueProperties().getTracingConfig(), stopWatch.getTotalTimeMillis());

            if (nonNull(result.getException())) {
                logger.error("Error While Processing Event: {}", result.getException().getMessage(), result.getException());
            }

            String logMessage = result.getLogMessage();
            if (isNotEmpty(logMessage)) {
                if (result.getStatus().isErrorStatus()) {
                    logger.error(logMessage);
                } else {
                    logger.info(logMessage);
                }
            }

            final String additionalFields = buildAdditionalFields(CCSIQueueTraceContextHolder.get());
            logger.info("Processed SQS Message, Elapsed Time: {}, Status: {}{}",
                    stopWatch.getTotalTimeMillis(), result.getStatus(), additionalFields);

            CCSIQueueTraceContextHolder.clean();
        }
        return result;
    }


    private String buildAdditionalFields(CCSIQueueTraceContext context) {
        StringBuilder builder = new StringBuilder();
        if (nonNull(context) && isNotEmpty(context.getAdditionalTraceData())) {
            for (Entry<String, String> entry : context.getAdditionalTraceData().entrySet()) {
                builder
                        .append(", ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue());
            }
        }
        return builder.toString();
    }


    private void recordMetrics(CCSIQueueListenerProperties properties,
            CCSIQueueMessageResult result,
            CCSIQueueMessageContext sqsMessage) {
        if (nonNull(meterRegistry)) {
            meterRegistry.counter("ccsi.sqs.messages",
                    "queue_short_name",
                    properties.getQueueShortName(),
                    "queue_url",
                    properties.getQueueUrl(),
                    "retry_count",
                    sqsMessage.getApproximateReceiveCount(),
                    "status",
                    result.getStatus().name()).increment();
        }
    }

    private void createContext(CCSIQueueMessageContext sqsMessage, QueueMessageProcessor processor) {
        CCSIQueueTraceContextHolder.createContext(sqsMessage,
                processor.getQueueProperties().getQueueUrl(),
                processor.getQueueProperties().getQueueShortName(),
                processor.getQueueProperties().getTracingConfig(),
                loggingTraceProperties.getAppNamespace(),
                loggingTraceProperties.getAppProject());
    }
}
