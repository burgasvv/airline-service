package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Airport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AirportRepository extends R2dbcRepository<Airport, Long> {

    @Query(
            value = """
                    select a.* from airport a
                        join address a2 on a2.id = a.address_id
                        join city c on a2.city_id = c.id
                        join country c2 on c2.id = c.country_id
                    where c2.id = :countryId
                    """
    )
    Flux<Airport> findAirportsByCountryId(Long countryId);

    @Query(
            value = """
                    select air.* from airport air
                        join address a on a.id = air.address_id
                        join city c on c.id = a.city_id
                    where c.id = :cityId
                    """
    )
    Flux<Airport> findAirportsByCityId(Long cityId);
}
