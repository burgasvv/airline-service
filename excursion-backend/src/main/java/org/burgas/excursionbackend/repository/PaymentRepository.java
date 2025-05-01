package org.burgas.excursionbackend.repository;

import org.burgas.excursionbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findPaymentsByIdentityId(Long identityId);
}
