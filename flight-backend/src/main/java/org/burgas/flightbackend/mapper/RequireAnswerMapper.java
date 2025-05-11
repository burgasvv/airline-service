package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.RequireAnswerRequest;
import org.burgas.flightbackend.dto.RequireAnswerResponse;
import org.burgas.flightbackend.dto.RequireResponse;
import org.burgas.flightbackend.entity.RequireAnswer;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.RequireRepository;
import org.springframework.stereotype.Component;

@Component
public final class RequireAnswerMapper implements MapperDataHandler<RequireAnswerRequest, RequireAnswer, RequireAnswerResponse> {

    private final RequireRepository requireRepository;
    private final RequireMapper requireMapper;

    public RequireAnswerMapper(RequireRepository requireRepository, RequireMapper requireMapper) {
        this.requireRepository = requireRepository;
        this.requireMapper = requireMapper;
    }

    @Override
    public RequireAnswer toEntity(RequireAnswerRequest requireAnswerRequest) {
        return RequireAnswer.builder()
                .allowed(requireAnswerRequest.getAllowed())
                .explanation(requireAnswerRequest.getExplanation())
                .requireId(requireAnswerRequest.getRequireId())
                .build();
    }

    @Override
    public RequireAnswerResponse toResponse(RequireAnswer requireAnswer) {
        return RequireAnswerResponse.builder()
                .id(requireAnswer.getId())
                .allowed(requireAnswer.getAllowed())
                .explanation(requireAnswer.getExplanation())
                .require(
                        this.requireRepository.findById(requireAnswer.getRequireId())
                                .map(this.requireMapper::toResponse)
                                .orElseGet(RequireResponse::new)
                )
                .build();
    }
}
