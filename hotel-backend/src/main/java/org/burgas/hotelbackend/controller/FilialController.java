package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.FilialRequest;
import org.burgas.hotelbackend.dto.FilialResponse;
import org.burgas.hotelbackend.exception.WrongContentTypeException;
import org.burgas.hotelbackend.service.FilialService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.burgas.hotelbackend.message.ImageMessages.WRONG_CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/filials")
public class FilialController {

    private final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<FilialResponse>> getAllFilials() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.filialService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<FilialResponse>> getAllFilialsAsync() throws ExecutionException, InterruptedException {
        List<FilialResponse> filialResponses = this.filialService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(filialResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<FilialResponse>> getAllFilialPages(@PathVariable Integer page, @RequestParam Integer size) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.filialService.findAllPages(page, size).getContent());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<FilialResponse> getFilialById(@RequestParam Long filialId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.filialService.findById(filialId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<FilialResponse> findFilialByIdAsync(@RequestParam Long filialId) throws ExecutionException, InterruptedException {
        FilialResponse filialResponse = this.filialService.findByIdAsync(filialId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(filialResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<FilialResponse> createOrUpdateFilial(@RequestBody FilialRequest filialRequest) {
        FilialResponse filialResponse = this.filialService.createOrUpdate(filialRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/filials/by-id?filialId=" + filialResponse.getId()))
                .body(filialResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<FilialResponse> createOrUpdateFilialAsync(@RequestBody FilialRequest filialRequest)
            throws ExecutionException, InterruptedException {
        FilialResponse filialResponse = this.filialService.createOrUpdateAsync(filialRequest).get();
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/filials/by-id/async?filialId=" + filialResponse.getId()))
                .body(filialResponse);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteFilialById(@RequestParam Long filialId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.filialService.deleteById(filialId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deleteFilialIdAsync(@RequestParam Long filialId) throws ExecutionException, InterruptedException {
        String answer = this.filialService.deleteByIdAsync(filialId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }

    @PostMapping(value = "/upload-image")
    public @ResponseBody ResponseEntity<String> uploadFilialImage(@RequestParam Long filialId, @RequestPart MultipartFile file) {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(OK)
                    .contentType(parseMediaType(file.getContentType()))
                    .body(this.filialService.uploadFilialImage(filialId, file));
        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PostMapping(value = "/upload-image/async")
    public @ResponseBody ResponseEntity<String> uploadFilialImageAsync(@RequestParam Long filialId, @RequestPart MultipartFile file)
            throws ExecutionException, InterruptedException {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            String answer = this.filialService.uploadFilialImageAsync(filialId, file).get();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", parseMediaType(file.getContentType()).getType());
            return new ResponseEntity<>(answer, httpHeaders, OK);

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PutMapping(value = "/change-image")
    public @ResponseBody ResponseEntity<String> changeFilialImage(@RequestParam Long filialId, @RequestPart MultipartFile file) {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(OK)
                    .contentType(parseMediaType(file.getContentType()))
                    .body(this.filialService.changeFilialImage(filialId, file));

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PutMapping(value = "/change-image/async")
    public @ResponseBody ResponseEntity<String> changeFilialImageAsync(@RequestParam Long filialId, @RequestPart MultipartFile file)
            throws ExecutionException, InterruptedException {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            String answer = this.filialService.changeFilialImageAsync(filialId, file).get();
            HttpHeaders httpHeaders = new HttpHeaders(
                    MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, parseMediaType(file.getContentType()).getType()))
            );
            return new ResponseEntity<>(answer, httpHeaders, OK);

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image")
    public @ResponseBody ResponseEntity<String> deleteFilialImage(@RequestParam Long filialId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.filialService.deleteFilialImage(filialId));
    }

    @DeleteMapping(value = "/delete-image/async")
    public @ResponseBody ResponseEntity<String> deleteFilialImageAsync(@RequestParam Long filialId) throws ExecutionException, InterruptedException {
        String answer = this.filialService.deleteFilialImageAsync(filialId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", new MediaType(TEXT_PLAIN, UTF_8).getType());
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
