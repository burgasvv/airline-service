package org.burgas.ticketservice.config;

import org.burgas.ticketservice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(new UrlBasedCorsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new HttpSessionSecurityContextRepository()))
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(
                        httpRequest -> httpRequest

                                .requestMatchers(
                                        "/identities/create", "/images/by-id",

                                        "/airports", "/airports/by-country", "/airports/by-city",

                                        "/filials", "/filials/by-country", "/filials/by-city",

                                        "/departments", "/departments/by-id",

                                        "/filial-departments", "/filial-departments/by-filial-department",

                                        "/flights", "/flights/by-departure-city-arrival-city-by-departure-date",
                                        "/flights/by-departure-city-by-arrival-city", "/flights/by-departure-city-by-arrival-city-back",
                                        "/flights/by-id",

                                        "/tickets", "/tickets/by-id", "/tickets/create-update"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/identities/by-id", "/identities/by-username", "/identities/update",
                                        "/identities/change-password", "/identities/set-password",
                                        "/identities/upload-image", "/identities/change-image", "/identities/delete-image",

                                        "/positions", "/positions/by-id"
                                )
                                .hasAnyAuthority("ADMIN", "EMPLOYEE", "USER")

                                .requestMatchers(
                                        "/departments", "/departments/by-id", "/employees"
                                )
                                .hasAnyAuthority("ADMIN", "EMPLOYEE")

                                .requestMatchers(
                                        "/employees/by-id"
                                )
                                .hasAnyAuthority("EMPLOYEE")

                                .requestMatchers(
                                        "/requires/create-update",

                                        "/require-answers/by-user",

                                        "/employees/create"
                                )
                                .hasAnyAuthority("USER")

                                .requestMatchers(
                                        "/images/upload",

                                        "/identities", "/identities/enable-disable",

                                        "/authorities", "/authorities/by-id", "/authorities/create-update", "/authorities/delete",

                                        "/addresses", "/addresses/create-update-secured",

                                        "/airports/create-update",

                                        "/filials/create-update",

                                        "/departments/create-update", "/departments/delete",

                                        "/filial-departments/create-update", "/filial-departments/delete",

                                        "/positions/create-update", "/positions/delete",

                                        "/requires/by-closed",

                                        "/require-answers/by-admin", "/require-answers/send-answer-or-token",

                                        "/flights/create-update",  "/flights/add-employee", "/flights/remove-employee"
                                )
                                .hasAnyAuthority("ADMIN")
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
