package com.epam.fedunkiv.periodicals.controllers;

import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

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
    public ResponseEntity<Object> getByEmail(@PathVariable("email") String email){
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", email + " — such email was not found");
        responseBody.put("status", HttpStatus.NOT_FOUND.value() + " NOT_FOUND");
        return userService.getByEmail(email).isEmpty()
                ? new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(userService.getByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/delete-by/{email}")
    public void deleteUser(@PathVariable("email") String email){
        userService.deleteUser(email);
        log.info("deleted user by email {}", email);
    }
}
