package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.RentRoom;
import org.burgas.hotelbackend.dto.RoomRequest;
import org.burgas.hotelbackend.dto.RoomResponse;
import org.burgas.hotelbackend.exception.WrongContentTypeException;
import org.burgas.hotelbackend.service.RoomService;
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
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.burgas.hotelbackend.message.ImageMessages.WRONG_CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/hotel-service/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(value = "/by-filial")
    public @ResponseBody ResponseEntity<List<RoomResponse>> getRoomsByFilialId(@RequestParam Long filialId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.roomService.findByFilialId(filialId));
    }

    @GetMapping(value = "/by-filial/async")
    public @ResponseBody ResponseEntity<List<RoomResponse>> getRoomsByFilialIdAsync(@RequestParam Long filialId)
            throws ExecutionException, InterruptedException {
        List<RoomResponse> roomResponses = this.roomService.findByFilialIdAsync(filialId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(roomResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/by-filial/pages/{page}")
    public @ResponseBody ResponseEntity<List<RoomResponse>> getPagesByFilialId(
            @PathVariable Integer page, @RequestParam Integer size, @RequestParam Long filialId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.roomService.findPagesByFilialId(filialId, page, size).getContent());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<RoomResponse> getRoomById(@RequestParam Long roomId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.roomService.findById(roomId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<RoomResponse> getRoomByIdAsync(@RequestParam Long roomId) throws ExecutionException, InterruptedException {
        RoomResponse roomResponse = this.roomService.findByIdAsync(roomId).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(roomResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<RoomResponse> createUpdateRoom(@RequestBody RoomRequest roomRequest) {
        RoomResponse roomResponse = this.roomService.createOrUpdate(roomRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/hotel-service/rooms/by-id?roomId=" + roomResponse.getId()))
                .body(roomResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<RoomResponse> createOrUpdateRoomAsync(@RequestBody RoomRequest roomRequest)
            throws ExecutionException, InterruptedException {
        RoomResponse roomResponse = this.roomService.createOrUpdateAsync(roomRequest).get();
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/hotel-service/rooms/by-id/async?roomId=" + roomResponse.getId()))
                .body(roomResponse);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteRoomById(@RequestParam Long roomId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.roomService.deleteById(roomId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deleteRoomByIdAsync(@RequestParam Long roomId) throws ExecutionException, InterruptedException {
        String answer = this.roomService.deleteByIdAsync(roomId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }

    @PostMapping(value = "/rent-room")
    public @ResponseBody ResponseEntity<String> clientRentRoom(@RequestBody RentRoom rentRoom) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.roomService.rentClientRoom(rentRoom));
    }

    @PostMapping(value = "/upload-images")
    public @ResponseBody ResponseEntity<String> uploadRoomImages(@RequestParam Long roomId, @RequestPart List<MultipartFile> files) {
        AtomicInteger imageType = new AtomicInteger(0);
        files.forEach(
                file -> {
                    if (requireNonNull(file.getContentType()).split("/")[0].equals("image"))
                        imageType.getAndIncrement();
                }
        );
        if (files.size() == imageType.get()) {
            return ResponseEntity
                    .status(OK)
                    .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                    .body(this.roomService.uploadRoomImages(roomId, files));
        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PostMapping(value = "/upload-images/async")
    public @ResponseBody ResponseEntity<String> uploadRoomImagesAsync(@RequestParam Long roomId, @RequestParam List<MultipartFile> files)
            throws ExecutionException, InterruptedException {
        AtomicInteger imageType = new AtomicInteger(0);
        files.forEach(
                file -> {
                    if (requireNonNull(file.getContentType()).split("/")[0].equals("image"))
                        imageType.getAndIncrement();
                }
        );
        if (files.size() == imageType.get()) {
            String answer = this.roomService.uploadRoomImagesAsync(roomId, files).get();
            HttpHeaders httpHeaders = new HttpHeaders(
                    MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, TEXT_PLAIN_VALUE))
            );
            return new ResponseEntity<>(answer, httpHeaders, OK);

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }
}
