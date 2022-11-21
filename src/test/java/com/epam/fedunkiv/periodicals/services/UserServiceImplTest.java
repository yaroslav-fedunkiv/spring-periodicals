package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchUserException;
import com.epam.fedunkiv.periodicals.model.Role;
import com.epam.fedunkiv.periodicals.model.User;
import com.epam.fedunkiv.periodicals.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private final ModelMapper mapper = new ModelMapper();
    private User user;

    @BeforeEach
    void setMockOutput() {
        user = new User();
        user.setId(1l);
        user.setFullName("John Snow");
        user.setRole(Role.CLIENT);
        user.setEmail("john@gmail.com");
        user.setAddress("address");
        user.setBalance(00.00);
        user.setPassword("123456Q@q");
        user.setIsActive(true);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        userService = new UserServiceImpl(userRepository, mapper);
    }

    @DisplayName("JUnit test for findByEmail() method (positive scenario)")
    @Test
    void GetUserByEmail_positiveTest(){
        when(userRepository.findByEmail("john@gmail.com")).thenReturn(user);
        FullUserDto user = userService.getByEmail("john@gmail.com").orElseThrow(NoSuchUserException::new);
        Assertions.assertNotNull(user);
    }

    @DisplayName("JUnit test for findByEmail() method (negative scenario)")
    @Test
    void GetUserByEmail_negativeTest(){
        when(userRepository.findByEmail("johnq@gmail.com")).thenThrow(NoSuchUserException.class);
        Assertions.assertThrows(NoSuchUserException.class, () -> {
            userService.getByEmail("johnq@gmail.com").orElseThrow(NoSuchUserException::new);
        });
    }

    @DisplayName("JUnit test for save() method")
    @Test
    void SaveUser_positiveTest(){
        CreateUserDto createUserDto = new CreateUserDto("CLIENT", "john@gmail.com", "John Snow", "123456Q@q", "123456Q@q");
        User user = mapper.map(createUserDto, User.class);
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findByEmail("john@gmail.com")).thenReturn(user);

        Optional<FullUserDto> savedUser = userService.addUser(createUserDto);
        assertThat(savedUser.orElseThrow().getEmail()).isEqualTo("john@gmail.com");
    }

    @DisplayName("JUnit test for findAll() method")
    @Test
    void FindAllUser_positiveTest(){
        lenient().when(userRepository.findAll()).thenReturn(List.of(user));
        List<FullUserDto> list = userService.getAll();
        Assertions.assertFalse(list.isEmpty());
        assertThat(list.get(0).getEmail()).isEqualTo(user.getEmail());
    }


    @DisplayName("JUnit test for updateUser() method")
    @Test
    void UpdateUser_positiveTest(){ //FIXME: how to test update methods?
        UpdateUserDto updatedUser = new UpdateUserDto();
        updatedUser.setOldEmail(user.getEmail());
        updatedUser.setFullName("Bob Smith");
        updatedUser.setAddress("Lviv, Sadova st. 35");

        lenient().when(userRepository.findByEmail("john@gmail.com")).thenReturn(user);
//        lenient().when(userRepository.updateUser(user.getEmail(), updatedUser.getFullName(), user.getEmail(), updatedUser.getAddress()));
    }

}