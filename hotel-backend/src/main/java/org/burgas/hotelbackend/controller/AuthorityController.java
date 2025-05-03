package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.AuthorityRequest;
import org.burgas.hotelbackend.dto.AuthorityResponse;
import org.burgas.hotelbackend.service.AuthorityService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@Controller
@RequestMapping(value = "/authorities")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<AuthorityResponse>> getAllAuthorities() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.authorityService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<AuthorityResponse>> getAllAuthoritiesAsync() throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.authorityService.findAllAsync().get());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<AuthorityResponse> getAuthorityById(@RequestParam Long authorityId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.authorityService.findById(authorityId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<AuthorityResponse> getAuthorityByIdAsync(@RequestParam Long authorityId)
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.authorityService.findByIdAsync(authorityId).get());
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<AuthorityResponse> createOrUpdateAuthority(@RequestBody AuthorityRequest authorityRequest) {
        AuthorityResponse authorityResponse = this.authorityService.createOrUpdate(authorityRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/authorities/by-id?authorityId=" + authorityResponse.getId()))
                .body(authorityResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<AuthorityResponse> createOrUpdateAuthorityAsync(@RequestBody AuthorityRequest authorityRequest)
            throws ExecutionException, InterruptedException {
        AuthorityResponse authorityResponse = this.authorityService.createOrUpdateAsync(authorityRequest).get();
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/authorities/by-id/async?authorityId=" + authorityResponse.getId()))
                .body(authorityResponse);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteAuthority(@RequestParam Long authorityId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.authorityService.deleteById(authorityId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deleteAuthorityAsync(@RequestParam Long authorityId) throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.authorityService.deleteByIdAsync(authorityId).get());
    }
}
