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
                                        "/cities/by-name", "/cities/by-name/async",

                                        "/hotels", "/hotels/async", "/hotels/pages/{page}",
                                        "/hotels/by-id", "/hotels/by-id/async",
                                        "/hotels/by-name", "/hotels/by-name/async",

                                        "/filials", "/filials/async", "/filials/pages/{page}",
                                        "/filials/by-id", "/filials/by-id/async",

                                        "/rooms/by-filial", "/rooms/by-filial/async", "/rooms/by-filial/pages/{page}",
                                        "/rooms/by-id", "/rooms/by-id/async", "/rooms/rent-room"
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
                                        "/employees/upload-image", "/employees/upload-image/async",
                                        "/employees/change-image", "/employees/change-image/async",
                                        "/employees/delete-image", "/employees/delete-image/async"
                                )
                                .hasAnyAuthority(EMPLOYEE)

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
                                        "/addresses/delete", "/addresses/delete/async",

                                        "/hotels/create-update", "/hotels/create-update/async",
                                        "/hotels/delete", "/hotels/delete/async",
                                        "/hotels/upload-image", "/hotels/upload-image/async",
                                        "/hotels/change-image", "/hotels/change-image/async",
                                        "/hotels/delete-image", "/hotels/delete-image/async",

                                        "/filials/create-update", "/filials/create-update/async",
                                        "/filials/delete", "/filials/delete/async",
                                        "/filials/upload-image", "/filials/upload-image/async",
                                        "/filials/change-image", "/filials/change-image/async",
                                        "/filials/delete-image", "/filials/delete-image/async",

                                        "/departments", "/departments/async", "/departments/pages/{page}",
                                        "/departments/by-filial", "/departments/by-filial/async",
                                        "/departments/by-id", "/departments/by-id/async",
                                        "/departments/by-name", "/departments/by-name/async",
                                        "/departments/create-update", "/departments/create-update/async",
                                        "/departments/delete", "/departments/delete/async",

                                        "/positions", "/positions/async", "/positions/pages/{page}",
                                        "/positions/by-id", "/positions/by-id/async",
                                        "/positions/create-update", "/positions/create-update/async",
                                        "/positions/delete", "/positions/delete/async",

                                        "/employees", "/employees/async", "/employees/pages/{page}",
                                        "/employees/by-id", "/employees/by-id/async",
                                        "/employees/create-update", "/employees/create-update/async",
                                        "/employees/delete", "/employees/delete/async",

                                        "/rooms/create-update", "/rooms/create-update/async",
                                        "/rooms/delete", "/rooms/delete/async",
                                        "/rooms/upload-images", "/rooms/upload-images/async",

                                        "/clients", "/clients/async", "/clients/pages/{page}",
                                        "/clients/by-id", "/clients/by-id/async",
                                        "/clients/create-update", "/clients/create-update/async",

                                        "/payments/by-client", "/payments/by-client/async",
                                        "/payments/close-payment", "/payments/cancel-payment",
                                        "/payments/early-payment"
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
