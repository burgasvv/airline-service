package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Require;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequireRepository extends JpaRepository<Require, Long> {

    List<Require> findRequiresByClosed(Boolean closed);
}
