package org.burgas.flightbackend.config;

import org.burgas.flightbackend.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";
    private static final String EMPLOYEE = "EMPLOYEE";
    private static final String USER = "USER";

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()))
                .cors(cors -> cors.configurationSource(new UrlBasedCorsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(
                        httpRequest -> httpRequest

                                .requestMatchers(
                                        "/flight-service/authentication/csrf-token",

                                        "/flight-service/identities/create",

                                        "/flight-service/images/by-id",

                                        "/flight-service/airports", "/flight-service/airports/async", "/flight-service/airports/pages/{page}",
                                        "/flight-service/airports/by-country", "/flight-service/airports/by-country/async",
                                        "/flight-service/airports/by-city", "/flight-service/airports/by-city/async",

                                        "/flight-service/filials", "/flight-service/filials/async", "/flight-service/filials/pages/{page}",
                                        "/flight-service/filials/by-country", "/flight-service/filials/by-country/async",
                                        "/flight-service/filials/by-city", "/flight-service/filials/by-city/async",

                                        "/flight-service/departments", "/flight-service/departments/async", "/flight-service/departments/pages/{page}",
                                        "/flight-service/departments/by-id", "/flight-service/departments/by-id/async",

                                        "/flight-service/filial-departments", "/flight-service/filial-departments/async", "/flight-service/filial-departments/pages/{page}",
                                        "/flight-service/filial-departments/by-filial-department", "/flight-service/filial-departments/by-filial-department/async",

                                        "/flight-service/planes", "/flight-service/planes/async", "/flight-service/planes/pages/{page}",
                                        "/flight-service/planes/by-free", "//flight-serviceplanes/by-free/async",
                                        "/flight-service/planes/by-id","/flight-service/planes/by-id/async",

                                        "/flight-service/flights", "/flight-service/flights/by-departure-city-arrival-city-by-departure-date",
                                        "/flight-service/flights/by-departure-city-by-arrival-city", "/flight-service/flights/by-departure-city-by-arrival-city-back",
                                        "/flight-service/flights/by-id",

                                        "/flight-service/tickets", "/flight-service/tickets/by-flight",
                                        "/flight-service/tickets/by-id", "/flight-service/tickets/create-update",

                                        "/flight-service/flight-seats/by-flight-id",

                                        "/flight-service/ordered-tickets/in-session", "/flight-service/ordered-tickets/order-ticket-session"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/flight-service/identities/by-id", "/flight-service/identities/by-username", "/flight-service/identities/update",
                                        "/flight-service/identities/change-password", "/flight-service/identities/set-password",
                                        "/flight-service/identities/upload-image", "/flight-service/identities/change-image", "/flight-service/identities/delete-image",

                                        "/flight-service/positions", "/flight-service/positions/async", "/flight-service/positions/pages/{page}",
                                        "/flight-service/positions/by-id", "/flight-service/positions/by-id/async",

                                        "/flight-service/ordered-tickets/by-identity", "/flight-service/ordered-tickets/order-ticket-identity"
                                )
                                .hasAnyAuthority(ADMIN, EMPLOYEE, USER)

                                .requestMatchers(
                                        "/flight-service/employees", "/flight-service/employees/async", "/flight-service/employees/pages/{page}",

                                        "/flight-service/ordered-tickets"
                                )
                                .hasAnyAuthority(ADMIN, EMPLOYEE)

                                .requestMatchers(
                                        "/flight-service/requires/by-user", "/flight-service/requires/delete/by-user",

                                        "/flight-service/employees/by-id", "/flight-service/employees/by-id/async",

                                        "/flight-service/flights/start-flight", "/flight-service/flights/complete-flight"
                                )
                                .hasAnyAuthority(EMPLOYEE)

                                .requestMatchers(
                                        "/flight-service/requires/by-user",
                                        "/flight-service/requires/create-update", "/flight-service/requires/delete/by-user",

                                        "/flight-service/require-answers/by-user",

                                        "/flight-service/employees/create", "/flight-service/employees/create/async"
                                )
                                .hasAnyAuthority(USER)

                                .requestMatchers(
                                        "/flight-service/identities", "/flight-service/identities/enable-disable",

                                        "/flight-service/authorities", "/flight-service/authorities/async",
                                        "/flight-service/authorities/by-id", "/flight-service/authorities/by-id/async",
                                        "/flight-service/authorities/create-update", "/flight-service/authorities/create-update/async",
                                        "/flight-service/authorities/delete", "/flight-service/authorities/delete/async",

                                        "/flight-service/addresses", "/flight-service/addresses/async", "/flight-service/addresses/pages/{page}",
                                        "/flight-service/addresses/create-update-secured", "/flight-service/addresses/create-update-secured/async",
                                        "/flight-service/addresses/delete", "/flight-service/addresses/delete/async",

                                        "/flight-service/airports/create-update",

                                        "/flight-service/departments/create-update", "/flight-service/departments/create-update/async",
                                        "/flight-service/departments/delete", "/flight-service/departments/delete/async",

                                        "/flight-service/employees/update", "/flight-service/employees/update/async",
                                        "/flight-service/employees/delete", "/flight-service/employees/delete/async",

                                        "/flight-service/filials/create-update", "/flight-service/filials/create-update/async",
                                        "/flight-service/filials/delete", "/flight-service/filials/delete/async",

                                        "/flight-service/airports/create-update/async",

                                        "/flight-service/filial-departments/create-update", "/flight-service/filial-departments/create-update/async",
                                        "/flight-service/filial-departments/delete", "/flight-service/filial-departments/delete/async",

                                        "/flight-service/positions/create-update", "/flight-service/positions/create-update/async",
                                        "/flight-service/positions/delete", "/flight-service/positions/delete/async",

                                        "/flight-service/planes/create-update", "/flight-service/planes/create-update/async",
                                        "/flight-service/planes/delete", "/flight-service/planes/delete/async",

                                        "/flight-service/requires/by-closed", "/flight-service/requires/by-admin",
                                        "/flight-service/requires/delete/by-admin",

                                        "/flight-service/require-answers/by-admin", "/flight-service/require-answers/send-answer-or-token",

                                        "/flight-service/flights/create-update", "/flight-service/flights/add-employee",
                                        "/flight-service/flights/remove-employee",

                                        "/flight-service/ordered-tickets/cancel-ordered-ticket"
                                )
                                .hasAnyAuthority(ADMIN)
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }
}
