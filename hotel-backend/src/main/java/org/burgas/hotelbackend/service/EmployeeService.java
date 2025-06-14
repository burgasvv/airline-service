package org.burgas.hotelbackend.service;

import org.burgas.hotelbackend.dto.EmployeeRequest;
import org.burgas.hotelbackend.dto.EmployeeResponse;
import org.burgas.hotelbackend.entity.Employee;
import org.burgas.hotelbackend.entity.Image;
import org.burgas.hotelbackend.exception.EmployeeNotCreatedOrUpdatedException;
import org.burgas.hotelbackend.exception.EmployeeNotFoundException;
import org.burgas.hotelbackend.exception.ImageNotFoundException;
import org.burgas.hotelbackend.mapper.EmployeeMapper;
import org.burgas.hotelbackend.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.hotelbackend.log.EmployeeLogs.*;
import static org.burgas.hotelbackend.message.EmployeeMessages.*;
import static org.burgas.hotelbackend.message.ImageMessages.IMAGE_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.*;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final ImageService imageService;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, ImageService imageService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.imageService = imageService;
    }

    public List<EmployeeResponse> findAll() {
        return this.employeeRepository.findAll()
                .stream()
                .peek(employee -> log.info(EMPLOYEE_FOUND_ALL.getLogMessage(), employee))
                .map(this.employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<EmployeeResponse>> findAllAsync() {
        return supplyAsync(this.employeeRepository::findAll)
                .thenApplyAsync(
                        employees -> employees.stream()
                                .peek(employee -> log.info(EMPLOYEE_FOUND_ALL_ASYNC.getLogMessage(), employee))
                                .map(this.employeeMapper::toResponse)
                                .collect(Collectors.toList())
                );
    }

    public Page<EmployeeResponse> findAllPages(final Integer page, final Integer size) {
        return this.employeeRepository.findAll(
                PageRequest.of(page - 1, size)
                        .withSort(Sort.Direction.ASC, "name", "surname", "patronymic")
        )
                .map(this.employeeMapper::toResponse);
    }

    public EmployeeResponse findById(final Long employeeId) {
        return this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .stream()
                .peek(employee -> log.info(EMPLOYEE_FOUND_BY_ID.getLogMessage(), employee))
                .map(this.employeeMapper::toResponse)
                .findFirst()
                .orElseGet(EmployeeResponse::new);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<EmployeeResponse> findByIdAsync(final Long employeeId) {
        return supplyAsync(() -> this.employeeRepository.findById(employeeId == null ? 0L : employeeId))
                .thenApplyAsync(
                        employee -> employee.stream()
                                .peek(foundEmployee -> log.info(EMPLOYEE_FOUND_BY_ID_ASYNC.getLogMessage(), foundEmployee))
                                .map(this.employeeMapper::toResponse)
                                .findFirst()
                                .orElseGet(EmployeeResponse::new)
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public EmployeeResponse createOrUpdate(final EmployeeRequest employeeRequest) {
        return of(this.employeeMapper.toEntity(employeeRequest))
                .map(this.employeeRepository::save)
                .stream()
                .peek(employee -> log.info(EMPLOYEE_CREATED_OR_UPDATED.getLogMessage(), employee))
                .map(this.employeeMapper::toResponse)
                .findFirst()
                .orElseThrow(() -> new EmployeeNotCreatedOrUpdatedException(EMPLOYEE_NOT_CREATED_OR_UPDATED.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<EmployeeResponse> createOrUpdateAsync(final EmployeeRequest employeeRequest) {
        return supplyAsync(() -> this.employeeMapper.toEntity(employeeRequest))
                .thenApplyAsync(this.employeeRepository::save)
                .thenApplyAsync(
                        employee -> of(employee).stream()
                                .peek(createdOrUpdatedEmployee -> log.info(EMPLOYEE_CREATED_OR_UPDATED_ASYNC.getLogMessage(), createdOrUpdatedEmployee))
                                .map(this.employeeMapper::toResponse)
                                .findFirst()
                                .orElseThrow(() -> new EmployeeNotCreatedOrUpdatedException(EMPLOYEE_NOT_CREATED_OR_UPDATED.getMessage()))
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final Long employeeId) {
        return this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .stream()
                .peek(employee -> log.info(EMPLOYEE_FOUND_BEFORE_DELETE.getLogMessage(), employee))
                .map(
                        employee -> {
                            this.employeeRepository.deleteById(employee.getId());
                            return EMPLOYEE_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(() -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage()));
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteByIdAsync(final Long employeeId) {
        return supplyAsync(() -> this.employeeRepository.findById(employeeId == null ? 0L : employeeId))
                .thenApplyAsync(
                        employee -> employee.stream()
                                .peek(foundEmployee -> log.info(EMPLOYEE_FOUND_BEFORE_DELETE_ASYNC.getLogMessage(), foundEmployee))
                                .map(
                                        foundEmployee -> {
                                            this.employeeRepository.deleteById(foundEmployee.getId());
                                            return EMPLOYEE_DELETED.getMessage();
                                        }
                                )
                                .findFirst()
                                .orElseThrow(() -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage()))
                );
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadEmployeeImage(final Long employeeId, final MultipartFile multipartFile) {
        return this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .stream()
                .peek(employee -> log.info(EMPLOYEE_FOUND_BEFORE_UPLOAD_IMAGE.getLogMessage(), employee))
                .map(
                        employee -> {
                            Image image = this.imageService.uploadImage(multipartFile);
                            employee.setImageId(image.getId());
                            Employee saved = this.employeeRepository.save(employee);
                            return format(EMPLOYEE_IMAGE_UPLOADED.getMessage(), saved.getImageId());
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> uploadEmployeeImageAsync(final Long employeeId, final MultipartFile multipartFile) {
        return supplyAsync(() -> this.employeeRepository.findById(employeeId == null ? 0L : employeeId))
                .thenApplyAsync(
                        employee -> employee.stream()
                                .peek(foundEmployee -> log.info(EMPLOYEE_FOUND_BEFORE_UPLOAD_IMAGE_ASYNC.getLogMessage(), foundEmployee))
                                .map(
                                        foundEmployee -> {
                                            try {
                                                Image image = this.imageService.uploadImageAsync(multipartFile).get();
                                                foundEmployee.setImageId(image.getId());
                                                Employee saved = this.employeeRepository.save(foundEmployee);
                                                return format(EMPLOYEE_IMAGE_UPLOADED.getMessage(), saved.getImageId());

                                            } catch (InterruptedException | ExecutionException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeEmployeeImage(final Long employeeId, final MultipartFile multipartFile) {
        return this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .stream()
                .peek(employee -> log.info(EMPLOYEE_FOUND_BEFORE_CHANGE_IMAGE.getLogMessage(), employee))
                .map(
                        employee -> ofNullable(employee.getImageId())
                                .map(
                                        imageId -> {
                                            Image image = this.imageService.changeImage(imageId, multipartFile);
                                            return format(EMPLOYEE_IMAGE_CHANGED.getMessage(), image.getId());
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .findFirst()
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> changeEmployeeImageAsync(final Long employeeId, final MultipartFile multipartFile) {
        return supplyAsync(() -> this.employeeRepository.findById(employeeId == null ? 0L : employeeId))
                .thenApplyAsync(
                        employee -> employee.stream()
                                .peek(foundEmployee -> log.info(EMPLOYEE_FOUND_BEFORE_CHANGE_IMAGE_ASYNC.getLogMessage(), foundEmployee))
                                .map(
                                        foundEmployee -> ofNullable(foundEmployee.getImageId())
                                                .map(
                                                        imageId -> {
                                                            try {
                                                                Image image = this.imageService.changeImageAsync(imageId, multipartFile).get();
                                                                return format(EMPLOYEE_IMAGE_CHANGED.getMessage(), image.getId());

                                                            } catch (InterruptedException | ExecutionException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                )
                                                .orElseThrow(
                                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                                )
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                                )
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteEmployeeImage(final Long employeeId) {
        return this.employeeRepository.findById(employeeId == null ? 0L : employeeId)
                .stream()
                .peek(employee -> log.info(EMPLOYEE_FOUND_BEFORE_DELETE.getLogMessage(), employee))
                .map(
                        employee -> ofNullable(employee.getImageId())
                                .map(
                                        imageId -> {
                                            this.imageService.deleteImage(imageId);
                                            return format(EMPLOYEE_IMAGE_DELETED.getMessage(), imageId);
                                        }
                                )
                                .orElseThrow(
                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                )
                )
                .findFirst()
                .orElseThrow(
                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                );
    }

    @Async(value = "taskExecutor")
    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<String> deleteEmployeeImageAsync(final Long employeeId) {
        return supplyAsync(() -> this.employeeRepository.findById(employeeId == null ? 0L : employeeId))
                .thenApplyAsync(
                        employee -> employee.stream()
                                .peek(foundEmployee -> log.info(EMPLOYEE_FOUND_BEFORE_DELETE_IMAGE_ASYNC.getLogMessage(), foundEmployee))
                                .map(
                                        foundEmployee -> ofNullable(foundEmployee.getImageId())
                                                .map(
                                                        imageId -> {
                                                            try {
                                                                this.imageService.deleteImageAsync(imageId).get();
                                                                return format(EMPLOYEE_IMAGE_DELETED.getMessage());

                                                            } catch (InterruptedException | ExecutionException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                )
                                                .orElseThrow(
                                                        () -> new ImageNotFoundException(IMAGE_NOT_FOUND.getMessage())
                                                )
                                )
                                .findFirst()
                                .orElseThrow(
                                        () -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage())
                                )
                );
    }
}
