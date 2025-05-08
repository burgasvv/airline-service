package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.HotelRequest;
import org.burgas.hotelbackend.dto.HotelResponse;
import org.burgas.hotelbackend.entity.Hotel;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.HotelRepository;
import org.burgas.hotelbackend.repository.StatusRepository;
import org.springframework.stereotype.Component;

@Component
public final class HotelMapper implements MapperDataHandler {

    private final HotelRepository hotelRepository;
    private final StatusRepository statusRepository;

    public HotelMapper(HotelRepository hotelRepository, StatusRepository statusRepository) {
        this.hotelRepository = hotelRepository;
        this.statusRepository = statusRepository;
    }

    public Hotel toHotel(final HotelRequest hotelRequest) {
        Long hotelId = this.getData(hotelRequest.getId(), 0L);
        return this.hotelRepository.findById(hotelId)
                .map(
                        hotel -> Hotel.builder()
                                .id(hotel.getId())
                                .name(this.getData(hotelRequest.getName(), hotel.getName()))
                                .statusId(this.getData(hotelRequest.getStatusId(), hotel.getStatusId()))
                                .description(this.getData(hotelRequest.getDescription(), hotel.getDescription()))
                                .imageId(this.getData(hotelRequest.getImageId(), hotel.getImageId()))
                                .build()
                )
                .orElseGet(
                        () -> Hotel.builder()
                                .name(hotelRequest.getName())
                                .statusId(hotelRequest.getStatusId())
                                .description(hotelRequest.getDescription())
                                .imageId(hotelRequest.getImageId())
                                .build()
                );
    }

    public HotelResponse toHotelResponse(final Hotel hotel) {
        return HotelResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .status(this.statusRepository.findById(hotel.getStatusId()).orElse(null))
                .description(hotel.getDescription())
                .imageId(hotel.getImageId())
                .build();
    }
}
