package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.Filial;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select f.* from filial f
                        join address a on a.id = f.address_id
                        join city c on c.id = a.city_id
                        join country c2 on c2.id = c.country_id
                    where c2.id = :countryId
                    """
    )
    List<Filial> findFilialsByCountryId(Long countryId);

    @Query(
            nativeQuery = true,
            value = """
                    select f.* from filial f
                        join address a on a.id = f.address_id
                        join city c on c.id = a.city_id
                    where c.id = :cityId
                    """
    )
    List<Filial> findFilialsByCityId(Long cityId);

    @NotNull Page<Filial> findAll(@NotNull Pageable pageable);
}
