package org.burgas.excursionservice.router;

import org.burgas.excursionservice.dto.PaymentRequest;
import org.burgas.excursionservice.dto.PaymentResponse;
import org.burgas.excursionservice.filter.IdentityFilterFunction;
import org.burgas.excursionservice.service.PaymentService;
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
                        "/payments/by-identity", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(paymentService.findAllByIdentityId(request.param("identityId").orElseThrow()))
                )
                .GET(
                        "/payments/by-identity/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(paymentService.findAllByIdentityIdAsync(request.param("identityId").orElseThrow()).get())
                )
                .GET(
                        "/payments/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(paymentService.findById(request.param("paymentId").orElseThrow()))
                )
                .GET(
                        "/payments/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(paymentService.findByIdAsync(request.param("paymentId").orElseThrow()).get())
                )
                .POST(
                        "/payments/make-identity-payment", request -> {
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
                        "/payments/make-identity-payment-id", request -> {
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
