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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
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
class ExcursionRouterTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(value = 1)
    void getAllExcursionsTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/excursions")))
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
    void getAllExcursionsSseTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/excursions/sse")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(TEXT_EVENT_STREAM))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 3)
    void getAllExcursionsAsyncTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/excursions/async")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 4)
    void getAllExcursionPagesTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/excursions/pages/1"))
                                .param("size", "3")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 4)
    void getExcursionsByGuideTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/excursions/by-guide"))
                                .param("guideId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 5)
    void getExcursionsByGuideSseTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/excursions/by-guide/sse"))
                                .param("guideId", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(TEXT_EVENT_STREAM))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 6)
    void getExcursionsByGuideAsyncTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/excursions/by-guide/async"))
                                .param("guideId", "3")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 7)
    @WithUserDetails(value = "admin@gmail.com", userDetailsServiceBeanName = "customUserDetailsService")
    void getExcursionsByIdentityTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/excursions/by-identity"))
                                .param("identityId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 8)
    @WithUserDetails(value = "user@gmail.com", userDetailsServiceBeanName = "customUserDetailsService")
    void getExcursionsByIdentitySseTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/excursions/by-identity/sse"))
                                .param("identityId", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(TEXT_EVENT_STREAM))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 9)
    @WithUserDetails(value = "user@gmail.com",userDetailsServiceBeanName = "customUserDetailsService")
    void getExcursionsByIdentityAsyncTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/excursions/by-identity/async"))
                                .param("identityId", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 10)
    void getExcursionByIdTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/excursions/by-id"))
                                .param("excursionId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 11)
    void getExcursionByIdAsyncTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(URI.create("/excursions/by-id/async"))
                                .param("excursionId", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 12)
    @WithUserDetails(value = "admin@gmail.com", userDetailsServiceBeanName = "customUserDetailsService")
    void addExcursionByIdentityTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/excursions/add-by-identity"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("excursionId", "3")
                                .param("identityId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 13)
    @WithUserDetails(value = "user@gmail.com", userDetailsServiceBeanName = "customUserDetailsService")
    void addExcursionByIdentityAsyncTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/excursions/add-by-identity/async"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("excursionId", "4")
                                .param("identityId", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 14)
    void addExcursionToSessionTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/excursions/add-to-session"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .session(new MockHttpSession())
                                .param("excursionId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 15)
    void addExcursionToSessionAsyncTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/excursions/add-to-session/async"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .session(new MockHttpSession())
                                .param("excursionId", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 16)
    void getExcursionsBySessionTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/excursions/by-session")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 17)
    void getExcursionsBySessionAsyncTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/excursions/by-session/async")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 18)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void createExcursionTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "New Excursion",
                    "description": "New Excursion description",
                    "guideId": 1,
                    "cost": 15000,
                    "starts": "2026-10-25T15:30",
                    "ends": "2026-10-25T21:30",
                    "inProgress": false,
                    "passed": false,
                    "sights": [1, 3, 5]
                }
                """;

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/excursions/create-update"))
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
    @Order(value = 19)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void updateExcursionTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "id": 5,
                    "name": "New Excursion EDITED",
                    "cost": 10000,
                    "starts": "2026-10-25T15:30",
                    "ends": "2026-10-25T18:30",
                    "sights": [1, 3]
                }
                """;

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(URI.create("/excursions/create-update"))
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
    @Order(value = 20)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void deleteExcursionByIdTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/excursions/delete"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("excursionId", "5")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 21)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void uploadExcursionImageTest() throws Exception {
        byte[] bytes = Files.readAllBytes(Path.of("src/main/resources/static/images (1).jpeg"));
        MockPart file = new MockPart("file", "images (1).jpeg", bytes, IMAGE_JPEG);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.multipart(URI.create("/excursions/upload-image"))
                                .part(file)
                                .param("excursionId", "1")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 22)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void changeExcursionImageTest() throws Exception {
        byte[] bytes = Files.readAllBytes(Path.of("src/main/resources/static/images.jpeg"));
        MockPart file = new MockPart("file", "images.jpeg", bytes, IMAGE_JPEG);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.multipart(URI.create("/excursions/change-image"))
                                .part(file)
                                .param("excursionId", "1")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 23)
    @WithMockUser(username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void deleteExcursionImageTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(URI.create("/excursions/delete-image"))
                                .param("excursionId", "1")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(TEXT_PLAIN, UTF_8)))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }
}