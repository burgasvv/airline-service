package org.burgas.ticketservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic requireTopic() {
        return TopicBuilder.name("user-admin-require-topic")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
