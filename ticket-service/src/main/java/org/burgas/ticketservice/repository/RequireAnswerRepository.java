package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.RequireAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequireAnswerRepository extends JpaRepository<RequireAnswer, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select ra.* from require_answer ra join require r on r.id = ra.require_id
                    where r.user_id = :userId
                    """
    )
    List<RequireAnswer> findRequireAnswersByUserId(Long userId);

    @Query(
            nativeQuery = true,
            value = """
                    select ra.* from require_answer ra join require r on r.id = ra.require_id
                    where r.admin_id = :adminId
                    """
    )
    List<RequireAnswer> findRequireAnswersByAdminId(Long adminId);
}
