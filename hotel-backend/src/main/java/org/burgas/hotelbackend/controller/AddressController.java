package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.AddressRequest;
import org.burgas.hotelbackend.dto.AddressResponse;
import org.burgas.hotelbackend.service.AddressService;
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
@RequestMapping(value = "/hotel-service/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<AddressResponse>> getAllAddresses() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.addressService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<AddressResponse>> getAllAddressesAsync() throws ExecutionException, InterruptedException {
        List<AddressResponse> addressResponses = this.addressService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(addressResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<AddressResponse>> getAllAddressPages(
            @PathVariable final Integer page, @RequestParam final Integer size
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.addressService.findAllPages(page, size).getContent());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<AddressResponse> getAddressById(@RequestParam final Long addressId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.addressService.findById(addressId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<AddressResponse> getAddressByIdAsync(@RequestParam final Long addressId)
            throws ExecutionException, InterruptedException {
        AddressResponse addressResponse = this.addressService.findByIdAsync(addressId).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(addressResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<AddressResponse> createOrUpdateAddress(@RequestBody final AddressRequest addressRequest) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.addressService.createOrUpdate(addressRequest));
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<AddressResponse> createOrUpdateAddressAsync(@RequestBody final AddressRequest addressRequest)
            throws ExecutionException, InterruptedException {
        AddressResponse addressResponse = this.addressService.createOrUpdateAsync(addressRequest).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(addressResponse, httpHeaders, OK);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteAddress(@RequestParam final Long addressId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.addressService.deleteById(addressId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deleteAddressAsync(@RequestParam final Long addressId) throws ExecutionException, InterruptedException {
        String answer = this.addressService.deleteByIdAsync(addressId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
