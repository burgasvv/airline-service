package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.HotelRequest;
import org.burgas.hotelbackend.dto.HotelResponse;
import org.burgas.hotelbackend.exception.WrongContentTypeException;
import org.burgas.hotelbackend.service.HotelService;
import org.springframework.data.domain.Page;
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
@RequestMapping(value = "/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.hotelService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<HotelResponse>> getAllHotelsAsync() throws ExecutionException, InterruptedException {
        List<HotelResponse> hotelResponses = this.hotelService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(hotelResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<HotelResponse>> getAllHotelPages(@PathVariable Integer page, @RequestParam Integer size) {
        Page<HotelResponse> allPages = this.hotelService.findAllPages(page, size);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(allPages.getContent(), httpHeaders, OK);
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<HotelResponse> getHotelById(@RequestParam Long hotelId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.hotelService.findById(hotelId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<HotelResponse> getHotelByIdAsync(@RequestParam Long hotelId) throws ExecutionException, InterruptedException {
        HotelResponse hotelResponse = this.hotelService.findByIdAsync(hotelId).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(hotelResponse, httpHeaders, OK);
    }

    @GetMapping(value = "/by-name")
    public @ResponseBody ResponseEntity<HotelResponse> getHotelByName(@RequestParam String name) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.hotelService.findByName(name));
    }

    @GetMapping(value = "/by-name/async")
    public @ResponseBody ResponseEntity<HotelResponse> getHotelByNameAsync(@RequestParam String name) throws ExecutionException, InterruptedException {
        HotelResponse hotelResponse = this.hotelService.findByNameAsync(name).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(hotelResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<HotelResponse> createOrUpdateHotel(@RequestBody HotelRequest hotelRequest) {
        HotelResponse hotelResponse = this.hotelService.createOrUpdate(hotelRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/hotels/by-id?hotelId=" + hotelResponse.getId()))
                .body(hotelResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<HotelResponse> createOrUpdateHotelAsync(@RequestBody HotelRequest hotelRequest)
            throws ExecutionException, InterruptedException {
        HotelResponse hotelResponse = this.hotelService.createOrUpdateAsync(hotelRequest).get();
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/hotels/by-id/async?hotelId=" + hotelResponse.getId()))
                .body(hotelResponse);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteHotelById(@RequestParam Long hotelId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.hotelService.deleteById(hotelId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deleteHotelByIdAsync(@RequestParam Long hotelId) throws ExecutionException, InterruptedException {
        String answer = this.hotelService.deleteByIdAsync(hotelId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }

    @PostMapping(value = "/upload-image")
    public @ResponseBody ResponseEntity<String> uploadHotelImage(@RequestParam Long hotelId, @RequestPart MultipartFile file) {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(OK)
                    .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                    .body(this.hotelService.uploadHotelImage(hotelId, file));

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PostMapping(value = "/upload-image/async")
    public @ResponseBody ResponseEntity<String> uploadHotelImageAsync(@RequestParam Long hotelId, @RequestPart MultipartFile file)
            throws ExecutionException, InterruptedException {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            String answer = this.hotelService.uploadHotelImageAsync(hotelId, file).get();
            HttpHeaders httpHeaders = new HttpHeaders(
                    MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, TEXT_PLAIN_VALUE))
            );
            return new ResponseEntity<>(answer, httpHeaders, OK);

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PutMapping(value = "/change-image")
    public @ResponseBody ResponseEntity<String> changeHotelImage(@RequestParam Long hotelId, @RequestPart MultipartFile file) {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(OK)
                    .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                    .body(this.hotelService.changeHotelImage(hotelId, file));

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PutMapping(value = "/change-image/async")
    public @ResponseBody ResponseEntity<String> changeHotelImageAsync(@RequestParam Long hotelId, @RequestPart MultipartFile file)
            throws ExecutionException, InterruptedException {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            String answer = this.hotelService.changeHotelImageAsync(hotelId, file).get();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
            return new ResponseEntity<>(answer, httpHeaders, OK);

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image")
    public @ResponseBody ResponseEntity<String> deleteHotelImage(@RequestParam Long hotelId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.hotelService.deleteHotelImage(hotelId));
    }

    @DeleteMapping(value = "/delete-image/async")
    public @ResponseBody ResponseEntity<String> deleteHotelImageAsync(@RequestParam Long hotelId) throws ExecutionException, InterruptedException {
        String answer = this.hotelService.deleteHotelImageAsync(hotelId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
