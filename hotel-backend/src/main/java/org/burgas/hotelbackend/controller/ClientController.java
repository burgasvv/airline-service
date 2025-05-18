package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.ClientRequest;
import org.burgas.hotelbackend.dto.ClientResponse;
import org.burgas.hotelbackend.service.ClientService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.clientService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<ClientResponse>> getAllClientsAsync() throws ExecutionException, InterruptedException {
        List<ClientResponse> clientResponses = this.clientService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(clientResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<ClientResponse>> getAllClientPages(@PathVariable Integer page, @RequestParam Integer size) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.clientService.findAllPages(page, size).getContent());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<ClientResponse> getClientById(@RequestParam Long clientId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.clientService.findById(clientId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<ClientResponse> getClientByIdAsync(@RequestParam Long clientId) throws ExecutionException, InterruptedException {
        ClientResponse clientResponse = this.clientService.findByIdAsync(clientId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(clientResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<ClientResponse> createOrUpdateClient(@RequestBody ClientRequest clientRequest) {
        ClientResponse clientResponse = this.clientService.createOrUpdate(clientRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/clients/by-id?clientId=" + clientResponse.getId()))
                .body(clientResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<ClientResponse> createOrUpdateAsync(@RequestBody ClientRequest clientRequest)
            throws ExecutionException, InterruptedException {
        ClientResponse clientResponse = this.clientService.createOrUpdateAsync(clientRequest).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(clientResponse, httpHeaders, OK);
    }
}
