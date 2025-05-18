package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.RentRoom;
import org.burgas.hotelbackend.dto.RoomRequest;
import org.burgas.hotelbackend.dto.RoomResponse;
import org.burgas.hotelbackend.entity.*;
import org.burgas.hotelbackend.exception.*;
import org.burgas.hotelbackend.mapper.RoomMapper;
import org.burgas.hotelbackend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.hotelbackend.log.RoomLogs.*;
import static org.burgas.hotelbackend.message.ClientMessages.*;
import static org.burgas.hotelbackend.message.FilialMessages.FILIAL_NOT_FOUND;
import static org.burgas.hotelbackend.message.RoomMessages.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    private final ClientRepository clientRepository;
    private final FilialRepository filialRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final ClientRoomRepository clientRoomRepository;
    private final PaymentRepository paymentRepository;
    private final RoomImageRepository roomImageRepository;
    private final ImageService imageService;

    public RoomService(
            ClientRepository clientRepository, FilialRepository filialRepository,
            RoomRepository roomRepository, RoomMapper roomMapper,
            ClientRoomRepository clientRoomRepository, PaymentRepository paymentRepository,
            RoomImageRepository roomImageRepository, ImageService imageService
    ) {
        this.clientRepository = clientRepository;
        this.filialRepository = filialRepository;
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.clientRoomRepository = clientRoomRepository;
        this.paymentRepository = paymentRepository;
        this.roomImageRepository = roomImageRepository;
        this.imageService = imageService;
    }

    @Scheduled(timeUnit = SECONDS, fixedRate = 10)
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public void scheduleRoomRenting() {
        this.roomRepository.findAll().forEach(
                room -> {

                    LocalDateTime now = LocalDateTime.now();
                    if (room.getRentedFrom() != null && room.getRentedTo() != null) {

                        if (now.isAfter(room.getRentedFrom()) && now.isBefore(room.getRentedTo())) {
                            room.setRented(true);
                            this.roomRepository.save(room);

                        } else {
                            room.setRented(false);
                            this.roomRepository.save(room);
                        }
                    }
                }
        );
    }

    public List<RoomResponse> findByFilialId(final Long filialId) {
        return this.roomRepository.findRoomsByFilialId(filialId == null ? 0L : filialId)
                .parallelStream()
                .peek(room -> log.info(ROOM_FOUND_BY_FILIAL_ID.getLog(), room))
                .map(this.roomMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<RoomResponse>> findByFilialIdAsync(final Long filialId) {
        return supplyAsync(() -> this.roomRepository.findRoomsByFilialId(filialId == null ? 0L : filialId))
                .thenApplyAsync(
                        rooms -> rooms.parallelStream()
                                .peek(room -> log.info(ROOM_FOUND_BY_FILIAL_ID_ASYNC.getLog(), room))
                                .map(this.roomMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<RoomResponse> findPagesByFilialId(final Long filialId, final Integer page, final Integer size) {
        return this.roomRepository.findRoomsByFilialId(
                        filialId == null ? 0L : filialId, PageRequest.of(page - 1, size, Sort.Direction.ASC, "id")
                )
                .map(this.roomMapper::toResponse);
    }

    public RoomResponse findById(final Long roomId) {
        return this.roomRepository.findById(roomId == null ? 0L : roomId)
                .stream()
                .peek(room -> log.info(ROOM_FOUND_BY_ID.getLog(), room))
                .map(this.roomMapper::toResponse)
                .findFirst()
                .orElseGet(RoomResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<RoomResponse> findByIdAsync(final Long roomId) {
        return supplyAsync(() -> this.roomRepository.findById(roomId == null ? 0L : roomId))
                .thenApplyAsync(
                        room -> room.stream()
                                .peek(foundRoom -> log.info(ROOM_FOUND_BY_ID_ASYNC.getLog(), foundRoom))
                                .map(this.roomMapper::toResponse)
                                .findFirst()
                                .orElseGet(RoomResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public RoomResponse createOrUpdate(final RoomRequest roomRequest) {
        return ofNullable(this.roomMapper.toEntity(roomRequest))
                .map(this.roomRepository::save)
                .stream()
                .peek(room -> log.info(ROOM_CREATED_OR_UPDATED.getLog(), room))
                .map(this.roomMapper::toResponse)
                .findFirst()
                .orElseThrow(
                        () -> new RoomNotCreatedOrUpdatedException(ROOM_NOT_CREATED_OR_UPDATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<RoomResponse> createOrUpdateAsync(final RoomRequest roomRequest) {
        return supplyAsync(() -> this.roomMapper.toEntity(roomRequest))
                .thenApplyAsync(this.roomRepository::save)
                .thenApplyAsync(
                        room -> ofNullable(room).stream()
                                .peek(foundRoom -> log.info(ROOM_CREATED_OR_UPDATED_ASYNC.getLog(), foundRoom))
                                .map(this.roomMapper::toResponse)
                                .findFirst()
                                .orElseThrow(
                                        () -> new RoomNotCreatedOrUpdatedException(ROOM_NOT_CREATED_OR_UPDATED.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long roomId) {
        return this.roomRepository.findById(roomId == null ? 0L : roomId)
                .stream()
                .peek(room -> log.info(ROOM_FOUND_BEFORE_DELETE.getLog(), room))
                .map(
                        room -> {
                            this.roomRepository.deleteById(room.getId());
                            return ROOM_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new RoomNotFoundException(ROOM_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final Long roomId) {
        return supplyAsync(() -> this.roomRepository.findById(roomId == null ? 0L : roomId))
                .thenApplyAsync(
                        room -> room.stream()
                                .peek(foundRoom -> log.info(ROOM_FOUND_BEFORE_DELETE_ASYNC.getLog(), foundRoom))
                                .map(
                                        foundRoom -> {
                                            this.roomRepository.deleteById(foundRoom.getId());
                                            return ROOM_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new RoomNotFoundException(ROOM_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String rentClientRoom(final RentRoom rentRoom) {
        Client client = this.clientRepository.findById(rentRoom.getClientId() == null ? 0L : rentRoom.getClientId())
                .orElseThrow(() -> new ClientNotFoundException(CLIENT_NOT_FOUND.getMessage()));
        Filial filial = this.filialRepository.findFilialByRoomId(rentRoom.getRoomId() == null ? 0L : rentRoom.getRoomId())
                .orElseThrow(() -> new FilialNotFoundException(FILIAL_NOT_FOUND.getMessage()));
        Room room = this.roomRepository.findById(rentRoom.getRoomId() == null ? 0L : rentRoom.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(ROOM_NOT_FOUND.getMessage()));

        return this.paymentRepository.findPaymentByClientIdAndClosedAndCancelled(client.getId(), false, false)
                .map(
                        payment -> {
                            if (!room.getRented()) {

                                if (room.getRentedFrom() == null && room.getRentedTo() == null) {
                                    room.setRentedFrom(rentRoom.getRentedFrom());
                                    room.setRentedTo(rentRoom.getRentedTo());
                                    Room savedRoom = this.roomRepository.save(room);
                                    this.clientRoomRepository.save(
                                            ClientRoom.builder().clientId(client.getId()).roomId(savedRoom.getId()).build()
                                    );
                                    Long hours = ChronoUnit.HOURS.between(savedRoom.getRentedFrom(), savedRoom.getRentedTo());
                                    payment.setCost(payment.getCost() + (hours * savedRoom.getHourPrice()));
                                    List<ClientRoom> clientRooms = this.clientRoomRepository.findClientRoomsByClientId(client.getId());
                                    Long[] roomIds = clientRooms.parallelStream()
                                            .map(ClientRoom::getRoomId)
                                            .toArray(Long[]::new);
                                    payment.setRooms(roomIds);
                                    this.paymentRepository.save(payment);

                                } else if (
                                        (rentRoom.getRentedFrom().isAfter(room.getRentedFrom()) && rentRoom.getRentedFrom().isBefore(room.getRentedTo())) ||
                                        rentRoom.getRentedTo().isAfter(room.getRentedFrom())
                                ) {
                                    throw new WrongRentedDateException(WRONG_RENTED_DATE.getMessage());
                                }

                            } else {
                                throw new RoomAlreadyRentedException(ROOM_ALREADY_RENTED.getMessage());
                            }

                            return ROOM_WAS_RENTED_PAYMENT_UPDATED.getMessage();
                        }
                )
                .orElseGet(
                        () -> {
                            if (!room.getRented()) {

                                if (room.getRentedFrom() == null && room.getRentedTo() == null) {
                                    room.setRentedFrom(rentRoom.getRentedFrom());
                                    room.setRentedTo(rentRoom.getRentedTo());
                                    Room savedRoom = this.roomRepository.save(room);
                                    this.clientRoomRepository.save(
                                            ClientRoom.builder().clientId(client.getId()).roomId(savedRoom.getId()).build()
                                    );
                                    Long hours = ChronoUnit.HOURS.between(savedRoom.getRentedFrom(), savedRoom.getRentedTo());
                                    List<ClientRoom> clientRooms = this.clientRoomRepository.findClientRoomsByClientId(client.getId());
                                    Long[] roomIds = clientRooms.parallelStream()
                                            .map(ClientRoom::getRoomId)
                                            .toArray(Long[]::new);

                                    Payment payment = Payment.builder()
                                            .clientId(client.getId())
                                            .filialId(filial.getId())
                                            .rooms(roomIds)
                                            .cost(hours * savedRoom.getHourPrice())
                                            .closed(false)
                                            .cancelled(false)
                                            .build();
                                    this.paymentRepository.save(payment);

                                } else if (
                                        (rentRoom.getRentedFrom().isAfter(room.getRentedFrom()) && rentRoom.getRentedFrom().isBefore(room.getRentedTo())) ||
                                        rentRoom.getRentedTo().isAfter(room.getRentedFrom())
                                ) {
                                    throw new WrongRentedDateException(WRONG_RENTED_DATE.getMessage());
                                }

                            } else {
                                throw new RoomAlreadyRentedException(ROOM_ALREADY_RENTED.getMessage());
                            }

                            return ROOM_WAS_RENTED_PAYMENT_CREATED.getMessage();
                        }
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadRoomImages(final Long roomId, final List<MultipartFile> multipartFiles) {
        return this.roomRepository.findById(roomId == null ? 0L : roomId)
                .stream()
                .peek(room -> log.info(ROOM_FOUND_BEFORE_UPLOAD_IMAGES.getLog(), room))
                .map(
                        room -> {
                            multipartFiles.forEach(
                                    file -> {
                                        Image image = this.imageService.uploadImage(file);
                                        this.roomImageRepository.save(
                                                RoomImage.builder().roomId(room.getId()).imageId(image.getId()).build()
                                        );
                                    }
                            );

                            return ROOM_IMAGES_UPLOADED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new RoomNotFoundException(ROOM_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadRoomImagesAsync(final Long roomId, final List<MultipartFile> multipartFiles) {
        return supplyAsync(() -> this.roomRepository.findById(roomId == null ? 0L : roomId))
                .thenApplyAsync(
                        room -> room.stream()
                                .peek(foundRoom -> log.info(ROOM_FOUND_BEFORE_UPLOAD_IMAGES_ASYNC.getLog(), foundRoom))
                                .map(
                                        foundRoom -> {
                                            multipartFiles.forEach(
                                                    file -> {
                                                        try {
                                                            Image image = this.imageService.uploadImageAsync(file).get();
                                                            this.roomImageRepository.save(
                                                                    RoomImage.builder().roomId(foundRoom.getId()).imageId(image.getId()).build()
                                                            );

                                                        } catch (InterruptedException | ExecutionException e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    }
                                            );

                                            return ROOM_IMAGES_UPLOADED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new RoomNotFoundException(ROOM_NOT_FOUND.getMessage())
                                )
                );
    }
}
