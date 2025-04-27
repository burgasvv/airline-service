package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.FilialRequest;
import org.burgas.ticketservice.dto.FilialResponse;
import org.burgas.ticketservice.exception.AddressNotCreatedException;
import org.burgas.ticketservice.exception.FilialNotCreatedException;
import org.burgas.ticketservice.mapper.AddressMapper;
import org.burgas.ticketservice.mapper.FilialMapper;
import org.burgas.ticketservice.message.FilialMessages;
import org.burgas.ticketservice.repository.AddressRepository;
import org.burgas.ticketservice.repository.FilialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.ticketservice.log.FilialLogs.*;
import static org.burgas.ticketservice.message.AddressMessages.ADDRESS_NOT_CREATED;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FilialService {

    private static final Logger log = LoggerFactory.getLogger(FilialService.class);

    private final FilialRepository filialRepository;
    private final FilialMapper filialMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public FilialService(
            FilialRepository filialRepository, FilialMapper filialMapper,
            AddressRepository addressRepository, AddressMapper addressMapper
    ) {
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Scheduled(timeUnit = SECONDS, fixedRate = 50)
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public void checkFilialSchedule() {
        this.filialRepository.findAll()
                .forEach(
                        filial -> {
                            LocalTime now = LocalTime.now();
                            filial.setOpened(now.isAfter(filial.getOpensAt()) && now.isBefore(filial.getClosesAt()));
                            this.filialRepository.save(filial);
                        }
                );
    }

    public List<FilialResponse> findAll() {
        return this.filialRepository.findAll()
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_ALL.getLogMessage(), filial))
                .map(this.filialMapper::toFilialResponse)
                .collect(Collectors.toList());
    }

    public List<FilialResponse> findByCountryId(final String countryId) {
        return this.filialRepository.findFilialsByCountryId(Long.valueOf(countryId))
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_BY_COUNTRY_ID.getLogMessage(), filial))
                .map(this.filialMapper::toFilialResponse)
                .collect(Collectors.toList());
    }

    public List<FilialResponse> findByCityId(final String cityId) {
        return this.filialRepository.findFilialsByCityId(Long.valueOf(cityId))
                .stream()
                .peek(filial -> log.info(FILIAL_FOUND_BY_CITY_ID.getLogMessage(), filial))
                .map(this.filialMapper::toFilialResponse)
                .collect(Collectors.toList());
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public FilialResponse createOrUpdate(final FilialRequest filialRequest) {
        return of(this.addressMapper.toAddress(filialRequest.getAddress()))
                .map(this.addressRepository::save)
                .map(
                        address -> {
                            filialRequest.getAddress().setId(address.getId());
                            return of(this.filialMapper.toFilial(filialRequest))
                                    .map(this.filialRepository::save)
                                    .map(this.filialMapper::toFilialResponse)
                                    .orElseThrow(
                                            () -> new FilialNotCreatedException(FilialMessages.FILIAL_NOT_CREATED.getMessage())
                                    );
                        }
                )
                .orElseThrow(
                        () -> new AddressNotCreatedException(ADDRESS_NOT_CREATED.getMessage())
                );
    }
}
