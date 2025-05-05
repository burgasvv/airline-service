package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.AuthorityRequest;
import org.burgas.hotelbackend.dto.AuthorityResponse;
import org.burgas.hotelbackend.service.AuthorityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

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
        List<AuthorityResponse> authorityResponses = this.authorityService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(authorityResponses, httpHeaders, OK);
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
        AuthorityResponse authorityResponse = this.authorityService.findByIdAsync(authorityId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(authorityResponse, httpHeaders, OK);
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
        String answer = this.authorityService.deleteByIdAsync(authorityId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", new MediaType(TEXT_PLAIN, UTF_8).getType());
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
