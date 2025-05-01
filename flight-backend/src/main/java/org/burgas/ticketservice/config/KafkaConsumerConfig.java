package org.burgas.ticketservice.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.dto.RequireAnswerTokenResponse;
import org.burgas.ticketservice.dto.RequireResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public Map<String, Object> consumerConfig() {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                GROUP_ID_CONFIG, "user-admin-require-group-id",
                KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                TYPE_MAPPINGS, new String[] {
                        "org.burgas.ticketservice.dto.RequireResponse:org.burgas.ticketservice.dto.RequireResponse",
                        "org.burgas.ticketservice.dto.RequireAnswerResponse:org.burgas.ticketservice.dto.RequireAnswerResponse",
                        "org.burgas.ticketservice.dto.RequireAnswerTokenResponse:org.burgas.ticketservice.dto.RequireAnswerTokenResponse"
                }
        );
    }

    @Bean
    public ConsumerFactory<String, RequireResponse> stringRequireResponseConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RequireResponse> stringRequireResponseConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RequireResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringRequireResponseConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, RequireAnswerResponse> stringRequireAnswerResponseConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RequireAnswerResponse> stringRequireAnswerResponseConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RequireAnswerResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringRequireAnswerResponseConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, RequireAnswerTokenResponse> stringRequireAnswerTokenResponseConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RequireAnswerTokenResponse> stringRequireAnswerTokenResponseConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RequireAnswerTokenResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringRequireAnswerTokenResponseConsumerFactory());
        return factory;
    }
}
