package org.burgas.excursionbackend.router;

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

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class GuideRouterTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(value = 1)
    void getAllGuidesTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/guides")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 2)
    void getAllGuidesSseTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/guides/sse")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(TEXT_EVENT_STREAM))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 3)
    void getAllGuidesAsyncTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/guides/async")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 4)
    void getAllGuidesPagesTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/guides/pages/1"))
                                .param("size", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 5)
    void getGuideByIdTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/guides/by-id"))
                                .param("guideId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 6)
    void getGuideByIdAsyncTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/guides/by-id/async"))
                                .param("guideId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 7)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void createGuideTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "Валерия",
                    "surname": "Касаева",
                    "patronymic": "Петровна",
                    "phone": "9135678934",
                    "about": "О Касаевой Валерии Петровне",
                    "languages": [
                        1, 2
                    ]
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/guides/create-update"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 8)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void updateGuideTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "id": 4,
                    "about": "О Касаевой Валерии Петровне EDITED",
                    "languages": [
                        1, 2, 3
                    ]
                }""";

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/guides/create-update"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 9)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void deleteGuideTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/guides/delete"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("guideId", "4")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 9)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void uploadGuideImageTest() throws Exception {
        byte[] bytes = Files.readAllBytes(Path.of("src/main/resources/static/images (1).jpeg"));
        MockPart file = new MockPart("file", "images (1).jpeg", bytes, IMAGE_JPEG);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.multipart(URI.create("/guides/upload-image"))
                                .part(file)
                                .param("guideId", "1")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 10)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void changeGuideImageTest() throws Exception {
        byte[] bytes = Files.readAllBytes(Path.of("src/main/resources/static/images.jpeg"));
        MockPart file = new MockPart("file", "images.jpeg", bytes, IMAGE_JPEG);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.multipart(URI.create("/guides/change-image"))
                                .part(file)
                                .param("guideId", "1")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 11)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void deleteGuideImageTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/guides/delete-image"))
                                .param("guideId", "1")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }
}