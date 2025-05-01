package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.OrderedTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedTicketRepository extends JpaRepository<OrderedTicket, Long> {

    List<OrderedTicket> findOrderedTicketsByIdentityId(Long identityId);
}
