package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.CountryRequest;
import org.burgas.hotelbackend.dto.CountryResponse;
import org.burgas.hotelbackend.service.CountryService;
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
@RequestMapping(value = "/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<CountryResponse>> getAllCountries() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.countryService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<CountryResponse>> getAllCountriesAsync() throws ExecutionException, InterruptedException {
        List<CountryResponse> countryResponses = this.countryService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(countryResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<CountryResponse>> getAllCountryPages(@PathVariable Integer page, @RequestParam Integer size) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.countryService.findAll(page, size).getContent());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<CountryResponse> getCountryById(@RequestParam Long countryId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.countryService.findById(countryId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<CountryResponse> getCountryByIdAsync(@RequestParam Long countryId) throws ExecutionException, InterruptedException {
        CountryResponse countryResponse = this.countryService.findByIdAsync(countryId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(countryResponse, httpHeaders, OK);
    }

    @GetMapping(value = "/by-name")
    public @ResponseBody ResponseEntity<CountryResponse> getCountryByName(@RequestParam String name) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.countryService.findByName(name));
    }

    @GetMapping(value = "/by-name/async")
    public @ResponseBody ResponseEntity<CountryResponse> getCountryByNameAsync(@RequestParam String name) throws ExecutionException, InterruptedException {
        CountryResponse countryResponse = this.countryService.findByNameAsync(name).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(countryResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<CountryResponse> createOrUpdate(@RequestBody CountryRequest countryRequest) {
        CountryResponse countryResponse = this.countryService.createOrUpdate(countryRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/countries/by-id?countryId=" + countryResponse.getId()))
                .body(countryResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<CountryResponse> createOrUpdateAsync(@RequestBody CountryRequest countryRequest)
            throws ExecutionException, InterruptedException {
        CountryResponse countryResponse = this.countryService.createOrUpdateAsync(countryRequest).get();
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/countries/by-id/async?countryId=" + countryResponse.getId()))
                .body(countryResponse);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteCountry(@RequestParam Long countryId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.countryService.deleteById(countryId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deleteCountryAsync(@RequestParam Long countryId) throws ExecutionException, InterruptedException {
        String answer = this.countryService.deleteByIdAsync(countryId).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, TEXT_PLAIN_VALUE))
        );
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
