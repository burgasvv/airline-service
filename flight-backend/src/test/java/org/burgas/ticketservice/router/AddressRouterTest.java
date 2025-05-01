package org.burgas.ticketservice.router;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class AddressRouterTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(value = 1)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleAddressesTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/addresses")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }

    @Test
    @Order(value = 2)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleCreateAddressTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "cityId": 1,
                    "street": "Калужская",
                    "house": "12a",
                    "apartment": "56"
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/addresses/create-update-secured"))
                                .contentType(APPLICATION_JSON)
                                .content(content)
                                .accept(APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }

    @Test
    @Order(value = 3)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleUpdateAddressTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "id": 11,
                    "cityId": 1,
                    "street": "Московская",
                    "house": "78/1",
                    "apartment": "45"
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/addresses/create-update-secured"))
                                .accept(APPLICATION_JSON)
                                .content(content)
                                .contentType(APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }

    @Test
    @Order(value = 4)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleDeleteAddressTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/addresses/delete"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("addressId", "11")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }
}