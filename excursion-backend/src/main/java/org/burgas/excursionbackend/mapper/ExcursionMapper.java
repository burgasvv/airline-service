package org.burgas.excursionbackend.mapper;

import org.burgas.excursionbackend.dto.ExcursionRequest;
import org.burgas.excursionbackend.dto.ExcursionResponse;
import org.burgas.excursionbackend.entity.Excursion;
import org.burgas.excursionbackend.entity.ExcursionSight;
import org.burgas.excursionbackend.entity.Guide;
import org.burgas.excursionbackend.handler.MapperDataHandler;
import org.burgas.excursionbackend.repository.ExcursionRepository;
import org.burgas.excursionbackend.repository.ExcursionSightRepository;
import org.burgas.excursionbackend.repository.GuideRepository;
import org.burgas.excursionbackend.repository.SightRepository;
import org.springframework.stereotype.Component;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class ExcursionMapper implements MapperDataHandler<ExcursionRequest, Excursion, ExcursionResponse> {

    private final ExcursionRepository excursionRepository;
    private final GuideRepository guideRepository;
    private final SightRepository sightRepository;
    private final SightMapper sightMapper;
    private final ExcursionSightRepository excursionSightRepository;

    public ExcursionMapper(
            ExcursionRepository excursionRepository, GuideRepository guideRepository,
            SightRepository sightRepository, SightMapper sightMapper, ExcursionSightRepository excursionSightRepository
    ) {
        this.excursionRepository = excursionRepository;
        this.guideRepository = guideRepository;
        this.sightRepository = sightRepository;
        this.sightMapper = sightMapper;
        this.excursionSightRepository = excursionSightRepository;
    }

    private void saveExcursionSights(ExcursionRequest excursionRequest, Excursion saved) {
        excursionRequest.getSights().forEach(
                sightId -> this.excursionSightRepository.save(
                        ExcursionSight.builder().excursionId(saved.getId()).sightId(sightId).build()
                )
        );
    }

    @Override
    public Excursion toEntity(ExcursionRequest excursionRequest) {
        Long excursionId = this.getData(excursionRequest.getId(), 0L);
        return this.excursionRepository.findById(excursionId)
                .map(
                        excursion -> {
                            Excursion saved = this.excursionRepository.save(
                                    Excursion.builder()
                                            .id(excursion.getId())
                                            .name(this.getData(excursionRequest.getName(), excursion.getName()))
                                            .description(this.getData(excursionRequest.getDescription(), excursion.getDescription()))
                                            .guideId(this.getData(excursionRequest.getGuideId(), excursion.getGuideId()))
                                            .cost(this.getData(excursionRequest.getCost(), excursion.getCost()))
                                            .starts(this.getData(excursionRequest.getStarts(), excursion.getStarts()))
                                            .ends(this.getData(excursionRequest.getEnds(), excursion.getEnds()))
                                            .inProgress(this.getData(excursionRequest.getInProgress(), excursion.getInProgress()))
                                            .passed(this.getData(excursionRequest.getPassed(), excursion.getPassed()))
                                            .build()
                            );

                            this.excursionSightRepository.deleteExcursionSightsByExcursionId(saved.getId());
                            saveExcursionSights(excursionRequest, saved);

                            return saved;
                        }
                )
                .orElseGet(
                        () -> {
                            Excursion saved = this.excursionRepository.save(
                                    Excursion.builder()
                                            .name(excursionRequest.getName())
                                            .description(excursionRequest.getDescription())
                                            .guideId(excursionRequest.getGuideId())
                                            .cost(excursionRequest.getCost())
                                            .starts(excursionRequest.getStarts())
                                            .ends(excursionRequest.getEnds())
                                            .inProgress(excursionRequest.getInProgress())
                                            .passed(excursionRequest.getPassed())
                                            .build()
                            );

                            saveExcursionSights(excursionRequest, saved);

                            return saved;
                        }
                );
    }

    @Override
    public ExcursionResponse toResponse(Excursion excursion) {
        Guide guide = this.guideRepository.findById(excursion.getGuideId()).orElseGet(Guide::new);
        return ExcursionResponse.builder()
                .id(excursion.getId())
                .name(excursion.getName())
                .description(excursion.getDescription())
                .guide(guide.getName() + " " + guide.getSurname() + " " + guide.getPatronymic())
                .cost(excursion.getCost())
                .starts(excursion.getStarts().format(ofPattern("dd.MM.yyyy, hh:mm")))
                .ends(excursion.getEnds().format(ofPattern("dd.MM.yyyy, hh:mm")))
                .inProgress(excursion.getInProgress())
                .passed(excursion.getPassed())
                .imageId(excursion.getImageId())
                .sights(
                        this.sightRepository.findSightsByExcursionId(excursion.getId())
                                .stream()
                                .map(this.sightMapper::toResponse)
                                .toList()
                )
                .build();
    }
}
