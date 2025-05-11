package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.RequireAnswerResponse;
import org.burgas.flightbackend.dto.RequireAnswerTokenResponse;
import org.burgas.flightbackend.entity.RequireAnswerToken;
import org.burgas.flightbackend.repository.RequireAnswerRepository;
import org.springframework.stereotype.Component;

@Component
public final class RequireAnswerTokenMapper {

    private final RequireAnswerRepository requireAnswerRepository;
    private final RequireAnswerMapper requireAnswerMapper;

    public RequireAnswerTokenMapper(RequireAnswerRepository requireAnswerRepository, RequireAnswerMapper requireAnswerMapper) {
        this.requireAnswerRepository = requireAnswerRepository;
        this.requireAnswerMapper = requireAnswerMapper;
    }

    public RequireAnswerTokenResponse toRequireAnswerTokenResponse(final RequireAnswerToken requireAnswerToken) {
        return RequireAnswerTokenResponse.builder()
                .id(requireAnswerToken.getId())
                .value(requireAnswerToken.getValue())
                .requireAnswer(
                        this.requireAnswerRepository.findById(requireAnswerToken.getRequireAnswerId())
                                .map(this.requireAnswerMapper::toResponse)
                                .orElseGet(RequireAnswerResponse::new)
                )
                .build();
    }
}
