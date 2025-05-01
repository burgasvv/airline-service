package org.burgas.excursionbackend.config;

import org.burgas.excursionbackend.service.CustomUserDetailsService;
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

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()))
                .cors(cors -> cors.configurationSource(new UrlBasedCorsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new HttpSessionSecurityContextRepository()))
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(
                        httpRequests -> httpRequests

                                .requestMatchers(
                                        "/authentication/csrf-token",

                                        "/images/by-id", "/images/by-id/async", "/images/by-id/data", "/images/by-id/data/async",

                                        "/identities/create", "/identities/create/async",

                                        "/countries", "/countries/sse", "/countries/async",
                                        "/countries/by-id", "/countries/by-id/async",

                                        "/cities", "/cities/async", "/cities/sse",
                                        "/cities/by-id", "/cities/by-id/async",

                                        "/sights", "/sights/sse", "/sights/async", "/sights/by-id", "/sights/by-id/async",

                                        "/guides", "/guides/sse", "/guides/async", "/guides/by-id", "/guides/by-id/async",

                                        "/excursions", "/excursions/sse", "/excursions/async",
                                        "/excursions/by-guide", "/excursions/by-guide/sse", "/excursions/by-guide/async",
                                        "/excursions/by-session", "/excursions/by-session/async",
                                        "/excursions/by-id", "/excursions/by-id/async",
                                        "/excursions/add-to-session", "/excursions/add-to-session/async"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/identities/by-id", "/identities/by-id/async",
                                        "/identities/update", "/identities/update/async",
                                        "/identities/delete", "/identities/delete/async",
                                        "/identities/upload-image", "/identities/upload-image/async",
                                        "/identities/change-image", "/identities/change-image/async",
                                        "/identities/delete-image", "/identities/delete-image/async",

                                        "/excursions/by-identity", "/excursions/by-identity/sse", "/excursions/by-identity/async",
                                        "/excursions/add-by-identity", "/excursions/add-by-identity/async",

                                        "/payments/make-identity-payment", "/payments/make-identity-payment-id",
                                        "/payments/by-identity", "/payments/by-identity/async"
                                )
                                .hasAnyAuthority("ADMIN", "USER")

                                .requestMatchers(
                                        "/images/upload", "/images/upload/async", "/images/change", "/images/change/async",
                                        "/images/delete", "/images/delete/async",

                                        "/authorities", "/authorities/sse", "/authorities/async",
                                        "/authorities/by-id", "/authorities/by-id/async",
                                        "/authorities/create-update", "/authorities/create-update/async",
                                        "/authorities/delete", "/authorities/delete/async",

                                        "/identities", "/identities/sse", "/identities/async",
                                        "/identities/by-excursion", "/identities/by-excursion/sse", "/identities/by-excursion/async",
                                        "/identities/control", "/identities/control/async",

                                        "/countries/create-update", "/countries/create-update/async",
                                        "/countries/delete", "/countries/delete/async",
                                        "/countries/upload-image", "/countries/upload-image/async",
                                        "/countries/change-image", "/countries/change-image/async",
                                        "/countries/delete-image", "/countries/delete-image/async",

                                        "/cities/create-update", "/cities/create-update/async",
                                        "/cities/delete", "/cities/delete/async",
                                        "/cities/upload-image", "/cities/upload-image/async",
                                        "/cities/change-image", "/cities/change-image/async",
                                        "/cities/delete-image", "/cities/delete-image/async",

                                        "/sights/create-update", "/sights/create-update/async",
                                        "/sights/delete", "/sights/delete/async",
                                        "/sights/upload-image", "/sights/upload-image/async",
                                        "/sights/change-image", "/sights/change-image/async",
                                        "/sights/delete-image", "/sights/delete-image/async",

                                        "/guides/create-update", "/guides/create-update/async",
                                        "/guides/delete", "/guides/delete/async",
                                        "/guides/upload-image", "/guides/upload-image/async",
                                        "/guides/change-image", "/guides/change-image/async",
                                        "/guides/delete-image", "/guides/delete-image/async",

                                        "/excursions/create-update", "/excursions/create-update/async",
                                        "/excursions/delete", "/excursions/delete/async",
                                        "/excursions/upload-image", "/excursions/upload-image/async",
                                        "/excursions/change-image", "/excursions/change-image/async",
                                        "/excursions/delete-image", "/excursions/delete-image/async",

                                        "/payments/by-id", "/payments/by-id/async"
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
