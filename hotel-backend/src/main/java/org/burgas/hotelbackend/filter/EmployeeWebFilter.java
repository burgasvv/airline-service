package org.burgas.hotelbackend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.hotelbackend.dto.IdentityResponse;
import org.burgas.hotelbackend.entity.Employee;
import org.burgas.hotelbackend.exception.EmployeeNotAuthorizedException;
import org.burgas.hotelbackend.exception.EmployeeNotFoundException;
import org.burgas.hotelbackend.exception.IdentityNotAuthenticatedException;
import org.burgas.hotelbackend.repository.EmployeeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.hotelbackend.message.EmployeeMessages.EMPLOYEE_NOT_AUTHORIZED;
import static org.burgas.hotelbackend.message.EmployeeMessages.EMPLOYEE_NOT_FOUND;
import static org.burgas.hotelbackend.message.IdentityMessages.IDENTITY_NOT_AUTHENTICATED;

@WebFilter(
        urlPatterns = {
                "/employees/upload-image", "/employees/upload-image/async",
                "/employees/change-image", "/employees/change-image/async",
                "/employees/delete-image", "/employees/delete-image/async"
        }
)
public class EmployeeWebFilter extends OncePerRequestFilter {

    private final EmployeeRepository employeeRepository;

    public EmployeeWebFilter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String employeeIdParam = request.getParameter("employeeId");
        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication.isAuthenticated()) {
            Long employeeId = Long.parseLong(employeeIdParam == null || employeeIdParam.isEmpty() ? "0" : employeeIdParam);
            IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();
            Employee employee = this.employeeRepository.findById(employeeId).orElse(null);

            if (employee != null) {

                if (identityResponse.getId().equals(employee.getIdentityId())) {
                    filterChain.doFilter(request, response);

                } else {
                    throw new EmployeeNotAuthorizedException(EMPLOYEE_NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND.getMessage() + ": EMPLOYEE-WEB-FILTER");
            }

        } else {
            throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
