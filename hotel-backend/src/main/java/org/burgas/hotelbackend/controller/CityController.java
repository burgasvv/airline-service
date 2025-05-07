package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.CityRequest;
import org.burgas.hotelbackend.dto.CityResponse;
import org.burgas.hotelbackend.service.CityService;
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
@RequestMapping(value = "/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<CityResponse>> getAllCities() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cityService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<CityResponse>> getAllCitiesAsync() throws ExecutionException, InterruptedException {
        List<CityResponse> cityResponses = this.cityService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(cityResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<CityResponse>> getAllCityPages(@PathVariable Integer page, @RequestParam Integer size) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cityService.findAllPages(page, size).getContent());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<CityResponse> getCityById(@RequestParam Long cityId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cityService.findById(cityId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<CityResponse> getCityByIdAsync(final @RequestParam Long cityId) throws ExecutionException, InterruptedException {
        CityResponse cityResponse = this.cityService.findByIdAsync(cityId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(cityResponse, httpHeaders, OK);
    }

    @GetMapping(value = "/by-name")
    public @ResponseBody ResponseEntity<CityResponse> getCityByName(@RequestParam String name) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cityService.findByName(name));
    }

    @GetMapping(value = "/by-name/async")
    public @ResponseBody ResponseEntity<CityResponse> getCityByNameAsync(@RequestParam String name) throws ExecutionException, InterruptedException {
        CityResponse cityResponse = this.cityService.findByNameAsync(name).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(cityResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<CityResponse> createOrUpdateCity(@RequestBody CityRequest cityRequest) {
        CityResponse cityResponse = this.cityService.createOrUpdate(cityRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/cities/by-id?cityId=" + cityResponse.getId()))
                .body(cityResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<CityResponse> createOrUpdateCityAsync(@RequestBody CityRequest cityRequest)
            throws ExecutionException, InterruptedException {
        CityResponse cityResponse = this.cityService.createOrUpdateAsync(cityRequest).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(cityResponse, httpHeaders, OK);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteCity(@RequestParam Long cityId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.cityService.deleteById(cityId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deleteCityAsync(@RequestParam Long cityId) throws ExecutionException, InterruptedException {
        String answer = this.cityService.deleteByIdAsync(cityId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
