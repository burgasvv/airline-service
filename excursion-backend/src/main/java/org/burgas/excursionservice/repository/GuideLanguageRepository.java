package org.burgas.excursionservice.repository;

import org.burgas.excursionservice.entity.GuideLanguage;
import org.burgas.excursionservice.entity.GuideLanguagePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideLanguageRepository extends JpaRepository<GuideLanguage, GuideLanguagePK> {

    void deleteGuideLanguagesByGuideId(Long guideId);
}
