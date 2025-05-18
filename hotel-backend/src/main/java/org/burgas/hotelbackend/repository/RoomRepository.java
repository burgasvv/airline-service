package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findRoomsByFilialId(Long filialId, Pageable pageable);

    List<Room> findRoomsByFilialId(Long filialId);

    Boolean existsRoomByNumberAndFilialId(Long number, Long filialId);
}
