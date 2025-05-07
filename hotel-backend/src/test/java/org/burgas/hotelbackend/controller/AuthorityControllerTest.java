package org.burgas.hotelbackend.controller;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class AuthorityControllerTest {

    private static final Logger log = LoggerFactory.getLogger(AuthorityControllerTest.class);
    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(value = 1)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void getAllAuthorities() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/authorities")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 2)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void getAllAuthoritiesAsync() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/authorities/async")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 3)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void getAuthorityById() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/authorities/by-id"))
                                .param("authorityId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 4)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void getAuthorityByIdAsync() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/authorities/by-id/async"))
                                .param("authorityId", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 5)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void createOrUpdateAuthority() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "USER"
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/authorities/create-update"))
                                .contentType(APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 6)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void createOrUpdateAuthorityAsync() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "MODERATOR"
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/authorities/create-update/async"))
                                .contentType(APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 7)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void deleteAuthority() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/authorities/delete"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("authorityId", "4")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 8)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void deleteAuthorityAsync() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/authorities/delete/async"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("authorityId", "5")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }
}