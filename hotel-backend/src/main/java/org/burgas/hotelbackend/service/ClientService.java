package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.ClientRequest;
import org.burgas.hotelbackend.dto.ClientResponse;
import org.burgas.hotelbackend.exception.ClientNotCreatedOrUpdatedException;
import org.burgas.hotelbackend.mapper.ClientMapper;
import org.burgas.hotelbackend.repository.*;
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

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.hotelbackend.log.ClientLogs.*;
import static org.burgas.hotelbackend.message.ClientMessages.CLIENT_NOT_CREATED_OR_UPDATED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public List<ClientResponse> findAll() {
        return this.clientRepository.findAll(Sort.by("name", "surname", "patronymic"))
                .parallelStream()
                .peek(client -> log.info(CLIENT_FOUND_ALL.getLog(), client))
                .map(this.clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<ClientResponse>> findAllAsync() {
        return supplyAsync(() -> this.clientRepository.findAll(Sort.by("name", "surname", "patronymic")))
                .thenApplyAsync(
                        clients -> clients.parallelStream()
                                .peek(client -> log.info(CLIENT_FOUND_ALL_ASYNC.getLog(), client))
                                .map(this.clientMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<ClientResponse> findAllPages(final Integer page, final Integer size) {
        return this.clientRepository.findAll(
                        PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "name", "surname", "patronymic")
                )
                .map(this.clientMapper::toResponse);
    }

    public ClientResponse findById(final Long clientId) {
        return this.clientRepository.findById(clientId == null ? 0L : clientId)
                .stream()
                .peek(client -> log.info(CLIENT_FOUND_BY_ID.getLog(), client))
                .map(this.clientMapper::toResponse)
                .findFirst()
                .orElseGet(ClientResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<ClientResponse> findByIdAsync(final Long clientId) {
        return supplyAsync(() -> this.clientRepository.findById(clientId == null ? 0L : clientId))
                .thenApplyAsync(
                        client -> client.stream()
                                .peek(foundClient -> log.info(CLIENT_FOUND_BY_ID_ASYNC.getLog(), foundClient))
                                .map(this.clientMapper::toResponse)
                                .findFirst()
                                .orElseGet(ClientResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public ClientResponse createOrUpdate(final ClientRequest clientRequest) {
        return ofNullable(this.clientMapper.toEntity(clientRequest))
                .map(this.clientRepository::save)
                .stream()
                .peek(client -> log.info(CLIENT_FOUND_BEFORE_CREATE_OR_UPDATE.getLog(), client))
                .map(this.clientMapper::toResponse)
                .findFirst()
                .orElseThrow(
                        () -> new ClientNotCreatedOrUpdatedException(CLIENT_NOT_CREATED_OR_UPDATED.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<ClientResponse> createOrUpdateAsync(final ClientRequest clientRequest) {
        return supplyAsync(() -> this.clientMapper.toEntity(clientRequest))
                .thenApplyAsync(this.clientRepository::save)
                .thenApplyAsync(
                        client -> ofNullable(client).stream()
                                .peek(createdOrUpdatedClient -> log.info(CLIENT_FOUND_BEFORE_CREATE_OR_UPDATE_ASYNC.getLog(), createdOrUpdatedClient))
                                .map(this.clientMapper::toResponse)
                                .findFirst()
                                .orElseThrow(
                                        () -> new ClientNotCreatedOrUpdatedException(CLIENT_NOT_CREATED_OR_UPDATED.getMessage())
                                )
                );
    }
}
