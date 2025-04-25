package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.dto.RequireAnswerTokenResponse;
import org.burgas.ticketservice.entity.RequireAnswerToken;
import org.burgas.ticketservice.repository.RequireAnswerRepository;
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
                                .map(this.requireAnswerMapper::toRequireAnswerResponse)
                                .orElseGet(RequireAnswerResponse::new)
                )
                .build();
    }
}
