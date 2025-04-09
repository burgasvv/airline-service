package org.burgas.ticketservice.mapper;

import org.burgas.ticketservice.dto.FilialRequest;
import org.burgas.ticketservice.dto.FilialResponse;
import org.burgas.ticketservice.entity.Filial;
import org.burgas.ticketservice.repository.AddressRepository;
import org.burgas.ticketservice.repository.FilialRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class FilialMapper {

    private final FilialRepository filialRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public FilialMapper(FilialRepository filialRepository, AddressRepository addressRepository, AddressMapper addressMapper) {
        this.filialRepository = filialRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    private <T> T getData(final T first, final T second) {
        return first == null || first == "" ? second : first;
    }

    public Mono<Filial> toFilial(final Mono<FilialRequest> filialRequestMono) {
        return filialRequestMono.flatMap(
                filialRequest -> {
                    Long filialId = this.getData(filialRequest.getId(), 0L);
                    return this.filialRepository.findById(filialId)
                            .flatMap(
                                    filial -> Mono.fromCallable(() ->
                                            Filial.builder()
                                                    .id(filial.getId())
                                                    .name(this.getData(filialRequest.getName(), filial.getName()))
                                                    .addressId(this.getData(filialRequest.getAddress().getId(), filial.getAddressId()))
                                                    .opensAt(this.getData(filialRequest.getOpensAt(), filial.getOpensAt()))
                                                    .closesAt(this.getData(filialRequest.getClosesAt(), filial.getClosesAt()))
                                                    .opened(this.getData(filialRequest.getOpened(), filial.getOpened()))
                                                    .isNew(false)
                                                    .build())
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(() ->
                                            Filial.builder()
                                                    .name(filialRequest.getName())
                                                    .addressId(filialRequest.getAddress().getId())
                                                    .opensAt(filialRequest.getOpensAt())
                                                    .closesAt(filialRequest.getClosesAt())
                                                    .opened(filialRequest.getOpened())
                                                    .isNew(true)
                                                    .build())
                            );
                }
        );
    }

    public Mono<FilialResponse> toFilialResponse(final Mono<Filial> filialMono) {
        return filialMono.flatMap(
                filial -> this.addressRepository.findById(filial.getAddressId())
                        .flatMap(
                                address -> this.addressMapper.toAddressResponse(Mono.fromCallable(() -> address))
                                        .flatMap(
                                                addressResponse -> Mono.fromCallable(() ->
                                                        FilialResponse.builder()
                                                                .id(filial.getId())
                                                                .name(filial.getName())
                                                                .address(addressResponse)
                                                                .opensAt(filial.getOpensAt().format(ofPattern("hh:mm")))
                                                                .closesAt(filial.getClosesAt().format(ofPattern("hh:mm")))
                                                                .opened(filial.getOpened())
                                                                .build())
                                        )
                        )
        );
    }
}
