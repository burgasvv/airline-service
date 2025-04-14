package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.AddressRequest;
import org.burgas.ticketservice.dto.AddressResponse;
import org.burgas.ticketservice.entity.Address;
import org.burgas.ticketservice.handler.MapperDataHandler;
import org.burgas.ticketservice.repository.AddressRepository;
import org.burgas.ticketservice.repository.CityRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

    public Mono<Address> toAddress(final Mono<AddressRequest> addressRequestMono) {
        return addressRequestMono.flatMap(
                addressRequest -> {
                    Long addressId = this.getData(addressRequest.getId(), 0L);
                    return this.addressRepository.findById(addressId)
                            .flatMap(
                                    address -> Mono.fromCallable(
                                            () -> Address.builder()
                                                    .id(address.getId())
                                                    .cityId(this.getData(addressRequest.getCityId(), address.getCityId()))
                                                    .street(this.getData(addressRequest.getStreet(), address.getStreet()))
                                                    .house(this.getData(addressRequest.getHouse(), address.getHouse()))
                                                    .apartment(this.getData(addressRequest.getApartment(), address.getApartment()))
                                                    .isNew(false)
                                                    .build()
                                    )
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(
                                            () -> Address.builder()
                                                    .cityId(addressRequest.getCityId())
                                                    .street(addressRequest.getStreet())
                                                    .house(addressRequest.getHouse())
                                                    .apartment(addressRequest.getApartment())
                                                    .isNew(true)
                                                    .build()
                                    )
                            );
                }
        );
    }

    public Mono<AddressResponse> toAddressResponse(final Mono<Address> addressMono) {
        return addressMono.flatMap(
                address -> this.cityRepository.findById(address.getCityId())
                        .flatMap(city -> this.cityMapper.toCityResponse(Mono.fromCallable(() -> city)))
                        .flatMap(
                                cityResponse -> Mono.fromCallable(
                                        () -> AddressResponse.builder()
                                                .id(address.getId())
                                                .city(cityResponse)
                                                .street(address.getStreet())
                                                .house(address.getHouse())
                                                .apartment(address.getApartment())
                                                .build()
                                )
                        )
        );
    }
}
