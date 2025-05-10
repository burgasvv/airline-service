package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.AddressRequest;
import org.burgas.hotelbackend.dto.AddressResponse;
import org.burgas.hotelbackend.entity.Address;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.AddressRepository;
import org.burgas.hotelbackend.repository.CityRepository;
import org.springframework.stereotype.Component;

@Component
public final class AddressMapper implements MapperDataHandler<AddressRequest, Address, AddressResponse> {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public AddressMapper(AddressRepository addressRepository, CityRepository cityRepository, CityMapper cityMapper) {
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    @Override
    public Address toEntity(AddressRequest addressRequest) {
        Long addressId = this.getData(addressRequest.getId(), 0L);
        return this.addressRepository.findById(addressId)
                .map(
                        address -> Address.builder()
                                .id(address.getId())
                                .cityId(this.getData(addressRequest.getCityId(), address.getCityId()))
                                .street(this.getData(addressRequest.getStreet(), address.getStreet()))
                                .house(this.getData(addressRequest.getHouse(), address.getHouse()))
                                .apartment(this.getData(addressRequest.getApartment(), address.getApartment()))
                                .build()
                )
                .orElseGet(
                        () -> Address.builder()
                                .cityId(addressRequest.getCityId())
                                .street(addressRequest.getStreet())
                                .house(addressRequest.getHouse())
                                .apartment(addressRequest.getApartment())
                                .build()
                );
    }

    @Override
    public AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .city(
                        this.cityRepository.findById(address.getCityId())
                                .map(this.cityMapper::toResponse)
                                .orElse(null)
                )
                .street(address.getStreet())
                .house(address.getHouse())
                .apartment(address.getApartment())
                .build();
    }
}
