package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.RequireAnswer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RequireAnswerRepository extends R2dbcRepository<RequireAnswer, Long> {

    @Query(
            value = """
                    select ra.* from require_answer ra join require r on r.id = ra.require_id
                    where r.user_id = :userId
                    """
    )
    Flux<RequireAnswer> findRequireAnswersByUserId(Long userId);

    @Query(
            value = """
                    select ra.* from require_answer ra join require r on r.id = ra.require_id
                    where r.admin_id = :adminId
                    """
    )
    Flux<RequireAnswer> findRequireAnswersByAdminId(Long adminId);
}
