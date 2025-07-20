package com.url.shortener.config.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {
    @Value("${async.executor.core-pool-size}")
    private Integer corePoolSize;
    @Value("${async.executor.max-pool-size}")
    private Integer maxPoolSize;
    @Value("${async.executor.queue-capacity}")
    private Integer queueCapacity;
    @Value("${async.executor.thread-name-prefix}")
    private String threadNamePrefix;
    @Value("${async.executor.keep-alive-seconds}")
    private Integer keepAliveSeconds;

    @Bean(name = "customAsyncExecutor")
    public Executor customAsyncExecutor() {
        log.info("Start creating custom async executor.");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();

        log.info("Successfully creating custom async executor.");
        return executor;
    }
}
