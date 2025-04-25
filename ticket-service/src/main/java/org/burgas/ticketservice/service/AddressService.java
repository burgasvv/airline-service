package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AddressRequest;
import org.burgas.ticketservice.dto.AddressResponse;
import org.burgas.ticketservice.mapper.AddressMapper;
import org.burgas.ticketservice.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public List<AddressResponse> findAll() {
        return this.addressRepository.findAll()
                .stream()
                .map(this.addressMapper::toAddressResponse)
                .toList();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public AddressResponse createOrUpdateSecured(final AddressRequest addressRequest) {
        return Optional.of(this.addressMapper.toAddress(addressRequest))
                .map(this.addressRepository::save)
                .map(this.addressMapper::toAddressResponse)
                .orElseGet(AddressResponse::new);
    }
}
