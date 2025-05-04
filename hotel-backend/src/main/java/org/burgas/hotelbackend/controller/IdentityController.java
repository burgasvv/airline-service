package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.IdentityRequest;
import org.burgas.hotelbackend.dto.IdentityResponse;
import org.burgas.hotelbackend.service.IdentityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/identities")
public class IdentityController {

    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<IdentityResponse>> getAllIdentities() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<IdentityResponse>> getAllIdentitiesAsync() throws ExecutionException, InterruptedException {
        List<IdentityResponse> identityResponses = this.identityService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(identityResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<IdentityResponse>> getAllIdentityPages(@PathVariable Integer page, @RequestParam Integer size) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findAllPages(page, size).getContent());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<IdentityResponse> getIdentityById(@RequestParam Long identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findById(identityId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<IdentityResponse> getIdentityByIdAsync(@RequestParam Long identityId)
            throws ExecutionException, InterruptedException {
        IdentityResponse identityResponse = this.identityService.findByIdAsync(identityId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(identityResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create")
    public @ResponseBody ResponseEntity<IdentityResponse> createIdentity(@RequestBody IdentityRequest identityRequest) {
        IdentityResponse identityResponse = this.identityService.createOrUpdate(identityRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(identityResponse,httpHeaders, OK);
    }

    @PostMapping(value = "/create/async")
    public @ResponseBody ResponseEntity<IdentityResponse> createIdentityAsync(@RequestBody IdentityRequest identityRequest)
            throws ExecutionException, InterruptedException {
        IdentityResponse identityResponse = this.identityService.createOrUpdateAsync(identityRequest).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of("Content-type", APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(identityResponse,httpHeaders, OK);
    }

    @PostMapping(value = "/update")
    public @ResponseBody ResponseEntity<IdentityResponse> updateIdentity(
            @RequestBody IdentityRequest identityRequest, @RequestParam Long identityId
    ) {
        identityRequest.setId(identityId);
        IdentityResponse identityResponse = this.identityService.createOrUpdate(identityRequest);
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(identityResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/update/async")
    public @ResponseBody ResponseEntity<IdentityResponse> updateIdentityAsync(
            @RequestBody IdentityRequest identityRequest, @RequestParam Long identityId
    ) throws ExecutionException, InterruptedException {
        identityRequest.setId(identityId);
        IdentityResponse identityResponse = this.identityService.createOrUpdateAsync(identityRequest).get();
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityResponse);
    }

    @PutMapping(value = "/activate-deactivate")
    public @ResponseBody ResponseEntity<String> activateOrDeactivateIdentity(@RequestParam Long identityId, @RequestParam Boolean enable) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.identityService.activateOrDeactivate(identityId, enable));
    }

    @PutMapping(value = "/activate-deactivate/async")
    public @ResponseBody ResponseEntity<String> activateOrDeactivateAsync(@RequestParam Long identityId, @RequestParam Boolean enable)
            throws ExecutionException, InterruptedException {
        String answer = this.identityService.activateOrDeactivateAsync(identityId, enable).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
