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

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class PaymentMapper implements MapperDataHandler<PaymentRequest, Payment, PaymentResponse> {

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

    @Override
    public Payment toEntity(PaymentRequest paymentRequest) {
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

    @Override
    public PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .identity(
                        this.identityRepository.findById(payment.getIdentityId())
                                .map(this.identityMapper::toResponse)
                                .orElse(null)
                )
                .excursion(
                        this.excursionRepository.findById(payment.getExcursionId())
                                .map(this.excursionMapper::toResponse)
                                .orElse(null)
                )
                .payedAt(payment.getPayedAt().format(ofPattern("dd.MM.yyyy, hh:mm:ss")))
                .build();
    }
}
