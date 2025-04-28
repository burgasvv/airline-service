package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.OrderedTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedTicketRepository extends JpaRepository<OrderedTicket, Long> {

    List<OrderedTicket> findOrderedTicketsByIdentityId(Long identityId);
}
