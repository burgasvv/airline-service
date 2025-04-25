package org.burgas.ticketservice.service;

import org.burgas.ticketservice.dto.RequireRequest;
import org.burgas.ticketservice.dto.RequireResponse;
import org.burgas.ticketservice.kafka.KafkaProducer;
import org.burgas.ticketservice.mapper.RequireMapper;
import org.burgas.ticketservice.repository.RequireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class RequireService {

    private final RequireRepository requireRepository;
    private final RequireMapper requireMapper;
    private final KafkaProducer kafkaProducer;

    public RequireService(RequireRepository requireRepository, RequireMapper requireMapper, KafkaProducer kafkaProducer) {
        this.requireRepository = requireRepository;
        this.requireMapper = requireMapper;
        this.kafkaProducer = kafkaProducer;
    }

    public List<RequireResponse> findAllByClosed(final String closed) {
        return this.requireRepository.findRequiresByClosed(Boolean.parseBoolean(closed))
                .stream()
                .map(this.requireMapper::toRequireResponse)
                .toList();
    }

    public RequireResponse findById(final String requireId) {
        return this.requireRepository.findById(Long.valueOf(requireId))
                .map(this.requireMapper::toRequireResponse)
                .orElseGet(RequireResponse::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public RequireResponse createOrUpdate(final RequireRequest requireRequest) {
        return Optional.of(this.requireMapper.toRequire(requireRequest))
                .map(this.requireRepository::save)
                .map(this.requireMapper::toRequireResponse)
                .map(
                        requireResponse -> {
                            this.kafkaProducer.sendStringRequireMessage(requireResponse);
                            return requireResponse;
                        }
                )
                .orElseThrow();
    }
}
