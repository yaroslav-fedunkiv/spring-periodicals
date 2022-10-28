package com.epam.fedunkiv.periodicals.controllers;

import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/create")
    public void createUser(@Valid @RequestBody CreateUserDto createUserDto){
        userService.addUser(createUserDto);
        log.info("user was register {}", createUserDto.getEmail());
    }

    @PostMapping("/replenish-balance/{email}")
    public void replenish(@Valid @RequestBody UpdateUserDto user, @Valid @PathVariable("email") String email){
        userService.replenishBalance(user.getBalance(), email);
        log.info("replenished balance {}", user.getBalance());
    }

    @PostMapping("/update")
    public void updateUser(@Valid @RequestBody UpdateUserDto user){
        userService.updateUser(user);
        log.info("user was update {}", user.getOldEmail());
    }

    @GetMapping("/get-all")
    public List<FullUserDto> getAllUsers(){
        List<FullUserDto> list = userService.getAll();
        log.info("got all users");
        return list;
    }

    @GetMapping("/get-by/{email}")
    public Optional<FullUserDto> getByEmail(@PathVariable("email") String email){
        FullUserDto fullUserDto = new FullUserDto();
            if (userService.getByEmail(email).isEmpty()){
                return Optional.of(fullUserDto);
            } else {
                log.info("got user by email {}", email);
                return userService.getByEmail(email);
            }
    }

    @GetMapping("/delete-by/{email}")
    public void deleteUser(@PathVariable("email") String email){
        userService.deleteUser(email);
        log.info("deleted user by email {}", email);
    }
}
