package org.burgas.excursionservice.router;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

import static java.lang.System.out;
import static java.net.URI.create;
import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorityRouterTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void getAllAuthorities() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(create("/authorities")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }

    @Test
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void getAllAuthoritiesSse() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/authorities/sse")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }

    @Test
    @WithMockUser(value = "admin", username = "admin@gmail.com", password = "admin", authorities = "ADMIN")
    void getAllAuthoritiesAsync() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI.create("/authorities/async")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(
                        new String(result.getResponse().getContentAsString().getBytes(UTF_8))
                ))
                .andReturn();
    }
}
