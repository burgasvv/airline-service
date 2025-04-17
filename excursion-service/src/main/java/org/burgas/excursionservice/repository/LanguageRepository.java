package org.burgas.excursionservice.repository;

import org.burgas.excursionservice.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select l.* from language l join guide_language gl on l.id = gl.language_id
                                        where gl.guide_id = :guideId
                    """
    )
    List<Language> findLanguagesByGuideId(Long guideId);
}
