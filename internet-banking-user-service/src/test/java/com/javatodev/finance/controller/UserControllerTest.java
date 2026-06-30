package com.javatodev.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatodev.finance.exception.EntityNotFoundException;
import com.javatodev.finance.model.dto.Status;
import com.javatodev.finance.model.dto.User;
import com.javatodev.finance.model.dto.UserUpdateRequest;
import com.javatodev.finance.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User sampleUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setIdentification("199512345678");
        user.setStatus(Status.PENDING);
        return user;
    }

    @Test
    void createUser_returnsOk() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(sampleUser());

        mockMvc.perform(post("/api/v1/bank-users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void updateUser_returnsOk() throws Exception {
        User updated = sampleUser();
        updated.setStatus(Status.APPROVED);
        when(userService.updateUser(eq(1L), any(UserUpdateRequest.class))).thenReturn(updated);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setStatus(Status.APPROVED);

        mockMvc.perform(patch("/api/v1/bank-users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void readUsers_returnsOk() throws Exception {
        List<User> users = Collections.singletonList(sampleUser());
        when(userService.readUsers(any())).thenReturn(users);

        mockMvc.perform(get("/api/v1/bank-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void readUser_returnsOk() throws Exception {
        when(userService.readUser(1L)).thenReturn(sampleUser());

        mockMvc.perform(get("/api/v1/bank-users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identification").value("199512345678"));
    }

    @Test
    void readUser_notFoundReturnsBadRequest() throws Exception {
        when(userService.readUser(99L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/v1/bank-users/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists());
    }
}
