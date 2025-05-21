package org.burgas.excursionbackend.router;

import org.burgas.excursionbackend.dto.PaymentRequest;
import org.burgas.excursionbackend.dto.PaymentResponse;
import org.burgas.excursionbackend.filter.IdentityFilterFunction;
import org.burgas.excursionbackend.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class PaymentRouter {

    @Bean
    public RouterFunction<ServerResponse> paymentRoutes(final PaymentService paymentService) {
        return route()
                .filter(new IdentityFilterFunction())
                .GET(
                        "/excursion-service/payments/by-identity", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(paymentService.findAllByIdentityId(request.param("identityId").orElseThrow()))
                )
                .GET(
                        "/excursion-service/payments/by-identity/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(paymentService.findAllByIdentityIdAsync(request.param("identityId").orElseThrow()).get())
                )
                .GET(
                        "/excursion-service/payments/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(paymentService.findById(request.param("paymentId").orElseThrow()))
                )
                .GET(
                        "/excursion-service/payments/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(paymentService.findByIdAsync(request.param("paymentId").orElseThrow()).get())
                )
                .POST(
                        "/excursion-service/payments/make-identity-payment", request -> {
                            String identityId = request.param("identityId").orElseThrow();
                            PaymentRequest paymentRequest = request.body(PaymentRequest.class);
                            paymentRequest.setIdentityId(Long.valueOf(requireNonNull(identityId)));
                            PaymentResponse paymentResponse = paymentService.makeIdentityPayment(paymentRequest);
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(paymentResponse);
                        }
                )
                .POST(
                        "/excursion-service/payments/make-identity-payment-id", request -> {
                            String identityId = request.param("identityId").orElseThrow();
                            PaymentRequest paymentRequest = request.body(PaymentRequest.class);
                            paymentRequest.setIdentityId(Long.valueOf(requireNonNull(identityId)));
                            PaymentResponse paymentResponse = paymentService.makeIdentityPaymentAsync(paymentRequest).get();
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(paymentResponse);
                        }
                )
                .build();
    }
}
