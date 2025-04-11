package org.burgas.ticketservice.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.dto.RequireAnswerTokenResponse;
import org.burgas.ticketservice.dto.RequireResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public final class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(groupId = "user-admin-require-group-id", topics = "user-admin-require-topic")
    public void listenToStringRequire(ConsumerRecord<String, RequireResponse> consumerRecord) {
        log.info("Get consumer record REQUIRE RESPONSE: {}", consumerRecord.value());
    }

    @KafkaListener(groupId = "user-admin-require-group-id", topics = "user-admin-require-topic")
    public void listenToStringRequireAnswer(ConsumerRecord<String, RequireAnswerResponse> consumerRecord) {
        log.info("Get consumer record REQUIRE ANSWER RESPONSE: {}", consumerRecord.value());
    }

    @KafkaListener(groupId = "user-admin-require-group-id", topics = "user-admin-require-topic")
    public void listenToStringRequireAnswerToken(ConsumerRecord<String, RequireAnswerTokenResponse> consumerRecord) {
        log.info("Get consumer record REQUIRE ANSWER TOKEN RESPONSE: {}", consumerRecord.value());
    }
}
