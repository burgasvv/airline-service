package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.PaymentResponse;
import org.burgas.hotelbackend.exception.PaymentNotFoundException;
import org.burgas.hotelbackend.mapper.PaymentMapper;
import org.burgas.hotelbackend.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.hotelbackend.log.PaymentLogs.PAYMENT_FOUND_ALL;
import static org.burgas.hotelbackend.log.PaymentLogs.PAYMENT_FOUND_ALL_ASYNC;
import static org.burgas.hotelbackend.message.PaymentMessages.PAYMENT_CLOSED_AND_PAYED;
import static org.burgas.hotelbackend.message.PaymentMessages.PAYMENT_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    public List<PaymentResponse> findByClientId(final Long clientId) {
        return this.paymentRepository.findPaymentsByClientId(clientId == null ? 0L : clientId)
                .parallelStream()
                .peek(payment -> log.info(PAYMENT_FOUND_ALL.getLog(), payment))
                .map(this.paymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<PaymentResponse>> findByClientIdAsync(final Long clientId) {
        return supplyAsync(() -> this.paymentRepository.findPaymentsByClientId(clientId == null ? 0L : clientId))
                .thenApplyAsync(
                        payments -> payments.parallelStream()
                                .peek(payment -> log.info(PAYMENT_FOUND_ALL_ASYNC.getLog(), payment))
                                .map(this.paymentMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String closePayment(final Long clientId) {
        return this.paymentRepository.findPaymentByClientIdAndClosed(clientId == null ? 0L : clientId, false)
                .map(
                        payment -> {
                            payment.setClosed(true);
                            this.paymentRepository.save(payment);
                            return PAYMENT_CLOSED_AND_PAYED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new PaymentNotFoundException(PAYMENT_NOT_FOUND.getMessage())
                );
    }
}
