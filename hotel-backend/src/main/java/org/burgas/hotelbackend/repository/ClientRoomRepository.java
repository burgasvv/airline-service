package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.ClientRoom;
import org.burgas.hotelbackend.entity.ClientRoomPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRoomRepository extends JpaRepository<ClientRoom, ClientRoomPK> {

    List<ClientRoom> findClientRoomsByClientId(Long clientId);
}
