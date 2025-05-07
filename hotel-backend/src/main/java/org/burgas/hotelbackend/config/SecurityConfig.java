package org.burgas.hotelbackend.config;

import org.burgas.hotelbackend.service.CustomUserDetailsService;
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

    public static final String ADMIN = "ADMIN";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String CLIENT = "CLIENT";

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()))
                .cors(cors -> cors.configurationSource(new UrlBasedCorsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(
                        httpRequest -> httpRequest

                                .requestMatchers(
                                        "/authentication/csrf-token",

                                        "/identities/create", "/identities/create/async",

                                        "/images/by-id", "/images/by-id/async",

                                        "/countries", "/countries/async", "/countries/pages/{page}",
                                        "/countries/by-id", "/countries/by-id/async",
                                        "/countries/by-name", "/countries/by-name/async",

                                        "/cities", "/cities/async", "/cities/pages/{page}",
                                        "/cities/by-id", "/cities/by-id/async",
                                        "/cities/by-name", "/cities/by-name/async"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/identities/by-id", "/identities/by-id/async",
                                        "/identities/update", "/identities/update/async",
                                        "/identities/upload-image", "/identities/upload-image/async",
                                        "/identities/change-image", "/identities/change-image/async",
                                        "/identities/delete-image", "/identities/delete-image/async"
                                )
                                .hasAnyAuthority(ADMIN, EMPLOYEE, CLIENT)

                                .requestMatchers(
                                        "/authorities", "/authorities/async",
                                        "/authorities/by-id", "/authorities/by-id/async",
                                        "/authorities/create-update", "/authorities/create-update/async",
                                        "/authorities/delete", "/authorities/delete/async",

                                        "/identities", "/identities/async", "/identities/pages/{page}",
                                        "/identities/activate-deactivate", "/identities/activate-deactivate/async",

                                        "/countries/create-update", "/countries/create-update/async",
                                        "/countries/delete", "/countries/delete/async",

                                        "/cities/create-update", "/cities/create-update/async",
                                        "/cities/delete", "/cities/delete/async",

                                        "/addresses", "/addresses/async", "/addresses/pages/{page}",
                                        "/addresses/by-id", "/addresses/by-id/async",
                                        "/addresses/create-update", "/addresses/create-update/async",
                                        "/addresses/delete", "/addresses/delete/async"
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
