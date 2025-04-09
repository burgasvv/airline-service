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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalTime;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.INFO;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;
import static reactor.core.publisher.SignalType.ON_COMPLETE;

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
    public Flux<Void> checkFilialSchedule() {
        return this.filialRepository.findAll()
                .flatMap(
                        filial -> {
                            LocalTime now = LocalTime.now();
                            filial.setOpened(now.isAfter(filial.getOpensAt()) && now.isBefore(filial.getClosesAt()));
                            filial.setNew(false);
                            return this.filialRepository.save(filial).then();
                        }
                )
                .log("Filial schedule checked and fixed", INFO, ON_COMPLETE);
    }

    public Flux<FilialResponse> findAll() {
        return this.filialRepository.findAll()
                .flatMap(filial -> this.filialMapper.toFilialResponse(Mono.fromCallable(() -> filial)));
    }

    public Flux<FilialResponse> findByCountryId(final String countryId) {
        return this.filialRepository.findFilialsByCountryId(Long.valueOf(countryId))
                .flatMap(filial -> this.filialMapper.toFilialResponse(Mono.fromCallable(() -> filial)));
    }

    public Flux<FilialResponse> findByCityId(final String cityId) {
        return this.filialRepository.findFilialsByCityId(Long.valueOf(cityId))
                .flatMap(filial -> this.filialMapper.toFilialResponse(Mono.fromCallable(() -> filial)));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<FilialResponse> createOrUpdate(final Mono<FilialRequest> filialRequestMono) {
        return filialRequestMono.flatMap(
                filialRequest -> this.addressMapper.toAddress(Mono.fromCallable(filialRequest::getAddress))
                        .flatMap(this.addressRepository::save)
                        .flatMap(
                                address -> {
                                    filialRequest.getAddress().setId(address.getId());
                                    return this.filialMapper.toFilial(Mono.fromCallable(() -> filialRequest))
                                            .flatMap(this.filialRepository::save)
                                            .flatMap(filial -> this.filialMapper.toFilialResponse(Mono.fromCallable(() -> filial)));
                                }
                        )
        );
    }
}
