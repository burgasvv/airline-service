package org.burgas.excursionbackend.service;

import org.burgas.excursionbackend.dto.PaymentRequest;
import org.burgas.excursionbackend.dto.PaymentResponse;
import org.burgas.excursionbackend.exception.IdentityNotFoundException;
import org.burgas.excursionbackend.mapper.PaymentMapper;
import org.burgas.excursionbackend.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.excursionbackend.log.PaymentLogs.PAYMENT_FOUND_BY_ID;
import static org.burgas.excursionbackend.log.PaymentLogs.PAYMENT_FOUND_BY_IDENTITY_ID;
import static org.burgas.excursionbackend.message.IdentityMessages.IDENTITY_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final IdentityService identityService;
    private final ExcursionService excursionService;

    public PaymentService(
            PaymentRepository paymentRepository, PaymentMapper paymentMapper,
            IdentityService identityService, ExcursionService excursionService
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.identityService = identityService;
        this.excursionService = excursionService;
    }

    public List<PaymentResponse> findAllByIdentityId(final String identityId) {
        return this.paymentRepository.findPaymentsByIdentityId(Long.valueOf(identityId == null ? "0" : identityId))
                .stream()
                .peek(payment -> log.info(PAYMENT_FOUND_BY_IDENTITY_ID.getLogMessage(), payment))
                .map(this.paymentMapper::toResponse)
                .toList();
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<PaymentResponse>> findAllByIdentityIdAsync(final String identityId) {
        return supplyAsync(() -> this.findAllByIdentityId(identityId));
    }

    public PaymentResponse findById(final String paymentId) {
        return this.paymentRepository.findById(Long.valueOf(paymentId == null ? "0" : paymentId))
                .stream()
                .peek(payment -> log.info(PAYMENT_FOUND_BY_ID.getLogMessage(), payment))
                .map(this.paymentMapper::toResponse)
                .findFirst()
                .orElseGet(PaymentResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<PaymentResponse> findByIdAsync(final String paymentId) {
        return supplyAsync(() -> this.findById(paymentId));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public PaymentResponse makeIdentityPayment(final PaymentRequest paymentRequest) {
        return ofNullable(this.identityService.findByIdOrElseThrow(String.valueOf(paymentRequest.getIdentityId())))
                .map(
                        identityResponse -> {
                            this.excursionService.addExcursionByIdentityId(
                                    String.valueOf(paymentRequest.getExcursionId()), String.valueOf(identityResponse.getId())
                            );
                            return of(this.paymentMapper.toEntity(paymentRequest))
                                    .map(this.paymentRepository::save)
                                    .map(this.paymentMapper::toResponse)
                                    .orElseGet(PaymentResponse::new);
                        }
                )
                .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<PaymentResponse> makeIdentityPaymentAsync(final PaymentRequest paymentRequest) {
        return supplyAsync(
                () -> of(this.identityService.findByIdOrElseThrow(String.valueOf(paymentRequest.getIdentityId())))
                        .orElseThrow(() -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage()))
        )
                .thenApplyAsync(
                        identityResponse -> {
                            this.excursionService.addExcursionByIdentityIdAsync(
                                    String.valueOf(paymentRequest.getExcursionId()), String.valueOf(identityResponse.getId())
                            );
                            return of(this.paymentMapper.toEntity(paymentRequest))
                                    .map(this.paymentRepository::save)
                                    .map(this.paymentMapper::toResponse)
                                    .orElseGet(PaymentResponse::new);
                        }
                );
    }
}
