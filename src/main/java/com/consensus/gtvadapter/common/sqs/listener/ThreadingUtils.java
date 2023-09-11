package com.consensus.gtvadapter.common.sqs.listener;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import lombok.experimental.UtilityClass;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;

@UtilityClass
public class ThreadingUtils {

    public static ThreadPoolTaskExecutor queueTaskListener(CCSIQueueListenerProperties properties) {
        var executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(properties.getQueueShortName() + "-listener-");
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.initialize();
        return executor;
    }

    public static ThreadPoolTaskExecutor queueTaskProcessor(CCSIQueueListenerProperties properties) {
        var executor = new BlockingThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(properties.getQueueShortName() + "-processor-");
        executor.setCorePoolSize(properties.getConcurrency());
        executor.setMaxPoolSize(properties.getConcurrency());
        executor.initialize();
        return executor;
    }

    private static class BlockingThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

        @Override
        protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
            return new SynchronousBlockingQueue<>(true);
        }
    }
}
