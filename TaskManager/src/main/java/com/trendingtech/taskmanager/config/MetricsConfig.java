package com.trendingtech.taskmanager.config;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsync;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClientBuilder;
import io.micrometer.cloudwatch.CloudWatchConfig; // <--- Interface correta
import io.micrometer.cloudwatch.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;

@Configuration
public class MetricsConfig {

    @Bean
    @SuppressWarnings("deprecation")
    public CloudWatchConfig cloudWatchConfig() {
        return new CloudWatchConfig() {
            
            private final Map<String, String> configuration = Map.of(
                    "cloudwatch.namespace", "TaskManagerApp",
                    "cloudwatch.step", "1m",
                    "cloudwatch.batchSize", "20"
            );

            @Override
            public String get(String key) {
                return configuration.get(key);
            }
        };
    }

    @Bean
    public AmazonCloudWatchAsync amazonCloudWatchAsync() {
        return AmazonCloudWatchAsyncClientBuilder.standard()
                .withRegion("us-east-2") 
                .build();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public MeterRegistry getMeterRegistry(CloudWatchConfig config, AmazonCloudWatchAsync client) {
        return new CloudWatchMeterRegistry(
                config,
                Clock.SYSTEM,
                client
        );
    }
}