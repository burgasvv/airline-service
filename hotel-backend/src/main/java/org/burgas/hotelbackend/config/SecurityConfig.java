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
                                        "/hotel-service/authentication/csrf-token",

                                        "/hotel-service/identities/create", "/hotel-service/identities/create/async",

                                        "/hotel-service/images/by-id", "/hotel-service/images/by-id/async",

                                        "/hotel-service/countries", "/hotel-service/countries/async", "/hotel-service/countries/pages/{page}",
                                        "/hotel-service/countries/by-id", "/hotel-service/countries/by-id/async",
                                        "/hotel-service/countries/by-name", "/hotel-service/countries/by-name/async",

                                        "/hotel-service/cities", "/hotel-service/cities/async", "/hotel-service/cities/pages/{page}",
                                        "/hotel-service/cities/by-id", "/hotel-service/cities/by-id/async",
                                        "/hotel-service/cities/by-name", "/hotel-service/cities/by-name/async",

                                        "/hotel-service/hotels", "/hotel-service/hotels/async", "/hotel-service/hotels/pages/{page}",
                                        "/hotel-service/hotels/by-id", "/hotel-service/hotels/by-id/async",
                                        "/hotel-service/hotels/by-name", "/hotel-service/hotels/by-name/async",

                                        "/hotel-service/filials", "/hotel-service/filials/async", "/hotel-service/filials/pages/{page}",
                                        "/hotel-service/filials/by-id", "/hotel-service/filials/by-id/async",

                                        "/hotel-service/rooms/by-filial", "/hotel-service/rooms/by-filial/async",
                                        "/hotel-service/rooms/by-filial/pages/{page}",
                                        "/hotel-service/rooms/by-id", "/hotel-service/rooms/by-id/async", "/hotel-service/rooms/rent-room"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/hotel-service/identities/by-id", "/hotel-service/identities/by-id/async",
                                        "/hotel-service/identities/update", "/hotel-service/identities/update/async",
                                        "/hotel-service/identities/upload-image", "/hotel-service/identities/upload-image/async",
                                        "/hotel-service/identities/change-image", "/hotel-service/identities/change-image/async",
                                        "/hotel-service/identities/delete-image", "/hotel-service/identities/delete-image/async"
                                )
                                .hasAnyAuthority(ADMIN, EMPLOYEE, CLIENT)

                                .requestMatchers(
                                        "/hotel-service/employees/upload-image", "/hotel-service/employees/upload-image/async",
                                        "/hotel-service/employees/change-image", "/hotel-service/employees/change-image/async",
                                        "/hotel-service/employees/delete-image", "/hotel-service/employees/delete-image/async"
                                )
                                .hasAnyAuthority(EMPLOYEE)

                                .requestMatchers(
                                        "/hotel-service/authorities", "/hotel-service/authorities/async",
                                        "/hotel-service/authorities/by-id", "/hotel-service/authorities/by-id/async",
                                        "/hotel-service/authorities/create-update", "/hotel-service/authorities/create-update/async",
                                        "/hotel-service/authorities/delete", "/hotel-service/authorities/delete/async",

                                        "/hotel-service/identities", "/hotel-service/identities/async", "/hotel-service/identities/pages/{page}",
                                        "/hotel-service/identities/activate-deactivate", "/hotel-service/identities/activate-deactivate/async",

                                        "/hotel-service/countries/create-update", "/hotel-service/countries/create-update/async",
                                        "/hotel-service/countries/delete", "/hotel-service/countries/delete/async",

                                        "/hotel-service/cities/create-update", "/hotel-service/cities/create-update/async",
                                        "/hotel-service/cities/delete", "/hotel-service/cities/delete/async",

                                        "/hotel-service/addresses", "/hotel-service/addresses/async", "/hotel-service/addresses/pages/{page}",
                                        "/hotel-service/addresses/by-id", "/hotel-service/addresses/by-id/async",
                                        "/hotel-service/addresses/create-update", "/hotel-service/addresses/create-update/async",
                                        "/hotel-service/addresses/delete", "/hotel-service/addresses/delete/async",

                                        "/hotel-service/hotels/create-update", "/hotel-service/hotels/create-update/async",
                                        "/hotel-service/hotels/delete", "/hotel-service/hotels/delete/async",
                                        "/hotel-service/hotels/upload-image", "/hotel-service/hotels/upload-image/async",
                                        "/hotel-service/hotels/change-image", "/hotel-service/hotels/change-image/async",
                                        "/hotel-service/hotels/delete-image", "/hotel-service/hotels/delete-image/async",

                                        "/hotel-service/filials/create-update", "/hotel-service/filials/create-update/async",
                                        "/hotel-service/filials/delete", "/hotel-service/filials/delete/async",
                                        "/hotel-service/filials/upload-image", "/hotel-service/filials/upload-image/async",
                                        "/hotel-service/filials/change-image", "/hotel-service/filials/change-image/async",
                                        "/hotel-service/filials/delete-image", "/hotel-service/filials/delete-image/async",

                                        "/hotel-service/departments", "/hotel-service/departments/async",
                                        "/hotel-service/departments/pages/{page}",
                                        "/hotel-service/departments/by-filial", "/hotel-service/departments/by-filial/async",
                                        "/hotel-service/departments/by-id", "/hotel-service/departments/by-id/async",
                                        "/hotel-service/departments/by-name", "/hotel-service/departments/by-name/async",
                                        "/hotel-service/departments/create-update", "/hotel-service/departments/create-update/async",
                                        "/hotel-service/departments/delete", "/hotel-service/departments/delete/async",

                                        "/hotel-service/positions", "/hotel-service/positions/async", "/hotel-service/positions/pages/{page}",
                                        "/hotel-service/positions/by-id", "/hotel-service/positions/by-id/async",
                                        "/hotel-service/positions/create-update", "/hotel-service/positions/create-update/async",
                                        "/hotel-service/positions/delete", "/hotel-service/positions/delete/async",

                                        "/hotel-service/employees", "/hotel-service/employees/async", "/hotel-service/employees/pages/{page}",
                                        "/hotel-service/employees/by-id", "/hotel-service/employees/by-id/async",
                                        "/hotel-service/employees/create-update", "/hotel-service/employees/create-update/async",
                                        "/hotel-service/employees/delete", "/hotel-service/employees/delete/async",

                                        "/hotel-service/rooms/create-update", "/hotel-service/rooms/create-update/async",
                                        "/hotel-service/rooms/delete", "/hotel-service/rooms/delete/async",
                                        "/hotel-service/rooms/upload-images", "/hotel-service/rooms/upload-images/async",

                                        "/hotel-service/clients", "/hotel-service/clients/async", "/hotel-service/clients/pages/{page}",
                                        "/hotel-service/clients/by-id", "/hotel-service/clients/by-id/async",
                                        "/hotel-service/clients/create-update", "/hotel-service/clients/create-update/async",

                                        "/hotel-service/payments/by-client", "/hotel-service/payments/by-client/async",
                                        "/hotel-service/payments/close-payment", "/hotel-service/payments/cancel-payment",
                                        "/hotel-service/payments/early-payment"
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
