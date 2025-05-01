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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new HttpSessionSecurityContextRepository()))
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(
                        httpRequest -> httpRequest

                                .requestMatchers(
                                        "/authentication/csrf-token",

                                        "/identities/create",

                                        "/images/by-id",

                                        "/airports", "/airports/async", "/airports/pages/{page}",
                                        "/airports/by-country", "/airports/by-country/async",
                                        "/airports/by-city", "/airports/by-city/async",

                                        "/filials", "/filials/async", "/filials/pages/{page}",
                                        "/filials/by-country", "/filials/by-country/async",
                                        "/filials/by-city", "/filials/by-city/async",

                                        "/departments", "/departments/async", "/departments/pages/{page}",
                                        "/departments/by-id", "/departments/by-id/async",

                                        "/filial-departments", "/filial-departments/async", "/filial-departments/pages/{page}",
                                        "/filial-departments/by-filial-department", "/filial-departments/by-filial-department/async",

                                        "/flights", "/flights/by-departure-city-arrival-city-by-departure-date",
                                        "/flights/by-departure-city-by-arrival-city", "/flights/by-departure-city-by-arrival-city-back",
                                        "/flights/by-id",

                                        "/tickets", "/tickets/by-flight", "/tickets/by-id", "/tickets/create-update",

                                        "/flight-seats/by-flight-id",

                                        "/ordered-tickets/in-session", "/ordered-tickets/order-ticket-session"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/identities/by-id", "/identities/by-username", "/identities/update",
                                        "/identities/change-password", "/identities/set-password",
                                        "/identities/upload-image", "/identities/change-image", "/identities/delete-image",

                                        "/positions", "/positions/by-id",

                                        "/ordered-tickets/by-identity", "/ordered-tickets/order-ticket-identity"
                                )
                                .hasAnyAuthority(ADMIN, EMPLOYEE, USER)

                                .requestMatchers(
                                        "/employees", "/employees/async", "/employees/pages/{page}",

                                        "/ordered-tickets"
                                )
                                .hasAnyAuthority(ADMIN, EMPLOYEE)

                                .requestMatchers(
                                        "/employees/by-id", "/employees/by-id/async",

                                        "/flights/start-flight", "/flights/complete-flight"
                                )
                                .hasAnyAuthority(EMPLOYEE)

                                .requestMatchers(
                                        "/requires/create-update",

                                        "/require-answers/by-user",

                                        "/employees/create", "/employees/create/async"
                                )
                                .hasAnyAuthority(USER)

                                .requestMatchers(
                                        "/identities", "/identities/enable-disable",

                                        "/authorities", "/authorities/async",
                                        "/authorities/by-id", "/authorities/by-id/async",
                                        "/authorities/create-update", "/authorities/create-update/async",
                                        "/authorities/delete", "/authorities/delete/async",

                                        "/addresses", "/addresses/async", "/addresses/pages/{page}",
                                        "/addresses/create-update-secured", "/addresses/create-update-secured/async",
                                        "/addresses/delete", "/addresses/delete/async",

                                        "/airports/create-update",

                                        "/departments/create-update", "/departments/create-update/async",
                                        "/departments/delete", "/departments/delete/async",

                                        "/employees/update", "/employees/update/async",
                                        "/employees/delete", "/employees/delete/async",

                                        "/filials/create-update", "/filials/create-update/async",
                                        "/filials/delete", "/filials/delete/async",

                                        "/airports/create-update/async",

                                        "/filial-departments/create-update", "/filial-departments/create-update/async",
                                        "/filial-departments/delete", "/filial-departments/delete/async",

                                        "/positions/create-update", "/positions/delete",

                                        "/requires/by-closed",

                                        "/require-answers/by-admin", "/require-answers/send-answer-or-token",

                                        "/flights/create-update", "/flights/add-employee", "/flights/remove-employee",

                                        "/ordered-tickets/cancel-ordered-ticket"
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
