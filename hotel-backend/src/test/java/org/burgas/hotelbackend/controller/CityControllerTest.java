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
import static org.springframework.http.MediaType.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class CityControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CityControllerTest.class);
    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(value = 1)
    void getAllCities() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/cities")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 2)
    void getAllCitiesAsync() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/cities/async")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 3)
    void getAllCityPages() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/cities/pages/1"))
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
    void getCityById() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/cities/by-id"))
                                .param("cityId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 5)
    void getCityByIdAsync() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/cities/by-id/async"))
                                .param("cityId", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 6)
    void getCityByName() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/cities/by-name"))
                                .param("name", "Москва")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 7)
    void getCityByNameAsync() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/cities/by-name/async"))
                                .param("name", "Пекин")
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
    void createOrUpdateCity() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "Волгоград",
                    "description": "Описание города Волгоград",
                    "countryId": 1,
                    "capital": false
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/cities/create-update"))
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
    void createOrUpdateCityAsync() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "Вайс Сити",
                    "description": "Описание города Вайс Сити",
                    "countryId": 3,
                    "capital": false
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/cities/create-update/async"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 10)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void deleteCity() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/cities/delete"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("cityId", "7")
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
    void deleteCityAsync() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/cities/delete/async"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("cityId", "8")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(TEXT_PLAIN_VALUE))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andReturn();
    }
}