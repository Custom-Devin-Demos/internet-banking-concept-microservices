package com.javatodev.finance.controller;

import com.javatodev.finance.exception.EntityNotFoundException;
import com.javatodev.finance.model.dto.User;
import com.javatodev.finance.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageSource messageSource;

    @Test
    void readUser_found() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setIdentificationNumber("ID123");
        when(userService.readUser("ID123")).thenReturn(user);

        mockMvc.perform(get("/api/v1/user/{identification}", "ID123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.identificationNumber").value("ID123"))
            .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void readUser_notFound_returnsBadRequest() throws Exception {
        when(userService.readUser("missing")).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/v1/user/{identification}", "missing"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void readUsers_returnsList() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setIdentificationNumber("ID123");
        when(userService.readUsers(any(Pageable.class))).thenReturn(List.of(user));

        mockMvc.perform(get("/api/v1/user"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].identificationNumber").value("ID123"));
    }
}
