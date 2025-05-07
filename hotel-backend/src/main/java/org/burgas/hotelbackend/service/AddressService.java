package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.AddressRequest;
import org.burgas.hotelbackend.dto.AddressResponse;
import org.burgas.hotelbackend.exception.AddressNotCreatedOrUpdatedException;
import org.burgas.hotelbackend.exception.AddressNotFoundException;
import org.burgas.hotelbackend.mapper.AddressMapper;
import org.burgas.hotelbackend.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.hotelbackend.log.AddressLogs.*;
import static org.burgas.hotelbackend.message.AddressMessages.*;
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
        return this.addressRepository.findAll(PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "id"))
                .map(this.addressMapper::toAddressResponse);
    }

    public AddressResponse findById(final Long addressId) {
        return this.addressRepository.findById(addressId)
                .stream()
                .peek(address -> log.info(ADDRESS_FOUND_BY_ID.getLogMessage(), address))
                .map(this.addressMapper::toAddressResponse)
                .findFirst()
                .orElseGet(AddressResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<AddressResponse> findByIdAsync(final Long addressId) {
        return supplyAsync(() -> this.addressRepository.findById(addressId))
                .thenApplyAsync(
                        address -> address.stream()
                                .peek(foundAddress -> log.info(ADDRESS_FOUND_BY_ID_ASYNC.getLogMessage(), foundAddress))
                                .map(this.addressMapper::toAddressResponse)
                                .findFirst()
                                .orElseGet(AddressResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public AddressResponse createOrUpdate(final AddressRequest addressRequest) {
        return Optional.of(this.addressMapper.toAddress(addressRequest))
                .map(this.addressRepository::save)
                .stream()
                .peek(address -> log.info(ADDRESS_CREATE_OR_UPDATE.getLogMessage(), address))
                .map(this.addressMapper::toAddressResponse)
                .findFirst()
                .orElseThrow(
                        () -> new AddressNotCreatedOrUpdatedException(ADDRESS_NOT_CREATED_OR_UPDATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<AddressResponse> createOrUpdateAsync(final AddressRequest addressRequest) {
        return supplyAsync(() -> this.addressMapper.toAddress(addressRequest))
                .thenApplyAsync(this.addressRepository::save)
                .thenApplyAsync(
                        address -> Optional.of(address)
                                .stream()
                                .peek(foundAddress -> log.info(ADDRESS_CREATE_OR_UPDATE_ASYNC.getLogMessage(), foundAddress))
                                .map(this.addressMapper::toAddressResponse)
                                .findFirst()
                                .orElseThrow(
                                        () -> new AddressNotCreatedOrUpdatedException(ADDRESS_NOT_CREATED_OR_UPDATED.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long addressId) {
        return this.addressRepository.findById(addressId)
                .stream()
                .peek(address -> log.info(ADDRESS_FOUND_BEFORE_DELETE.getLogMessage(), address))
                .map(
                        address -> {
                            this.addressRepository.deleteById(address.getId());
                            return ADDRESS_DELETED_ASYNC.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new AddressNotFoundException(ADDRESS_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final Long addressId) {
        return supplyAsync(() -> this.addressRepository.findById(addressId))
                .thenApplyAsync(
                        address -> address.stream()
                                .peek(foundAddress -> log.info(ADDRESS_FOUND_BEFORE_DELETE_ASYNC.getLogMessage(), foundAddress))
                                .map(
                                        foundAddress -> {
                                            this.addressRepository.deleteById(foundAddress.getId());
                                            return ADDRESS_DELETED_ASYNC.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new AddressNotFoundException(ADDRESS_NOT_FOUND.getMessage())
                                )
                );
    }
}
