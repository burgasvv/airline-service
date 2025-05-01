package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.AddressRequest;
import org.burgas.ticketservice.dto.AddressResponse;
import org.burgas.ticketservice.dto.CityResponse;
import org.burgas.ticketservice.entity.Address;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.AddressRepository;
import org.burgas.ticketservice.repository.CityRepository;
import org.springframework.stereotype.Component;

@Component
public final class AddressMapper implements MapperDataHandler {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public AddressMapper(AddressRepository addressRepository, CityRepository cityRepository, CityMapper cityMapper) {
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    public Address toAddress(final AddressRequest addressRequest) {
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

    public AddressResponse toAddressResponse(final Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .city(
                        this.cityRepository.findById(address.getCityId())
                                .map(this.cityMapper::toCityResponse)
                                .orElseGet(CityResponse::new)
                )
                .street(address.getStreet())
                .house(address.getHouse())
                .apartment(address.getApartment())
                .build();
    }
}
