package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.Airport;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select a.* from airport a
                        join address a2 on a2.id = a.address_id
                        join city c on a2.city_id = c.id
                        join country c2 on c2.id = c.country_id
                    where c2.id = :countryId
                    """
    )
    List<Airport> findAirportsByCountryId(Long countryId);

    @Query(
            nativeQuery = true,
            value = """
                    select air.* from airport air
                        join address a on a.id = air.address_id
                        join city c on c.id = a.city_id
                    where c.id = :cityId
                    """
    )
    List<Airport> findAirportsByCityId(Long cityId);

    @NotNull Page<Airport> findAll(@NotNull Pageable pageable);
}
