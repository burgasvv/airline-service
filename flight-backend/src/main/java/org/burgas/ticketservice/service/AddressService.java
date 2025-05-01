package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.AddressRequest;
import org.burgas.ticketservice.dto.AddressResponse;
import org.burgas.ticketservice.exception.AddressNotCreatedException;
import org.burgas.ticketservice.exception.AddressNotFoundException;
import org.burgas.ticketservice.log.AddressLogs;
import org.burgas.ticketservice.mapper.AddressMapper;
import org.burgas.ticketservice.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.*;
import static org.burgas.ticketservice.log.AddressLogs.ADDRESS_FOUND_ALL;
import static org.burgas.ticketservice.log.AddressLogs.ADDRESS_FOUND_ALL_ASYNC;
import static org.burgas.ticketservice.message.AddressMessages.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public List<AddressResponse> findAll() {
        return this.addressRepository.findAll()
                .stream()
                .peek(address -> log.info(ADDRESS_FOUND_ALL.getLogMessage(), address))
                .map(this.addressMapper::toAddressResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<AddressResponse>> findAllAsync() {
        return supplyAsync(this.addressRepository::findAll)
                .thenApplyAsync(
                        addresses -> addresses.stream()
                                .peek(address -> log.info(ADDRESS_FOUND_ALL_ASYNC.getLogMessage(), address))
                                .map(this.addressMapper::toAddressResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<AddressResponse> findAllPages(final Integer page, final Integer size) {
        return this.addressRepository
                .findAll(PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "id"))
                .map(this.addressMapper::toAddressResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public AddressResponse createOrUpdateSecured(final AddressRequest addressRequest) {
        return of(this.addressMapper.toAddress(addressRequest))
                .map(this.addressRepository::save)
                .map(this.addressMapper::toAddressResponse)
                .orElseThrow(() -> new AddressNotCreatedException(ADDRESS_NOT_CREATED.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<AddressResponse> createOrUpdateSecuresAsync(final AddressRequest addressRequest) {
        return supplyAsync(() -> this.addressMapper.toAddress(addressRequest))
                .thenApplyAsync(this.addressRepository::save)
                .thenApplyAsync(this.addressMapper::toAddressResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final String addressId) {
        return this.addressRepository.findById(Long.parseLong(addressId))
                .map(
                        address -> {
                            this.addressRepository.deleteById(address.getId());
                            return ADDRESS_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new AddressNotFoundException(ADDRESS_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final String addressId) {
        return supplyAsync(() -> this.addressRepository.findById(Long.parseLong(addressId)))
                .thenApplyAsync(
                        address -> address.stream()
                                .peek(foundAddress -> log.info(AddressLogs.ADDRESS_FOUND_BEFORE_DELETE.getLogMessage(), foundAddress))
                                .map(
                                        foundAddress -> {
                                            this.addressRepository.deleteById(foundAddress.getId());
                                            return ADDRESS_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new AddressNotFoundException(ADDRESS_NOT_FOUND.getMessage())
                                )
                );
    }
}
