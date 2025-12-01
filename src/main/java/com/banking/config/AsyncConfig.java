package com.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * AsyncConfig - Configuration for asynchronous task execution
 *
 * This configuration sets up a thread pool for executing async tasks
 * like statement generation. It allows the API to return immediately
 * while processing continues in the background.
 *
 * Key configurations:
 * - Core pool size: Number of threads to keep alive
 * - Max pool size: Maximum number of threads
 * - Queue capacity: Number of tasks to queue before rejecting
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Minimum number of threads to maintain
        executor.setCorePoolSize(2);

        // Maximum number of threads
        executor.setMaxPoolSize(5);

        // Queue capacity for pending tasks
        executor.setQueueCapacity(100);

        // Thread name prefix for easier debugging
        executor.setThreadNamePrefix("async-statement-");

        executor.initialize();
        return executor;
    }
}