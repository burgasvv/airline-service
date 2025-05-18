package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Filial;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {

    @Override
    @NotNull Page<Filial> findAll(@NotNull Pageable pageable);

    @Query(
            nativeQuery = true,
            value = """
                    select f.* from hotel_backend.public.filial f join public.room r on f.id = r.filial_id
                                        where r.id = :roomId
                    """
    )
    Optional<Filial> findFilialByRoomId(Long roomId);
}
