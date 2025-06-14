package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.PaymentResponse;
import org.burgas.hotelbackend.entity.ClientRoomPK;
import org.burgas.hotelbackend.entity.Payment;
import org.burgas.hotelbackend.entity.Room;
import org.burgas.hotelbackend.exception.PaymentNotFoundException;
import org.burgas.hotelbackend.exception.RoomNotFoundException;
import org.burgas.hotelbackend.mapper.PaymentMapper;
import org.burgas.hotelbackend.repository.ClientRoomRepository;
import org.burgas.hotelbackend.repository.PaymentRepository;
import org.burgas.hotelbackend.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.hotelbackend.log.PaymentLogs.PAYMENT_FOUND_ALL;
import static org.burgas.hotelbackend.log.PaymentLogs.PAYMENT_FOUND_ALL_ASYNC;
import static org.burgas.hotelbackend.message.PaymentMessages.*;
import static org.burgas.hotelbackend.message.RoomMessages.ROOM_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.*;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ClientRoomRepository clientRoomRepository;
    private final RoomRepository roomRepository;

    public PaymentService(
            PaymentRepository paymentRepository, PaymentMapper paymentMapper,
            ClientRoomRepository clientRoomRepository, RoomRepository roomRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.clientRoomRepository = clientRoomRepository;
        this.roomRepository = roomRepository;
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
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String closePayment(final Long clientId) {
        return this.paymentRepository.findPaymentByClientIdAndClosedAndCancelled(
                clientId == null ? 0L : clientId, false, false)
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

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String cancelRentAndPayment(final Long paymentId) {
        Payment payment = this.paymentRepository.findById(paymentId == null ? 0L : paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(PAYMENT_NOT_FOUND.getMessage()));
        Arrays.stream(payment.getRooms()).forEach(
                roomId -> {
                    this.clientRoomRepository.deleteById(
                            ClientRoomPK.builder().clientId(payment.getClientId()).roomId(roomId).build()
                    );
                    Room room = this.roomRepository.findById(roomId)
                            .orElseThrow(() -> new RoomNotFoundException(ROOM_NOT_FOUND.getMessage()));
                    room.setRented(false);
                    room.setRentedFrom(null);
                    room.setRentedTo(null);
                    this.roomRepository.save(room);
                }
        );
        payment.setCancelled(true);
        this.paymentRepository.save(payment);
        return PAYMENT_CANCELLED.getMessage();
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String earlyPaymentReservationReturn(final Long paymentId) {
        Payment payment = this.paymentRepository.findById(paymentId == null ? 0L : paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(PAYMENT_NOT_FOUND.getMessage()));
        LocalDateTime now = LocalDateTime.now();
        Arrays.stream(payment.getRooms()).forEach(
                roomId -> {
                    Room room = this.roomRepository.findById(roomId)
                            .orElseThrow(() -> new RoomNotFoundException(ROOM_NOT_FOUND.getMessage()));
                    room.setRentedTo(now);
                    room.setRented(false);
                    payment.setCost(0L);
                    Room savedRoom = this.roomRepository.save(room);
                    Payment savedPayment = this.paymentRepository.save(payment);
                    Long hours = ChronoUnit.HOURS.between(room.getRentedFrom(), now);
                    savedPayment.setCost(savedPayment.getCost() + (savedRoom.getHourPrice() + hours));
                    this.paymentRepository.save(savedPayment);
                }
        );
        return EARLY_PAYMENT_AND_RETURN_RESERVATION.getMessage();
    }
}
