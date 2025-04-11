package org.burgas.ticketservice.config;

import org.burgas.ticketservice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(withDefaults())
                .authenticationManager(reactiveAuthenticationManager())
                .httpBasic(httpBasicSpec -> httpBasicSpec.authenticationManager(reactiveAuthenticationManager()))
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec

                                .pathMatchers(
                                        "/identities/create", "/images/by-id",
                                        "/airports", "/airports/by-country", "/airports/by-city",
                                        "/filials", "/filials/by-country", "/filials/by-city",
                                        "/departments", "/departments/by-id",
                                        "/filial-departments", "/filial-departments/by-filial-department"
                                )
                                .permitAll()

                                .pathMatchers(
                                        "/identities/by-id", "/identities/by-username", "/identities/update",
                                        "/identities/change-password", "/identities/set-password",
                                        "/identities/upload-image", "/identities/change-image", "/identities/delete-image",
                                        "/positions", "/positions/by-id"
                                )
                                .hasAnyAuthority("ADMIN", "EMPLOYEE", "USER")

                                .pathMatchers(
                                        "/departments", "/departments/by-id", "/employees"
                                )
                                .hasAnyAuthority("ADMIN", "EMPLOYEE")

                                .pathMatchers(
                                        "/employees/by-id"
                                )
                                .hasAnyAuthority("EMPLOYEE")

                                .pathMatchers(
                                        "/requires/create-update", "/require-answers/by-user", "/employees/create"
                                )
                                .hasAnyAuthority("USER")

                                .pathMatchers(
                                        "/images/upload",
                                        "/identities", "/identities/enable-disable",
                                        "/authorities", "/authorities/by-id", "/authorities/create-update", "/authorities/delete",
                                        "/addresses", "/addresses/create-update-secured",
                                        "/airports/create-update", "/filials/create-update",
                                        "/departments/create-update", "/departments/delete",
                                        "/filial-departments/create-update", "/filial-departments/delete",
                                        "/positions/create-update", "/positions/delete",
                                        "/requires/by-closed",
                                        "/require-answers/by-admin", "/require-answers/send-answer-or-token"
                                )
                                .hasAnyAuthority("ADMIN")
                )
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager reactiveAuthenticationManager = new
                UserDetailsRepositoryReactiveAuthenticationManager(this.customUserDetailsService);
        reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);
        return reactiveAuthenticationManager;
    }
}
