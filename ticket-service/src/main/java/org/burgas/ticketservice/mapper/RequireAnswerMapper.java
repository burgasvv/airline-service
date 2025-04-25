package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.RequireAnswerRequest;
import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.dto.RequireResponse;
import org.burgas.ticketservice.entity.RequireAnswer;
import org.burgas.ticketservice.repository.RequireRepository;
import org.springframework.stereotype.Component;

@Component
public final class RequireAnswerMapper {

    private final RequireRepository requireRepository;
    private final RequireMapper requireMapper;

    public RequireAnswerMapper(RequireRepository requireRepository, RequireMapper requireMapper) {
        this.requireRepository = requireRepository;
        this.requireMapper = requireMapper;
    }

    public RequireAnswer toRequireAnswer(final RequireAnswerRequest requireAnswerRequest) {
        return RequireAnswer.builder()
                .allowed(requireAnswerRequest.getAllowed())
                .explanation(requireAnswerRequest.getExplanation())
                .requireId(requireAnswerRequest.getRequireId())
                .build();
    }

    public RequireAnswerResponse toRequireAnswerResponse(final RequireAnswer requireAnswer) {
        return RequireAnswerResponse.builder()
                .id(requireAnswer.getId())
                .allowed(requireAnswer.getAllowed())
                .explanation(requireAnswer.getExplanation())
                .require(
                        this.requireRepository.findById(requireAnswer.getRequireId())
                                .map(this.requireMapper::toRequireResponse)
                                .orElseGet(RequireResponse::new)
                )
                .build();
    }
}
