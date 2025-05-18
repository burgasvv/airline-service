package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.ClientRequest;
import org.burgas.hotelbackend.dto.ClientResponse;
import org.burgas.hotelbackend.entity.Client;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.ClientRepository;
import org.burgas.hotelbackend.repository.IdentityRepository;
import org.springframework.stereotype.Component;

@Component
public final class ClientMapper implements MapperDataHandler<ClientRequest, Client, ClientResponse> {

    private final ClientRepository clientRepository;
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public ClientMapper(ClientRepository clientRepository, IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.clientRepository = clientRepository;
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    @Override
    public Client toEntity(ClientRequest clientRequest) {
        Long clientId = this.getData(clientRequest.getId(), 0L);
        return this.clientRepository.findById(clientId)
                .map(
                        client -> Client.builder()
                                .id(client.getId())
                                .name(this.getData(clientRequest.getName(), client.getName()))
                                .surname(this.getData(clientRequest.getSurname(), client.getSurname()))
                                .patronymic(this.getData(clientRequest.getPatronymic(), client.getPatronymic()))
                                .passport(this.getData(clientRequest.getPassport(), client.getPassport()))
                                .identityId(this.getData(clientRequest.getIdentityId(), client.getIdentityId()))
                                .build()
                )
                .orElseGet(
                        () -> Client.builder()
                                .name(clientRequest.getName())
                                .surname(clientRequest.getSurname())
                                .patronymic(clientRequest.getPatronymic())
                                .passport(clientRequest.getPassport())
                                .identityId(clientRequest.getIdentityId())
                                .build()
                );
    }

    @Override
    public ClientResponse toResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .patronymic(client.getPatronymic())
                .passport(client.getPassport())
                .identity(
                        this.identityRepository.findById(client.getIdentityId() == null ? 0L : client.getIdentityId())
                                .map(this.identityMapper::toResponse)
                                .orElse(null)
                )
                .build();
    }
}
