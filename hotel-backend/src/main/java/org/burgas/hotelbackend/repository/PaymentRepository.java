package org.burgas.hotelbackend.repository;

import org.burgas.hotelbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findPaymentByClientIdAndClosedAndCancelled(Long clientId, Boolean closed, Boolean cancelled);

    List<Payment> findPaymentsByClientId(Long clientId);
}
