package io.fullstack.augmented.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fullstack.augmented.entity.User;
import io.fullstack.augmented.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnAllUsers() throws Exception {
        var user = new User("testuser", "test@example.com");
        user.setId(1L);
        user.setCreatedAt(OffsetDateTime.now());

        when(userService.findAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(user))));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        var user = new User("testuser", "test@example.com");
        user.setId(1L);
        user.setCreatedAt(OffsetDateTime.now());

        when(userService.findUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void shouldCreateUser() throws Exception {
        var request = new User("newuser", "new@example.com");
        var savedUser = new User("newuser", "new@example.com");
        savedUser.setId(2L);
        savedUser.setCreatedAt(OffsetDateTime.now());

        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(savedUser)));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var request = new User("updated", "update@example.com");
        var updatedUser = new User("updated", "update@example.com");
        updatedUser.setId(1L);
        updatedUser.setCreatedAt(OffsetDateTime.now());

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
