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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static java.lang.System.out;
import static java.net.URI.create;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class AuthorityRouterTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(value = 1)
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleAAuthoritiesTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(create("/authorities")))
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
    void handleBAuthoritiesSseTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(create("/authorities/sse")))
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
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleCAuthoritiesAsyncTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(create("/authorities/async")))
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
    void handleDAuthorityByIdTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(create("/authorities/by-id"))
                                .param("authorityId", "1")
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
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void handleEAuthorityByIdAsyncTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(create("/authorities/by-id/async"))
                                .param("authorityId", "1")
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
    void handleFCreateOrUpdateTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "MODER"
                }
                """;
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(create("/authorities/create-update"))
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
    void handleGCreateOrUpdateTest() throws Exception {
        @Language("JSON") String content = """
                {
                    "id": 3,
                    "name": "MODERATOR"
                }
                """;
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post(create("/authorities/create-update/async"))
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
    void handleHDeleteByIdTest() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(create("/authorities/delete"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .param("authorityId", "3")
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
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
