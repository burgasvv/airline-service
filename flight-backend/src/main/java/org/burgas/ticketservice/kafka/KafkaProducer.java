package org.burgas.ticketservice.kafka;

import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.dto.RequireAnswerTokenResponse;
import org.burgas.ticketservice.dto.RequireResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class KafkaProducer {

    private final KafkaTemplate<String, RequireResponse> stringRequireResponseKafkaTemplate;
    private final KafkaTemplate<String, RequireAnswerResponse> stringRequireAnswerResponseKafkaTemplate;
    private final KafkaTemplate<String, RequireAnswerTokenResponse> stringRequireAnswerTokenResponseKafkaTemplate;

    public KafkaProducer(
            KafkaTemplate<String, RequireResponse> stringRequireResponseKafkaTemplate,
            KafkaTemplate<String, RequireAnswerResponse> stringRequireAnswerResponseKafkaTemplate,
            KafkaTemplate<String, RequireAnswerTokenResponse> stringRequireAnswerTokenResponseKafkaTemplate
    ) {
        this.stringRequireResponseKafkaTemplate = stringRequireResponseKafkaTemplate;
        this.stringRequireAnswerResponseKafkaTemplate = stringRequireAnswerResponseKafkaTemplate;
        this.stringRequireAnswerTokenResponseKafkaTemplate = stringRequireAnswerTokenResponseKafkaTemplate;
    }

    public void sendStringRequireMessage(final RequireResponse requireResponse) {
        this.stringRequireResponseKafkaTemplate.send("user-admin-require-topic", requireResponse);
    }

    public void sendStringRequireAnswerMessage(final RequireAnswerResponse requireAnswerResponse) {
        this.stringRequireAnswerResponseKafkaTemplate.send("user-admin-require-topic", requireAnswerResponse);
    }

    public void sendStringRequireAnswerTokenMessage(final RequireAnswerTokenResponse requireAnswerTokenResponse) {
        this.stringRequireAnswerTokenResponseKafkaTemplate.send("user-admin-require-topic", requireAnswerTokenResponse);
    }
}
