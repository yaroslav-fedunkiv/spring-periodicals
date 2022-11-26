package com.epam.fedunkiv.periodicals.controllers;

import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchUserException;
import com.epam.fedunkiv.periodicals.services.UserService;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static groovy.json.JsonOutput.toJson;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
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
    void GetAllUsers_positiveTest() throws Exception{
        lenient().when(userService.getAll()).thenReturn(List.of(user));
        mockMvc.perform(get("/users/get-all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("2")))
                .andExpect(jsonPath("$[0].fullName", is("John Snow")))
                .andExpect(jsonPath("$[0].email", is("john@gmail.com")))
                .andExpect(jsonPath("$[0].role", is("CLIENT")));
        verify(userService, times(1)).getAll();
    }

    @Test
    void GetByEmail_positiveTest() throws Exception{
        lenient().when(userService.getByEmail("john@gmail.com")).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/get-by/john@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("2")))
                .andExpect(jsonPath("$.fullName", is("John Snow")))
                .andExpect(jsonPath("$.email", is("john@gmail.com")))
                .andExpect(jsonPath("$.role", is("CLIENT")));
        verify(userService, times(1)).getByEmail("john@gmail.com");
    }

    @Test
    void GetByEmail_negativeTest() throws Exception{
        lenient().when(userService.getByEmail("johnq@gmail.com")).thenThrow(NoSuchUserException.class);
        mockMvc.perform(get("/users/get-by/johnq@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("johnq@gmail.com — the user with such email was not found"));
        verify(userService, times(1)).getByEmail("johnq@gmail.com");
    }

    @Test
    void CreateUser_positiveTest() throws Exception{
        CreateUserDto createUserDto = new CreateUserDto("CLIENT", "john@gmail.com", "John", "123456Q@q", "123456Q@q");
        when(userService.addUser(createUserDto)).thenReturn(Optional.of(user));
        when(userService.getByEmail("john@gmail.com")).thenThrow(NoSuchUserException.class);

        mockMvc.perform(post("/users/create")
                        .content(toJson(createUserDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(createUserDto.getEmail() + ": user was created"));
        verify(userService, times(1)).getByEmail("john@gmail.com");
    }

    @Test
    void CreateUser_checkValidation_negativeTest() throws Exception{
        CreateUserDto createUserDto = new CreateUserDto("CLIENT", "john@gmail.com", null, "123456", "123456Q@q");
        when(userService.addUser(createUserDto)).thenReturn(Optional.of(user));
        when(userService.getByEmail("john@gmail.com")).thenReturn(Optional.of(user));
        mockMvc.perform(post("/users/create")
                        .content(toJson(createUserDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem("Such email address is already exist")))
                .andExpect(jsonPath("$.errors", hasItem("Field full name can't be empty")))
                .andExpect(jsonPath("$.fields", hasSize(3)))
                .andExpect(jsonPath("$.fields", hasItem("email")))
                .andExpect(jsonPath("$.fields", hasItem("fullName")))
                .andExpect(jsonPath("$.fields", hasItem("password")));
        verify(userService, times(1)).getByEmail("john@gmail.com");
        verify(userService, times(0)).addUser(createUserDto);
    }

    @Test
    void ReplenishUserBalance_positiveTest() throws Exception{
        user.setBalance("546.33");
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setBalance("546.33");
        when(userService.replenishBalance(updateUserDto.getBalance(), "john@gmail.com")).thenReturn(user);
        when(userService.getByEmail("john@gmail.com")).thenReturn(Optional.of(user));

        mockMvc.perform(patch("/users/replenish-balance/{email}", "john@gmail.com")
                        .content(toJson(updateUserDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("john@gmail.com — balance of this user was replenished"));
        verify(userService, times(1)).replenishBalance(updateUserDto.getBalance(), "john@gmail.com");
    }

    @Test
    void ReplenishUserBalance_checkValidation_negativeTest() throws Exception{
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setBalance("-546.33");

        mockMvc.perform(patch("/users/replenish-balance/{email}", "john@gmail.com")
                        .content(toJson(updateUserDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("balance must be positive")))
                .andExpect(jsonPath("$.fields", hasSize(1)))
                .andExpect(jsonPath("$.fields", hasItem("balance")));
        verify(userService, times(0)).replenishBalance(updateUserDto.getBalance(), "john@gmail.com");
    }

    @Test
    void ReplenishUserBalance_negativeTest() throws Exception{
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setBalance("546.33");
        when(userService.replenishBalance(updateUserDto.getBalance(), "johnq@gmail.com")).thenThrow(NoSuchElementException.class);

        mockMvc.perform(patch("/users/replenish-balance/{email}", "johnq@gmail.com")
                        .content(toJson(updateUserDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("404 NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("johnq@gmail.com — such email was not found")));
        verify(userService, times(1)).replenishBalance(updateUserDto.getBalance(), "johnq@gmail.com");
    }

    @Test
    void UpdateUser_negativeTest() throws Exception{//fixme
        UpdateUserDto updateUserDto = new UpdateUserDto();
        when(userService.updateUser(updateUserDto, "johnq@gmail.com")).thenThrow(NoSuchUserException.class);
        when(userService.getByEmail("johnq@gmail.com")).thenThrow(NoSuchUserException.class);

        mockMvc.perform(patch("/users/update/johnq@gmail.com")
                        .content(toJson(updateUserDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with such email doesn't exist johnq@gmail.com"));
    }

    @Test
    void UpdateUser_positiveTest() throws Exception{
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setAddress("Lviv, Sadova st. 25");
        updateUserDto.setFullName("Joe Biden");
        user.setAddress("Lviv, Sadova st. 25");
        user.setFullName("Joe Biden");
        when(userService.updateUser(updateUserDto, "john@gmail.com")).thenReturn(updateUserDto);

        mockMvc.perform(patch("/users/update/{email}", "johnq@gmail.com")
                        .content(toJson(updateUserDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User johnq@gmail.com was updated"));
    }

    @Test
    void DeactivateUser_negativeTest() throws Exception{
        when(userService.isActive(user.getEmail())).thenReturn(false);

        mockMvc.perform(delete("/users/deactivate/{email}", user.getEmail()))
                .andExpect(status().isConflict())
                .andExpect(content().string(user.getEmail() + " – user is already deactivated"));
    }

    @Test
    void DeactivateUser_positiveTest() throws Exception{
        when(userService.deactivateUser(user.getEmail())).thenReturn(user);
        when(userService.isActive(user.getEmail())).thenReturn(true);

        mockMvc.perform(delete("/users/deactivate/"+user.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string(user.getEmail() + " – user was deactivated"));
    }

    @Test
    void DeactivateUser_checkEmail_negativeTest() throws Exception{
        when(userService.isActive("johnq@gmail.com")).thenThrow(NoSuchUserException.class);

        mockMvc.perform(delete("/users/deactivate/{email}", "johnq@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("johnq@gmail.com – user with such email doesn't exist"));
    }
}