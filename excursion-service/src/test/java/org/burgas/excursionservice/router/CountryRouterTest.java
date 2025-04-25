package org.burgas.excursionservice.router;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.net.URI;

import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class CountryRouterTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(value = 1)
    void handleCountriesTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/countries")))
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
    void handleCountriesSseTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/countries/sse")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(TEXT_EVENT_STREAM))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }

    @Test
    @Order(value = 3)
    void handleCountriesAsyncTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/countries/async")))
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
    void handleCountryByIdTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/countries/by-id"))
                                .param("countryId", "1")
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
    @Order(value = 5)
    void handleCountryByIdAsyncTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/countries/by-id/async"))
                                .param("countryId", "2")
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
    @Order(value = 6)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleCreateCountryTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "Испания",
                    "description": "Описание страны Испания",
                    "population": 48356783
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
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }

    @Test
    @Order(value = 7)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleUpdateCountryTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "id": 4,
                    "description": "Описание страны Испания EDITED"
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
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }

    @Test
    @Order(value = 8)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleDeleteCountryTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/countries/delete"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("countryId", "4")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }

    @Test
    @Order(value = 9)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleUploadCountryImageTest() {
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/static/images.jpeg")) {
            MockPart file = new MockPart("file", "images.jpeg", fileInputStream.readAllBytes(), IMAGE_JPEG);
            this.mockMvc
                    .perform(
                            MockMvcRequestBuilders.multipart(URI.create("/countries/upload-image"))
                                    .part(file)
                                    .param("countryId", "1")
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                    .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                    .andDo(result -> out.println(
                            new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                    ))
                    .andReturn();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(value = 10)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleChangeCountryImageTest() {
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/static/images (1).jpeg")) {
            MockPart file = new MockPart("file", "images (1).jpeg", fileInputStream.readAllBytes(), IMAGE_JPEG);
            this.mockMvc
                    .perform(
                            MockMvcRequestBuilders.multipart(URI.create("/countries/change-image"))
                                    .part(file)
                                    .param("countryId", "1")
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                    .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                    .andDo(result -> out.println(
                            new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                    ))
                    .andReturn();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(value = 11)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleDeleteCountryImage() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/countries/delete-image"))
                                .param("countryId", "1")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
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
