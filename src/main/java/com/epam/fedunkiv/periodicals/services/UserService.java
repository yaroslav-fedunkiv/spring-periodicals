package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NotEnoughMoneyException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<FullUserDto> addUser(CreateUserDto createUserDto);
    List<FullUserDto> getAll();
    Optional<FullUserDto> getByEmail(String email);
    FullUserDto deactivateUser(String email);
    UpdateUserDto updateUser(UpdateUserDto updatedUser, String email);
    FullUserDto replenishBalance(String newBalance, String email);
    FullUserDto writeOffFromBalance(String price, String email) throws NotEnoughMoneyException;
    boolean isActive(String email);

//    void testUpdateUserMethod(UpdateUserDto updatedUser, String email);
}
