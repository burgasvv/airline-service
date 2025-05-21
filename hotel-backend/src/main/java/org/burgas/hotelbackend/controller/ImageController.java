package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.entity.Image;
import org.burgas.hotelbackend.service.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.parseMediaType;

@Controller
@RequestMapping(value = "/hotel-service/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<Resource> getImageById(@RequestParam Long imageId) {
        Image image = this.imageService.findById(imageId);
        return ResponseEntity
                .status(OK)
                .contentType(parseMediaType(image.getContentType()))
                .body(
                        new InputStreamResource(new ByteArrayInputStream(image.getData()))
                );
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<Resource> getImageByIdAsync(@RequestParam Long imageId) throws ExecutionException, InterruptedException {
        Image image = this.imageService.findByIdAsync(imageId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", parseMediaType(image.getContentType()).getType());
        return new ResponseEntity<>(
                new InputStreamResource(new ByteArrayInputStream(image.getData())), httpHeaders, OK
        );
    }
}
