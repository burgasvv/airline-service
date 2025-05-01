package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.RequireAnswerRequest;
import org.burgas.flightbackend.dto.RequireAnswerResponse;
import org.burgas.flightbackend.dto.RequireResponse;
import org.burgas.flightbackend.entity.RequireAnswer;
import org.burgas.flightbackend.repository.RequireRepository;
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
