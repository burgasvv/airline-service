package org.burgas.flightbackend.mapper;

import org.burgas.flightbackend.dto.AddressResponse;
import org.burgas.flightbackend.dto.FilialRequest;
import org.burgas.flightbackend.dto.FilialResponse;
import org.burgas.flightbackend.entity.Filial;
import org.burgas.flightbackend.handler.MapperDataHandler;
import org.burgas.flightbackend.repository.AddressRepository;
import org.burgas.flightbackend.repository.FilialRepository;
import org.springframework.stereotype.Component;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class FilialMapper implements MapperDataHandler {

    private final FilialRepository filialRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public FilialMapper(FilialRepository filialRepository, AddressRepository addressRepository, AddressMapper addressMapper) {
        this.filialRepository = filialRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public Filial toFilial(final FilialRequest filialRequest) {
        Long filialId = this.getData(filialRequest.getId(), 0L);
        return this.filialRepository.findById(filialId)
                .map(
                        filial -> Filial.builder()
                                .id(filial.getId())
                                .name(this.getData(filialRequest.getName(), filial.getName()))
                                .addressId(this.getData(filialRequest.getAddress().getId(), filial.getAddressId()))
                                .opensAt(this.getData(filialRequest.getOpensAt(), filial.getOpensAt()))
                                .closesAt(this.getData(filialRequest.getClosesAt(), filial.getClosesAt()))
                                .opened(this.getData(filialRequest.getOpened(), filial.getOpened()))
                                .build()
                )
                .orElseGet(
                        () -> Filial.builder()
                                .name(filialRequest.getName())
                                .addressId(filialRequest.getAddress().getId())
                                .opensAt(filialRequest.getOpensAt())
                                .closesAt(filialRequest.getClosesAt())
                                .opened(filialRequest.getOpened())
                                .build()
                );
    }

    public FilialResponse toFilialResponse(final Filial filial) {
        return FilialResponse.builder()
                .id(filial.getId())
                .name(filial.getName())
                .address(
                        this.addressRepository.findById(filial.getAddressId())
                                .map(this.addressMapper::toAddressResponse)
                                .orElseGet(AddressResponse::new)
                )
                .opensAt(filial.getOpensAt().format(ofPattern("hh:mm")))
                .closesAt(filial.getClosesAt().format(ofPattern("hh:mm")))
                .opened(filial.getOpened())
                .build();
    }
}
