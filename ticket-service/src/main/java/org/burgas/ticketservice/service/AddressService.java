package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AddressRequest;
import org.burgas.ticketservice.dto.AddressResponse;
import org.burgas.ticketservice.mapper.AddressMapper;
import org.burgas.ticketservice.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public Flux<AddressResponse> findAll() {
        return this.addressRepository.findAll()
                .flatMap(address -> this.addressMapper.toAddressResponse(Mono.fromCallable(() -> address)));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<AddressResponse> createOrUpdateSecured(final Mono<AddressRequest> addressRequestMono) {
        return addressRequestMono.flatMap(
                addressRequest -> this.addressMapper.toAddress(Mono.fromCallable(() -> addressRequest))
                        .flatMap(this.addressRepository::save)
                        .flatMap(address -> this.addressMapper.toAddressResponse(Mono.fromCallable(() -> address)))
        );
    }
}
