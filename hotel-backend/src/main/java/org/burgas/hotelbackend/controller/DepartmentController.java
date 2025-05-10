package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.DepartmentRequest;
import org.burgas.hotelbackend.dto.DepartmentResponse;
import org.burgas.hotelbackend.service.DepartmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.departmentService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<DepartmentResponse>> getAllDepartmentsAsync() throws ExecutionException, InterruptedException {
        List<DepartmentResponse> departmentResponses = this.departmentService.findAllAsync().get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(departmentResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/pages/{page}")
    public @ResponseBody ResponseEntity<List<DepartmentResponse>> getAllDepartmentPages(@PathVariable Integer page, @RequestParam Integer size) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.departmentService.findAllPages(page, size).getContent());
    }

    @GetMapping(value = "/by-filial")
    public @ResponseBody ResponseEntity<List<DepartmentResponse>> getDepartmentsByFilial(@RequestParam Long filialId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.departmentService.findByFilialId(filialId));
    }

    @GetMapping(value = "/by-filial/async")
    public @ResponseBody ResponseEntity<List<DepartmentResponse>> getDepartmentsByFilialAsync(@RequestParam Long filialId)
            throws ExecutionException, InterruptedException {
        List<DepartmentResponse> departmentResponses = this.departmentService.findByFilialIdAsync(filialId).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(departmentResponses, httpHeaders, OK);
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<DepartmentResponse> getDepartmentById(@RequestParam Long departmentId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.departmentService.findById(departmentId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<DepartmentResponse> getDepartmentByIdAsync(@RequestParam Long departmentId)
            throws ExecutionException, InterruptedException {
        DepartmentResponse departmentResponse = this.departmentService.findByIdAsync(departmentId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(departmentResponse, httpHeaders, OK);
    }

    @GetMapping(value = "/by-name")
    public @ResponseBody ResponseEntity<DepartmentResponse> getDepartmentByName(@RequestParam String name) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.departmentService.findByName(name));
    }

    @GetMapping(value = "/by-name/async")
    public @ResponseBody ResponseEntity<DepartmentResponse> getDepartmentByNameAsync(@RequestParam String name)
            throws ExecutionException, InterruptedException {
        DepartmentResponse departmentResponse = this.departmentService.findByNameAsync(name).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(departmentResponse, httpHeaders, OK);
    }

    @PostMapping(value = "/create-update")
    public @ResponseBody ResponseEntity<DepartmentResponse> createOrUpdateDepartment(@RequestBody DepartmentRequest departmentRequest) {
        DepartmentResponse departmentResponse = this.departmentService.createOrUpdate(departmentRequest);
        return ResponseEntity
                .status(FOUND)
                .contentType(APPLICATION_JSON)
                .location(URI.create("/departments/by-id?departmentId=" + departmentResponse.getId()))
                .body(departmentResponse);
    }

    @PostMapping(value = "/create-update/async")
    public @ResponseBody ResponseEntity<DepartmentResponse> createOrUpdateDepartmentAsync(@RequestBody DepartmentRequest departmentRequest)
            throws ExecutionException, InterruptedException {
        DepartmentResponse departmentResponse = this.departmentService.createOrUpdateAsync(departmentRequest).get();
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(departmentResponse, httpHeaders, OK);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteDepartmentById(@RequestParam Long departmentId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.departmentService.deleteById(departmentId));
    }

    @DeleteMapping(value = "/delete/async")
    public @ResponseBody ResponseEntity<String> deleteDepartmentByIdAsync(@RequestParam Long departmentId)
            throws ExecutionException, InterruptedException {
        String answer = this.departmentService.deleteByIdAsync(departmentId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(answer, httpHeaders, OK);
    }
}