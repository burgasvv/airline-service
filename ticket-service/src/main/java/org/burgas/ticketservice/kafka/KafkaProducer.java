package org.burgas.ticketservice.kafka;

import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.dto.RequireAnswerTokenResponse;
import org.burgas.ticketservice.dto.RequireResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

    public Mono<Void> sendStringRequireMessage(final Mono<RequireResponse> requireResponseMono) {
        return requireResponseMono.flatMap(
                requireResponse -> Mono.fromCallable(() ->
                        this.stringRequireResponseKafkaTemplate.send("user-admin-require-topic", requireResponse))
                        .then()
        );
    }

    public Mono<Void> sendStringRequireAnswerMessage(final Mono<RequireAnswerResponse> requireAnswerResponseMono) {
        return requireAnswerResponseMono.flatMap(
                requireAnswerResponse -> Mono.fromCallable(() ->
                        this.stringRequireAnswerResponseKafkaTemplate.send("user-admin-require-topic", requireAnswerResponse))
                        .then()
        );
    }

    public Mono<Void> sendStringRequireAnswerTokenMessage(final Mono<RequireAnswerTokenResponse> requireAnswerTokenResponseMono) {
        return requireAnswerTokenResponseMono.flatMap(
                requireAnswerTokenResponse -> Mono.fromCallable(() ->
                        this.stringRequireAnswerTokenResponseKafkaTemplate.send("user-admin-require-topic", requireAnswerTokenResponse))
                        .then()
        );
    }
}
