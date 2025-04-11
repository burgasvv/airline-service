package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.RequireAnswerRequest;
import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.entity.RequireAnswer;
import org.burgas.ticketservice.repository.RequireRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class RequireAnswerMapper {

    private final RequireRepository requireRepository;
    private final RequireMapper requireMapper;

    public RequireAnswerMapper(RequireRepository requireRepository, RequireMapper requireMapper) {
        this.requireRepository = requireRepository;
        this.requireMapper = requireMapper;
    }

    public Mono<RequireAnswer> toRequireAnswer(final Mono<RequireAnswerRequest> requireAnswerRequestMono) {
        return requireAnswerRequestMono.flatMap(
                requireAnswerRequest -> Mono.fromCallable(() ->
                        RequireAnswer.builder()
                                .allowed(requireAnswerRequest.getAllowed())
                                .explanation(requireAnswerRequest.getExplanation())
                                .requireId(requireAnswerRequest.getRequireId())
                                .isNew(true)
                                .build())
        );
    }

    public Mono<RequireAnswerResponse> toRequireAnswerResponse(final Mono<RequireAnswer> requireAnswerMono) {
        return requireAnswerMono.flatMap(
                requireAnswer -> this.requireRepository.findById(requireAnswer.getRequireId())
                        .flatMap(require -> this.requireMapper.toRequireResponse(Mono.fromCallable(() -> require)))
                        .flatMap(
                                requireResponse -> Mono.fromCallable(() ->
                                        RequireAnswerResponse.builder()
                                                .id(requireAnswer.getId())
                                                .allowed(requireAnswer.getAllowed())
                                                .explanation(requireAnswer.getExplanation())
                                                .require(requireResponse)
                                                .build())
                        )
        );
    }
}
