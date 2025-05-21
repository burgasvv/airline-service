package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.PositionRequest;
import org.burgas.hotelbackend.dto.PositionResponse;
import org.burgas.hotelbackend.service.PositionService;
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
@RequestMapping(value = "/hotel-service/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<PositionResponse>> getAllPositions() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.positionService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<PositionResponse>> getAllPositionsAsync() throws ExecutionException, InterruptedException {
        List<PositionResponse> positionResponses = this.positionService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(positionResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<PositionResponse>> getAllPositionPages(@PathVariable Integer page, @RequestParam Integer size) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.positionService.findAllPages(page, size).getContent());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<PositionResponse> getPositionById(@RequestParam Long positionId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.positionService.findById(positionId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<PositionResponse> getPositionByIdAsync(@RequestParam Long positionId)
            throws ExecutionException, InterruptedException {
        PositionResponse positionResponse = this.positionService.findByIdAsync(positionId).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(positionResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<PositionResponse> createOrUpdatePosition(@RequestBody PositionRequest positionRequest) {
        PositionResponse positionResponse = this.positionService.createOrUpdate(positionRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/hotel-service/positions/by-id?positionId=" + positionResponse.getId()))
                .body(positionResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<PositionResponse> createOrUpdateAsync(@RequestBody PositionRequest positionRequest)
            throws ExecutionException, InterruptedException {
        PositionResponse positionResponse = this.positionService.createOrUpdateAsync(positionRequest).get();
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/hotel-service/positions/by-id/async?positionId=" + positionResponse.getId()))
                .body(positionResponse);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deletePositionById(@RequestParam Long positionId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.positionService.deleteById(positionId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deletePositionByIdAsync(@RequestParam Long positionId) throws ExecutionException, InterruptedException {
        String answer = this.positionService.deleteByIdAsync(positionId).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, TEXT_PLAIN_VALUE))
        );
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
