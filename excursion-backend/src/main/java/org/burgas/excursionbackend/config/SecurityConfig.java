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
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
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
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(
                        httpRequests -> httpRequests

                                .requestMatchers(
                                        "/excursion-service/authentication/csrf-token",

                                        "/excursion-service/images/by-id", "/excursion-service/images/by-id/async",
                                        "/excursion-service/images/by-id/data", "/excursion-service/images/by-id/data/async",

                                        "/excursion-service/identities/create", "/excursion-service/identities/create/async",

                                        "/excursion-service/countries", "/excursion-service/countries/sse",
                                        "/excursion-service/countries/async", "/excursion-service/countries/pages/{page}",
                                        "/excursion-service/countries/by-id", "/excursion-service/countries/by-id/async",

                                        "/excursion-service/cities", "/excursion-service/cities/async",
                                        "/excursion-service/cities/sse", "/excursion-service/cities/pages/{page}",
                                        "/excursion-service/cities/by-id", "/excursion-service/cities/by-id/async",

                                        "/excursion-service/sights", "/excursion-service/sights/sse",
                                        "/excursion-service/sights/async", "/excursion-service/sights/pages/{page}",
                                        "/excursion-service/sights/by-id", "/excursion-service/sights/by-id/async",

                                        "/excursion-service/guides", "/excursion-service/guides/sse",
                                        "/excursion-service/guides/async", "/excursion-service/guides/pages/{page}",
                                        "/excursion-service/guides/by-id", "/excursion-service/guides/by-id/async",

                                        "/excursion-service/excursions", "/excursion-service/excursions/sse",
                                        "/excursion-service/excursions/async", "/excursion-service/excursions/pages/{page}",
                                        "/excursion-service/excursions/by-guide", "/excursion-service/excursions/by-guide/sse",
                                        "/excursion-service/excursions/by-guide/async",
                                        "/excursion-service/excursions/by-session", "/excursion-service/excursions/by-session/async",
                                        "/excursion-service/excursions/by-id", "/excursion-service/excursions/by-id/async",
                                        "/excursion-service/excursions/add-to-session", "/excursion-service/excursions/add-to-session/async"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/excursion-service/identities/by-id", "/excursion-service/identities/by-id/async",
                                        "/excursion-service/identities/update", "/excursion-service/identities/update/async",
                                        "/excursion-service/identities/delete", "/excursion-service/identities/delete/async",
                                        "/excursion-service/identities/upload-image", "/excursion-service/identities/upload-image/async",
                                        "/excursion-service/identities/change-image", "/excursion-service/identities/change-image/async",
                                        "/excursion-service/identities/delete-image", "/excursion-service/identities/delete-image/async",

                                        "/excursion-service/excursions/by-identity", "/excursion-service/excursions/by-identity/sse",
                                        "/excursion-service/excursions/by-identity/async",
                                        "/excursion-service/excursions/add-by-identity", "/excursion-service/excursions/add-by-identity/async",

                                        "/excursion-service/payments/make-identity-payment", "/excursion-service/payments/make-identity-payment-id",
                                        "/excursion-service/payments/by-identity", "/excursion-service/payments/by-identity/async"
                                )
                                .hasAnyAuthority("ADMIN", "USER")

                                .requestMatchers(
                                        "/excursion-service/images/upload", "/excursion-service/images/upload/async",
                                        "/excursion-service/images/change", "/excursion-service/images/change/async",
                                        "/excursion-service/images/delete", "/excursion-service/images/delete/async",

                                        "/excursion-service/authorities", "/excursion-service/authorities/sse",
                                        "/excursion-service/authorities/async",
                                        "/excursion-service/authorities/by-id", "/excursion-service/authorities/by-id/async",
                                        "/excursion-service/authorities/create-update", "/excursion-service/authorities/create-update/async",
                                        "/excursion-service/authorities/delete", "/excursion-service/authorities/delete/async",

                                        "/excursion-service/identities", "/excursion-service/identities/sse",
                                        "/excursion-service/identities/async", "/excursion-service/identities/pages/{page}",
                                        "/excursion-service/identities/by-excursion", "/excursion-service/identities/by-excursion/sse",
                                        "/excursion-service/identities/by-excursion/async",
                                        "/excursion-service/identities/control", "/excursion-service/identities/control/async",

                                        "/excursion-service/countries/create-update", "/excursion-service/countries/create-update/async",
                                        "/excursion-service/countries/delete", "/excursion-service/countries/delete/async",
                                        "/excursion-service/countries/upload-image", "/excursion-service/countries/upload-image/async",
                                        "/excursion-service/countries/change-image", "/excursion-service/countries/change-image/async",
                                        "/excursion-service/countries/delete-image", "/excursion-service/countries/delete-image/async",

                                        "/excursion-service/cities/create-update", "/excursion-service/cities/create-update/async",
                                        "/excursion-service/cities/delete", "/excursion-service/cities/delete/async",
                                        "/excursion-service/cities/upload-image", "/excursion-service/cities/upload-image/async",
                                        "/excursion-service/cities/change-image", "/excursion-service/cities/change-image/async",
                                        "/excursion-service/cities/delete-image", "/excursion-service/cities/delete-image/async",

                                        "/excursion-service/sights/create-update", "/excursion-service/sights/create-update/async",
                                        "/excursion-service/sights/delete", "/excursion-service/sights/delete/async",
                                        "/excursion-service/sights/upload-image", "/excursion-service/sights/upload-image/async",
                                        "/excursion-service/sights/change-image", "/excursion-service/sights/change-image/async",
                                        "/excursion-service/sights/delete-image", "/excursion-service/sights/delete-image/async",

                                        "/excursion-service/guides/create-update", "/excursion-service/guides/create-update/async",
                                        "/excursion-service/guides/delete", "/excursion-service/guides/delete/async",
                                        "/excursion-service/guides/upload-image", "/excursion-service/guides/upload-image/async",
                                        "/excursion-service/guides/change-image", "/excursion-service/guides/change-image/async",
                                        "/excursion-service/guides/delete-image", "/excursion-service/guides/delete-image/async",

                                        "/excursion-service/excursions/create-update",
                                        "/excursion-service/excursions/delete", "/excursion-service/excursions/delete/async",
                                        "/excursion-service/excursions/upload-image", "/excursion-service/excursions/upload-image/async",
                                        "/excursion-service/excursions/change-image", "/excursion-service/excursions/change-image/async",
                                        "/excursion-service/excursions/delete-image", "/excursion-service/excursions/delete-image/async",

                                        "/excursion-service/payments/by-id", "/excursion-service/payments/by-id/async"
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
