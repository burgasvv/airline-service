package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.EmployeeRequest;
import org.burgas.hotelbackend.dto.EmployeeResponse;
import org.burgas.hotelbackend.exception.WrongContentTypeException;
import org.burgas.hotelbackend.service.EmployeeService;
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
@RequestMapping(value = "/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.employeeService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<EmployeeResponse>> getAllEmployeesAsync() throws ExecutionException, InterruptedException {
        List<EmployeeResponse> employeeResponses = this.employeeService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(employeeResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<EmployeeResponse>> getAllEmployeePages(@PathVariable Integer page, @RequestParam Integer size) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.employeeService.findAllPages(page, size).getContent());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<EmployeeResponse> getEmployeeById(@RequestParam Long employeeId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.employeeService.findById(employeeId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<EmployeeResponse> getEmployeeByIdAsync(@RequestParam Long employeeId) throws ExecutionException, InterruptedException {
        EmployeeResponse employeeResponse = this.employeeService.findByIdAsync(employeeId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(employeeResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<EmployeeResponse> createOrUpdateEmployee(@RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse employeeResponse = this.employeeService.createOrUpdate(employeeRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/employees/by-id?employeeId=" + employeeResponse.getId()))
                .body(employeeResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<EmployeeResponse> createOrUpdateEmployeeAsync(@RequestBody EmployeeRequest employeeRequest) throws ExecutionException, InterruptedException {
        EmployeeResponse employeeResponse = this.employeeService.createOrUpdateAsync(employeeRequest).get();
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/employees/by-id/async?employeeId=" + employeeResponse.getId()))
                .body(employeeResponse);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteEmployeeById(@RequestParam Long employeeId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.employeeService.deleteById(employeeId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deleteEmployeeByIdAsync(@RequestParam Long employeeId) throws ExecutionException, InterruptedException {
        String answer = this.employeeService.deleteByIdAsync(employeeId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }

    @PostMapping(value = "/upload-image")
    public @ResponseBody ResponseEntity<String> uploadEmployeeImage(@RequestParam Long employeeId, @RequestPart MultipartFile file) {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(OK)
                    .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                    .body(this.employeeService.uploadEmployeeImage(employeeId, file));
        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PostMapping(value = "/upload-image/async")
    public @ResponseBody ResponseEntity<String> uploadEmployeeImageAsync(@RequestParam Long employeeId, @RequestPart MultipartFile file)
            throws ExecutionException, InterruptedException {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            String answer = this.employeeService.uploadEmployeeImageAsync(employeeId, file).get();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
            return new ResponseEntity<>(answer, httpHeaders, OK);

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PutMapping(value = "/change-image")
    public @ResponseBody ResponseEntity<String> changeEmployeeImage(@RequestParam Long employeeId, @RequestPart MultipartFile file) {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(OK)
                    .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                    .body(this.employeeService.changeEmployeeImage(employeeId, file));
        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @PutMapping(value = "/change-image/async")
    public @ResponseBody ResponseEntity<String> changeEmployeeImageAsync(@RequestParam Long employeeId, @RequestPart MultipartFile file)
            throws ExecutionException, InterruptedException {
        if (requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            String answer = this.employeeService.changeEmployeeImageAsync(employeeId, file).get();
            HttpHeaders httpHeaders = new HttpHeaders(
                    MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, TEXT_PLAIN_VALUE))
            );
            return new ResponseEntity<>(answer, httpHeaders, OK);

        } else {
            throw new WrongContentTypeException(WRONG_CONTENT_TYPE.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image")
    public @ResponseBody ResponseEntity<String> deleteEmployeeImage(@RequestParam Long employeeId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.employeeService.deleteEmployeeImage(employeeId));
    }

    @DeleteMapping(value = "/delete-image/async")
    public @ResponseBody ResponseEntity<String> deleteEmployeeImageAsync(@RequestParam Long employeeId) throws ExecutionException, InterruptedException {
        String answer = this.employeeService.deleteEmployeeImageAsync(employeeId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}
