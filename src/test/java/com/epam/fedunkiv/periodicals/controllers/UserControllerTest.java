package com.epam.fedunkiv.periodicals.controllers;


import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
//    private static final ModelMapper mapper = new ModelMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private FullUserDto user;

    @BeforeEach
    void init() {
        user = new FullUserDto("2", "John Snow", "CLIENT", "john@gmail.com", "00.00","address",
                "true","123456Q@q",  LocalDateTime.now().toString(), LocalDateTime.now().toString());
    }
    @Test
    void GetAllUsers() throws Exception{
        lenient().when(userService.getAll()).thenReturn(List.of(user));
        mockMvc.perform(get("/users/get-all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("2")))
                .andExpect(jsonPath("$[0].fullName", is("John Snow")))
                .andExpect(jsonPath("$[0].email", is("john@gmail.com")))
                .andExpect(jsonPath("$[0].role", is("CLIENT")));
    }

    @Test
    void GetByEmail() throws Exception{
        lenient().when(userService.getByEmail("john@gmail.com")).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/get-by/john@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("2")))
                .andExpect(jsonPath("$.fullName", is("John Snow")))
                .andExpect(jsonPath("$.email", is("john@gmail.com")))
                .andExpect(jsonPath("$.role", is("CLIENT")));
    }

    @Test
    void testOk()throws Exception{
        mockMvc.perform(get("/users/ok")).andDo(print())
                .andExpect(status().isOk());
    }
}