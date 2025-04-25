package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.FilialRequest;
import org.burgas.ticketservice.dto.FilialResponse;
import org.burgas.ticketservice.mapper.AddressMapper;
import org.burgas.ticketservice.mapper.FilialMapper;
import org.burgas.ticketservice.repository.AddressRepository;
import org.burgas.ticketservice.repository.FilialRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FilialService {

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
                .map(this.filialMapper::toFilialResponse)
                .toList();
    }

    public List<FilialResponse> findByCountryId(final String countryId) {
        return this.filialRepository.findFilialsByCountryId(Long.valueOf(countryId))
                .stream()
                .map(this.filialMapper::toFilialResponse)
                .toList();
    }

    public List<FilialResponse> findByCityId(final String cityId) {
        return this.filialRepository.findFilialsByCityId(Long.valueOf(cityId))
                .stream()
                .map(this.filialMapper::toFilialResponse)
                .toList();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public FilialResponse createOrUpdate(final FilialRequest filialRequest) {
        return Optional.of(this.addressMapper.toAddress(filialRequest.getAddress()))
                .map(this.addressRepository::save)
                .map(
                        address -> {
                            filialRequest.getAddress().setId(address.getId());
                            return Optional.of(this.filialMapper.toFilial(filialRequest))
                                    .map(this.filialRepository::save)
                                    .map(this.filialMapper::toFilialResponse)
                                    .orElseGet(FilialResponse::new);
                        }
                )
                .orElseThrow();
    }
}
