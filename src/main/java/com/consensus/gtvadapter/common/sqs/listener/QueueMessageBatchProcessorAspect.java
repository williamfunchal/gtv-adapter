package com.consensus.gtvadapter.common.sqs.listener;

import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;

import static com.consensus.common.sqs.CCSIQueueMessageStatus.NOOP;
import static com.consensus.common.sqs.CCSIQueueMessageStatus.UNHANDLED_EXCEPTION;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Aspect
@Component
@ConditionalOnBean(QueueMessageBatchProcessor.class)
public class QueueMessageBatchProcessorAspect {

    @Pointcut("execution(public * com.consensus.gtvadapter.common.sqs.listener.QueueMessageBatchProcessor.process(..))")
    public void messageBatchProcessor() {
        // pointcut
    }

    @Around("messageBatchProcessor()")
    public Object messageProcessor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Class<?> declaringType = methodSignature.getDeclaringType();

        Logger logger = LoggerFactory.getLogger(declaringType);
        StopWatch stopWatch = new StopWatch(declaringType.getSimpleName() + "-" + methodSignature.getName());
        stopWatch.setKeepTaskList(false);
        stopWatch.start(methodSignature.getName());

        String sqsMessageGroupName = (String) proceedingJoinPoint.getArgs()[0];
        List<CCSIQueueMessageContext> sqsMessageBatch = (List<CCSIQueueMessageContext>) proceedingJoinPoint.getArgs()[1];
        QueueMessageProcessor processor = (QueueMessageProcessor) proceedingJoinPoint.getThis();

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

            logger.info("Processed SQS Message Batch, Elapsed Time: {}, Status: {}",
                    stopWatch.getTotalTimeMillis(), result.getStatus());
        }
        return result;
    }
}
