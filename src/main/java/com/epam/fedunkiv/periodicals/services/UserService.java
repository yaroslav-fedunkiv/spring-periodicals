package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void addUser(CreateUserDto createUserDto);
    List<FullUserDto> getAll();
    Optional<FullUserDto> getByEmail(String email);
    void deleteUser(String email);
    void updateUser(UpdateUserDto updatedUser);
    Double replenishBalance(String newBalance, String email);
    Double writeOffFromBalance(String price, String email);
}
