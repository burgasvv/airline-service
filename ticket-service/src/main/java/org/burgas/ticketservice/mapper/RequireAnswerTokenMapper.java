package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.RequireAnswerTokenResponse;
import org.burgas.ticketservice.entity.RequireAnswerToken;
import org.burgas.ticketservice.repository.RequireAnswerRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class RequireAnswerTokenMapper {

    private final RequireAnswerRepository requireAnswerRepository;
    private final RequireAnswerMapper requireAnswerMapper;

    public RequireAnswerTokenMapper(RequireAnswerRepository requireAnswerRepository, RequireAnswerMapper requireAnswerMapper) {
        this.requireAnswerRepository = requireAnswerRepository;
        this.requireAnswerMapper = requireAnswerMapper;
    }

    public Mono<RequireAnswerTokenResponse> toRequireAnswerTokenResponse(final Mono<RequireAnswerToken> requireAnswerTokenMono) {
        return requireAnswerTokenMono.flatMap(
                requireAnswerToken -> this.requireAnswerRepository.findById(requireAnswerToken.getRequireAnswerId())
                        .flatMap(requireAnswer -> this.requireAnswerMapper.toRequireAnswerResponse(Mono.fromCallable(() -> requireAnswer)))
                        .flatMap(
                                requireAnswerResponse -> Mono.fromCallable(() ->
                                        RequireAnswerTokenResponse.builder()
                                                .id(requireAnswerToken.getId())
                                                .value(requireAnswerToken.getValue())
                                                .requireAnswer(requireAnswerResponse)
                                                .build()
                                )
                        )
        );
    }
}
