package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchUserException;
import com.epam.fedunkiv.periodicals.exceptions.NotEnoughMoneyException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    void init() {
        user = new User(1L, "John Snow", Role.ROLE_CLIENT, "john@gmail.com", "address", null, 00.00,
                "123456Q@q", true, LocalDateTime.now(), LocalDateTime.now());
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
    void UpdateUser_positiveTest(){
        lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UpdateUserDto editUserDto = new UpdateUserDto();
        editUserDto.setFullName("Bob Smith");
        editUserDto.setAddress("Lviv, Sadova st. 35");

        UpdateUserDto updatedUserDto = userService.updateUser(editUserDto, user.getEmail());

        assertThat(updatedUserDto.getFullName()).isEqualTo("Bob Smith");
        assertThat(updatedUserDto.getAddress()).isEqualTo("Lviv, Sadova st. 35");
    }

    @DisplayName("JUnit test for replenishBalance() method")
    @Test
    void ReplenishUserBalance_positiveTest(){
        lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        FullUserDto userDto = userService.replenishBalance("235.56", user.getEmail());

        assertThat(userDto.getBalance()).isEqualTo("235.56");
    }

    @DisplayName("JUnit test for writeOffFromBalance() method")
    @Test
    void WriteOffFromUserBalance_positiveTest(){
        user.setBalance(200.25);
        lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        FullUserDto userDto = userService.writeOffFromBalance("100.10", user.getEmail());

        assertThat(userDto.getBalance()).isEqualTo("100.15");
    }

    @DisplayName("JUnit test for writeOffFromBalance() method (negative scenario)")
    @Test
    void WriteOffFromUserBalance_negativeTest(){
        lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        Assertions.assertThrows(NotEnoughMoneyException.class, () -> {
            userService.writeOffFromBalance("100.10", user.getEmail());
        });
    }

    @DisplayName("JUnit test for deactivateUser() method (positive scenario)")
    @Test
    void DeactivateUser_positiveTest(){
        lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        FullUserDto userDto = userService.deactivateUser(user.getEmail());
        assertThat(userDto.getIsActive()).isEqualTo("false");
    }

    @DisplayName("JUnit test for isActive() method")
    @Test
    void IsActiveUser_positiveTest(){
        lenient().when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        assertThat(userService.isActive(user.getEmail())).isTrue();
    }
}