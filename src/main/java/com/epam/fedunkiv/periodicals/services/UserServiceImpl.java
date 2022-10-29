package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.model.User;
import com.epam.fedunkiv.periodicals.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private ModelMapper mapper;

    @Override
    public void addUser(CreateUserDto createUserDto) {
        userRepository.save(mapper.map(createUserDto, User.class));
        log.info("added new user");
    }

    @Override
    public List<FullUserDto> getAll() {
        log.info("start method getAll() in user service");
        return userRepository.findAll().stream()
                .map(e -> mapper.map(e, FullUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FullUserDto> getByEmail(String email) {
        log.info("start method getByEmail() in user service {}", email);
        try {
            User user = userRepository.findByEmail(email);
            return Optional.of(mapper.map(user, FullUserDto.class));
        } catch (Exception e) {
            log.info("This email was not found {}", email);
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserDto updatedUser) {
        FullUserDto fullUserDto = getByEmail(updatedUser.getOldEmail()).get();
        CreateUserDto editUser = new CreateUserDto();
        editUser.setEmail(updatedUser.getEmail() == null ? updatedUser.getOldEmail() : updatedUser.getEmail());
        editUser.setFullName(updatedUser.getFullName() == null ? fullUserDto.getFullName() : updatedUser.getFullName());
        editUser.setPassword(updatedUser.getPassword() == null ? fullUserDto.getPassword() : updatedUser.getPassword());

        log.info("start updating user");
        deleteUser(updatedUser.getOldEmail());
        addUser(editUser);
        log.info("edited user {}", editUser);
    }

    @Override
    @Transactional
    public Double replenishBalance(String newBalance, String email) {
        log.info("start replenish the balance {}", email);
        FullUserDto user = getByEmail(email).get();
        Double balance = Double.parseDouble(newBalance) + Double.parseDouble(user.getBalance());
        userRepository.updateBalance(balance, email);
        return balance;
    }

    @Override
    @Transactional
    public Double writeOffFromBalance(String price, String email) {
        log.info("start replenish the balance {}", email);
        FullUserDto user = getByEmail(email).get();
        Double balance = Double.parseDouble(user.getBalance()) - Double.parseDouble(price);
        userRepository.updateBalance(round(balance), email);
        return balance;
    }

    private Double round(Double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
        log.info("user is deleted by email {}", email);
    }
}
