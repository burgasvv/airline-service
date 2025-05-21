package org.burgas.hotelbackend.controller;

import org.burgas.hotelbackend.dto.PaymentResponse;
import org.burgas.hotelbackend.service.PaymentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/hotel-service/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping(value = "/by-client")
    public @ResponseBody ResponseEntity<List<PaymentResponse>> getPaymentsByClient(@RequestParam Long clientId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.paymentService.findByClientId(clientId));
    }

    @GetMapping(value = "/by-client/async")
    public @ResponseBody ResponseEntity<List<PaymentResponse>> getPaymentsByClientAsync(@RequestParam Long clientId)
            throws ExecutionException, InterruptedException {
        List<PaymentResponse> paymentResponses = this.paymentService.findByClientIdAsync(clientId).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(paymentResponses, httpHeaders, OK);
    }

    @PutMapping(value = "/close-payment")
    public @ResponseBody ResponseEntity<String> closeClientPayment(@RequestParam Long clientId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.paymentService.closePayment(clientId));
    }

    @PutMapping(value = "/cancel-payment")
    public @ResponseBody ResponseEntity<String> cancelClientPayment(@RequestParam Long paymentId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.paymentService.cancelRentAndPayment(paymentId));
    }

    @PutMapping(value = "/early-payment")
    public @ResponseBody ResponseEntity<String> earlyPaymentAndReturnReservation(@RequestParam Long paymentId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.paymentService.earlyPaymentReservationReturn(paymentId));
    }
}
