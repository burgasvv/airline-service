package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.RoomImage;
import org.burgas.hotelbackend.entity.RoomImagePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, RoomImagePK> {
}
