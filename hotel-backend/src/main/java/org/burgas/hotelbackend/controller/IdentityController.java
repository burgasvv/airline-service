package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.IdentityRequest;
import org.burgas.hotelbackend.dto.IdentityResponse;
import org.burgas.hotelbackend.exception.WrongContentTypeException;
import org.burgas.hotelbackend.service.IdentityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.burgas.hotelbackend.message.ImageMessages.WRONG_CONTENT_TYPE;
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

    @PostMapping(value = "/upload-image")
    public @ResponseBody ResponseEntity<String> uploadIdentityImage(@RequestParam Long identityId, @RequestPart MultipartFile file) {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            String answer = this.identityService.uploadIdentityImage(identityId, file);
            return ResponseEntity
                    .status(OK)
                    .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                    .body(answer);
        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PostMapping(value = "/upload-image/async")
    public @ResponseBody ResponseEntity<String> uploadIdentityImageAsync(@RequestParam Long identityId, @RequestPart MultipartFile file)
            throws ExecutionException, InterruptedException {

        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            String answer = this.identityService.uploadIdentityImageAsync(identityId, file).get();
            HttpHeaders httpHeaders = new HttpHeaders(
                    MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, TEXT_PLAIN_VALUE))
            );
            return new ResponseEntity<>(answer, httpHeaders, OK);

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PutMapping(value = "/change-image")
    public @ResponseBody ResponseEntity<String> changeIdentityImage(@RequestParam Long identityId, @RequestPart MultipartFile file) {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(OK)
                    .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                    .body(this.identityService.changeIdentityImage(identityId, file));

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PutMapping(value = "/change-image/async")
    public @ResponseBody ResponseEntity<String> changeIdentityImageAsync(@RequestParam Long identityId, @RequestPart MultipartFile file)
            throws ExecutionException, InterruptedException {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            String answer = this.identityService.changeIdentityImageAsync(identityId, file).get();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
            return new ResponseEntity<>(answer, httpHeaders, OK);

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image")
    public @ResponseBody ResponseEntity<String> deleteIdentityImage(@RequestParam Long identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.identityService.deleteIdentityImage(identityId));
    }

    @DeleteMapping(value = "/delete-image/async")
    public @ResponseBody ResponseEntity<String> deleteIdentityImageAsync(@RequestParam Long identityId) throws ExecutionException, InterruptedException {
        String answer = this.identityService.deleteIdentityImageAsync(identityId).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of("Content-Type", TEXT_PLAIN_VALUE))
        );
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
