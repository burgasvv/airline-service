package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.RoomRequest;
import org.burgas.hotelbackend.dto.RoomResponse;
import org.burgas.hotelbackend.entity.Room;
import org.burgas.hotelbackend.exception.RoomByNumberAndFilialIdAlreadyExistsException;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.FilialRepository;
import org.burgas.hotelbackend.repository.RoomRepository;
import org.burgas.hotelbackend.repository.RoomTypeRepository;
import org.springframework.stereotype.Component;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.burgas.hotelbackend.message.RoomMessages.ROOM_BY_NUMBER_AND_FILIAL_ALREADY_EXISTS;

@Component
public final class RoomMapper implements MapperDataHandler<RoomRequest, Room, RoomResponse> {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final FilialRepository filialRepository;
    private final FilialMapper filialMapper;

    public RoomMapper(
            RoomRepository roomRepository, RoomTypeRepository roomTypeRepository,
            FilialRepository filialRepository, FilialMapper filialMapper
    ) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
    }

    @Override
    public Room toEntity(RoomRequest roomRequest) {
        Long roomId = this.getData(roomRequest.getId(), 0L);
        return this.roomRepository.findById(roomId)
                .map(
                        room -> Room.builder()
                                    .id(room.getId())
                                    .number(this.getData(roomRequest.getNumber(), room.getNumber()))
                                    .roomTypeId(this.getData(roomRequest.getRoomTypeId(), room.getRoomTypeId()))
                                    .filialId(this.getData(roomRequest.getFilialId(), room.getFilialId()))
                                    .description(this.getData(roomRequest.getDescription(), room.getDescription()))
                                    .rented(this.getData(roomRequest.getRented(), room.getRented()))
                                    .rentedFrom(this.getData(roomRequest.getRentedFrom(), room.getRentedTo()))
                                    .rentedTo(this.getData(roomRequest.getRentedTo(), room.getRentedTo()))
                                    .hourPrice(this.getData(roomRequest.getHoutPrice(), room.getHourPrice()))
                                    .build()
                )
                .orElseGet(
                        () -> {
                            if (!this.roomRepository.existsRoomByNumberAndFilialId(roomRequest.getNumber(), roomRequest.getFilialId())) {
                                return Room.builder()
                                        .number(roomRequest.getNumber())
                                        .roomTypeId(roomRequest.getRoomTypeId())
                                        .filialId(roomRequest.getFilialId())
                                        .description(roomRequest.getDescription())
                                        .rented(false)
                                        .rentedFrom(null)
                                        .rentedTo(null)
                                        .hourPrice(roomRequest.getHoutPrice())
                                        .build();

                            } else {
                                throw new RoomByNumberAndFilialIdAlreadyExistsException(ROOM_BY_NUMBER_AND_FILIAL_ALREADY_EXISTS.getMessage());
                            }
                        }
                );
    }

    @Override
    public RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .number(room.getNumber())
                .roomType(this.roomTypeRepository.findById(room.getRoomTypeId()).orElse(null))
                .filial(
                        this.filialRepository.findById(room.getFilialId())
                                .map(this.filialMapper::toResponse)
                                .orElse(null)
                )
                .description(room.getDescription())
                .rented(room.getRented())
                .rentedFrom(
                        room.getRentedFrom() == null ? null : room.getRentedFrom().format(ofPattern("dd.MM.yyyy, hh:mm"))
                )
                .rentedTo(
                        room.getRentedTo() == null ? null : room.getRentedTo().format(ofPattern("dd.MM.yyyy, hh:mm"))
                )
                .hourPrice(room.getHourPrice())
                .build();
    }
}
