package org.burgas.excursionbackend.service;

import org.burgas.excursionbackend.dto.AuthorityResponse;
import org.burgas.excursionbackend.mapper.AuthorityMapper;
import org.burgas.excursionbackend.repository.AuthorityRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.lang.System.out;

@SpringBootTest
@AutoConfigureDataJpa
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class AuthorityServiceTest {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    AuthorityMapper authorityMapper;

    @Test
    @Order(value = 1)
    void findAll() {
        List<AuthorityResponse> authorityResponses = this.authorityRepository.findAll()
                .parallelStream()
                .map(this.authorityMapper::toResponse)
                .toList();
        Assertions.assertEquals(2, authorityResponses.size());
        authorityResponses.forEach(
                authorityResponse -> Assertions.assertInstanceOf(AuthorityResponse.class, authorityResponse)
        );
        List<String> testAuthorities = List.of("ADMIN", "USER");
        List<String> authorities = authorityResponses
                .parallelStream()
                .map(AuthorityResponse::getAuthority)
                .toList();
        Assertions.assertLinesMatch(testAuthorities, authorities);
        out.println(authorityResponses);
    }

    @Test
    @Order(value = 1)
    void findById() {
        AuthorityResponse authorityResponse = this.authorityRepository.findById(1L)
                .map(this.authorityMapper::toResponse)
                .orElseThrow();
        Assertions.assertEquals("ADMIN", authorityResponse.getAuthority());
        Assertions.assertInstanceOf(AuthorityResponse.class, authorityResponse);
        out.println(authorityResponse);
    }
}