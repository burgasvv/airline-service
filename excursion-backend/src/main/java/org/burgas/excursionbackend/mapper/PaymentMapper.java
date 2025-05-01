package org.burgas.excursionbackend.mapper;

import org.burgas.excursionbackend.dto.PaymentRequest;
import org.burgas.excursionbackend.dto.PaymentResponse;
import org.burgas.excursionbackend.entity.Payment;
import org.burgas.excursionbackend.handler.MapperDataHandler;
import org.burgas.excursionbackend.repository.ExcursionRepository;
import org.burgas.excursionbackend.repository.IdentityRepository;
import org.burgas.excursionbackend.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public final class PaymentMapper implements MapperDataHandler {

    private final PaymentRepository paymentRepository;
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final ExcursionRepository excursionRepository;
    private final ExcursionMapper excursionMapper;

    public PaymentMapper(
            PaymentRepository paymentRepository, IdentityRepository identityRepository, IdentityMapper identityMapper,
            ExcursionRepository excursionRepository, ExcursionMapper excursionMapper
    ) {
        this.paymentRepository = paymentRepository;
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
        this.excursionRepository = excursionRepository;
        this.excursionMapper = excursionMapper;
    }

    public Payment toPayment(final PaymentRequest paymentRequest) {
        Long paymentId = this.getData(paymentRequest.getId(), 0L);
        return this.paymentRepository.findById(paymentId)
                .map(
                        payment -> Payment.builder()
                                .id(payment.getId())
                                .identityId(this.getData(paymentRequest.getIdentityId(), payment.getIdentityId()))
                                .excursionId(this.getData(paymentRequest.getExcursionId(), payment.getExcursionId()))
                                .payedAt(payment.getPayedAt())
                                .build()
                )
                .orElseGet(
                        () -> Payment.builder()
                                .identityId(paymentRequest.getIdentityId())
                                .excursionId(paymentRequest.getExcursionId())
                                .payedAt(LocalDateTime.now())
                                .build()
                );
    }

    public PaymentResponse toPaymentResponse(final Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .identity(
                        this.identityRepository.findById(payment.getIdentityId())
                                .map(this.identityMapper::toIdentityResponse)
                                .orElse(null)
                )
                .excursion(
                        this.excursionRepository.findById(payment.getExcursionId())
                                .map(this.excursionMapper::toExcursionResponse)
                                .orElse(null)
                )
                .payedAt(payment.getPayedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, hh:mm:ss")))
                .build();
    }
}
