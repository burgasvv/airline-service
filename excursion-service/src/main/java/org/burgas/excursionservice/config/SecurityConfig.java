package org.burgas.excursionservice.config;

import org.burgas.excursionservice.service.CustomUserDetailsService;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(new UrlBasedCorsConfigurationSource()))
                .authenticationManager(authenticationManager())
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(
                        new HttpSessionSecurityContextRepository()
                ))
                .authorizeHttpRequests(
                        httpRequests -> httpRequests

                                .requestMatchers(
                                        "/identities/create", "/identities/create/async",

                                        "/countries","/countries/sse", "/countries/async",
                                        "/countries/by-id", "/countries/by-id/async",

                                        "/cities", "/cities/async", "/cities/sse",
                                        "/cities/by-id", "/cities/by-id/async",

                                        "/images/by-id", "/images/by-id/async", "/images/by-id/data", "/images/by-id/data/async"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/identities/by-id", "/identities/by-id/async",
                                        "/identities/update", "/identities/update/async",
                                        "/identities/delete", "/identities/delete/async",
                                        "/identities/upload-image", "/identities/upload-image/async",
                                        "/identities/change-image", "/identities/change-image/async",
                                        "/identities/delete-image", "/identities/delete-image/async"
                                )
                                .hasAnyAuthority("ADMIN", "USER")

                                .requestMatchers(
                                        "/authorities", "/authorities/sse", "/authorities/async",
                                        "/authorities/by-id", "/authorities/by-id/async",
                                        "/authorities/create-update", "/authorities/create-update/async",
                                        "/authorities/delete", "/authorities/delete/async",

                                        "/identities", "/identities/sse", "/identities/async",
                                        "/identities/control", "/identities/control/async",

                                        "/countries/create-update", "/countries/create-update/async",
                                        "/countries/delete", "/countries/delete/async",

                                        "/cities/create-update", "/cities/create-update/async",
                                        "/cities/delete", "/cities/delete/async",

                                        "/images/upload", "/images/upload/async", "/images/change", "/images/change/async",
                                        "/images/delete", "/images/delete/async"
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
