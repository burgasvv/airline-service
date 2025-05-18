package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.PaymentResponse;
import org.burgas.hotelbackend.dto.RoomResponse;
import org.burgas.hotelbackend.entity.Payment;
import org.burgas.hotelbackend.repository.ClientRepository;
import org.burgas.hotelbackend.repository.FilialRepository;
import org.burgas.hotelbackend.repository.RoomRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public final class PaymentMapper {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final FilialRepository filialRepository;
    private final FilialMapper filialMapper;

    public PaymentMapper(
            RoomRepository roomRepository, RoomMapper roomMapper, ClientRepository clientRepository, ClientMapper clientMapper,
            FilialRepository filialRepository, FilialMapper filialMapper
    ) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
    }

    public PaymentResponse toResponse(final Payment payment) {
        List<RoomResponse> rooms = Arrays.stream(payment.getRooms())
                .map(roomId -> this.roomRepository.findById(roomId).orElse(null))
                .filter(Objects::nonNull)
                .map(this.roomMapper::toResponse)
                .collect(Collectors.toList());
        return PaymentResponse.builder()
                .id(payment.getId())
                .client(
                        this.clientRepository.findById(payment.getClientId())
                                .map(this.clientMapper::toResponse)
                                .orElse(null)
                )
                .filial(
                        this.filialRepository.findById(payment.getFilialId())
                                .map(this.filialMapper::toResponse)
                                .orElse(null)
                )
                .rooms(rooms)
                .cost(payment.getCost())
                .closed(payment.getClosed())
                .build();
    }
}
