package org.burgas.hotelbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Controller
@RequestMapping(value = "/hotel-service/authentication")
public class AuthenticationController {

    @GetMapping(value = "/csrf-token")
    public @ResponseBody ResponseEntity<CsrfToken> getCsrfToken(@RequestAttribute(name = "_csrf") CsrfToken csrfToken) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(csrfToken);
    }
}
