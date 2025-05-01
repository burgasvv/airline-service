package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.GuideLanguage;
import org.burgas.excursionbackend.entity.GuideLanguagePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideLanguageRepository extends JpaRepository<GuideLanguage, GuideLanguagePK> {

    void deleteGuideLanguagesByGuideId(Long guideId);
}
