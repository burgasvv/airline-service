package org.burgas.flightbackend.repository;

import org.burgas.flightbackend.entity.Require;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequireRepository extends JpaRepository<Require, Long> {

    List<Require> findRequiresByClosed(Boolean closed);

    List<Require> findRequiresByUserId(Long userId);

    List<Require> findRequiresByAdminId(Long adminId);
}
