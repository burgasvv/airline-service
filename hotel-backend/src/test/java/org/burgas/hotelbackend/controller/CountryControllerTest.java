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
class CountryControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CountryControllerTest.class);
    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(value = 1)
    void getAllCountries() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/countries")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 2)
    void getAllCountriesAsync() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/countries/async")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 3)
    void getAllCountryPages() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/countries/pages/1"))
                                .param("size", "3")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 4)
    void getCountryById() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/countries/by-id"))
                                .param("countryId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 5)
    void getCountryByIdAsync() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/countries/by-id/async"))
                                .param("countryId", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 6)
    void getCountryByName() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/countries/by-name"))
                                .param("name", "Россия")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 7)
    void getCountryByNameAsync() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/countries/by-name/async"))
                                .param("name", "Китай")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 8)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void createOrUpdate() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "Испания",
                    "description": "Описание страны Испания"
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/countries/create-update"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 9)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void createOrUpdateAsync() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "Великобритания",
                    "description": "Описание страны Великобритания"
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/countries/create-update/async"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 10)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void deleteCountry() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/countries/delete"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("countryId", "4")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 11)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void deleteCountryAsync() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/countries/delete/async"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("countryId", "5")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(TEXT_PLAIN))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }
}