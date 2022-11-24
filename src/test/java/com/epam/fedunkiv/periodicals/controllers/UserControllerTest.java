package com.epam.fedunkiv.periodicals.controllers;

import com.epam.fedunkiv.periodicals.PeriodicalsApplication;
import com.epam.fedunkiv.periodicals.model.Role;
import com.epam.fedunkiv.periodicals.model.User;
import com.epam.fedunkiv.periodicals.repositories.UserRepository;
import com.epam.fedunkiv.periodicals.services.UserService;
import com.epam.fedunkiv.periodicals.services.UserServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = PeriodicalsApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    private static final ModelMapper mapper = new ModelMapper();
    @Autowired
    private MockMvc mockMvc;

//    @MockBean
    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

//    @InjectMocks
//    private UserService userService = new UserServiceImpl(userRepository, mapper);
    private User user;

    @BeforeEach
    void init() {
        user = new User(2L, "John Snow", Role.CLIENT, "john@gmail.com", "address", null, 00.00,
                "123456Q@q", true, LocalDateTime.now(), LocalDateTime.now());
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    }
    @Test
    void GetAllUsers() throws Exception{
        lenient().when(userRepository.findAll()).thenReturn(List.of(user));
        mockMvc.perform(get("/users/get-all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].fullName", is("John Snow")));
    }

    @Test
    void GetByEmail() throws Exception{
        lenient().when(userRepository.findByEmail("john@gmail.com")).thenReturn(user);
        mockMvc.perform(get("/users/get-by/john@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2L)))
                .andExpect(jsonPath("$.fullName", is("John Snow")))
                .andExpect(jsonPath("$.email", is("john@gmail.com")));

//        mockMvc.perform(get("/users/ok")).andDo(print())
//                .andExpect(status().isOk());
    }
}