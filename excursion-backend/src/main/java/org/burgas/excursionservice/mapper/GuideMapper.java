package org.burgas.excursionservice.mapper;

import org.burgas.excursionservice.dto.GuideRequest;
import org.burgas.excursionservice.dto.GuideResponse;
import org.burgas.excursionservice.entity.Guide;
import org.burgas.excursionservice.entity.GuideLanguage;
import org.burgas.excursionservice.handler.MapperDataHandler;
import org.burgas.excursionservice.repository.GuideLanguageRepository;
import org.burgas.excursionservice.repository.GuideRepository;
import org.burgas.excursionservice.repository.LanguageRepository;
import org.springframework.stereotype.Component;

@Component
public final class GuideMapper implements MapperDataHandler {

    private final GuideRepository guideRepository;
    private final LanguageRepository languageRepository;
    private final GuideLanguageRepository guideLanguageRepository;

    public GuideMapper(GuideRepository guideRepository, LanguageRepository languageRepository, GuideLanguageRepository guideLanguageRepository) {
        this.guideRepository = guideRepository;
        this.languageRepository = languageRepository;
        this.guideLanguageRepository = guideLanguageRepository;
    }

    public Guide toGuideSave(final GuideRequest guideRequest) {
        Long guideId = this.getData(guideRequest.getId(), 0L);
        return this.guideRepository.findById(guideId)
                .map(
                        guide -> {
                            this.guideLanguageRepository.deleteGuideLanguagesByGuideId(guide.getId());
                            guideRequest.getLanguages().forEach(
                                    languageId -> this.guideLanguageRepository.save(
                                            GuideLanguage.builder()
                                                    .guideId(guide.getId())
                                                    .languageId(languageId)
                                                    .build()
                                    )
                            );
                            return this.guideRepository.save(
                                    Guide.builder()
                                            .id(guide.getId())
                                            .name(this.getData(guideRequest.getName(), guide.getName()))
                                            .surname(this.getData(guideRequest.getSurname(), guide.getSurname()))
                                            .patronymic(this.getData(guideRequest.getPatronymic(), guide.getPatronymic()))
                                            .phone(this.getData(guideRequest.getPhone(), guide.getPhone()))
                                            .about(this.getData(guideRequest.getAbout(), guide.getAbout()))
                                            .build()
                            );
                        }
                )
                .orElseGet(
                        () -> {
                            Guide guide = this.guideRepository.save(
                                    Guide.builder()
                                            .name(guideRequest.getName())
                                            .surname(guideRequest.getSurname())
                                            .patronymic(guideRequest.getPatronymic())
                                            .phone(guideRequest.getPhone())
                                            .about(guideRequest.getAbout())
                                            .build()
                            );
                            guideRequest.getLanguages().forEach(
                                    languageId -> this.guideLanguageRepository.save(
                                            GuideLanguage.builder()
                                                    .guideId(guide.getId())
                                                    .languageId(languageId)
                                                    .build()
                                    )
                            );
                            return guide;
                        }
                );
    }

    public GuideResponse toGuideResponse(final Guide guide) {
        return GuideResponse.builder()
                .id(guide.getId())
                .name(guide.getName())
                .surname(guide.getSurname())
                .patronymic(guide.getPatronymic())
                .phone(guide.getPhone())
                .about(guide.getAbout())
                .imageId(guide.getImageId())
                .languages(this.languageRepository.findLanguagesByGuideId(guide.getId()))
                .build();
    }
}
