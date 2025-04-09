package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Filial;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FilialRepository extends R2dbcRepository<Filial, Long> {

    @Query(
            value = """
                    select f.* from filial f
                        join address a on a.id = f.address_id
                        join city c on c.id = a.city_id
                        join country c2 on c2.id = c.country_id
                    where c2.id = :countryId
                    """
    )
    Flux<Filial> findFilialsByCountryId(Long countryId);

    @Query(
            value = """
                    select f.* from filial f
                        join address a on a.id = f.address_id
                        join city c on c.id = a.city_id
                    where c.id = :cityId
                    """
    )
    Flux<Filial> findFilialsByCityId(Long cityId);
}
