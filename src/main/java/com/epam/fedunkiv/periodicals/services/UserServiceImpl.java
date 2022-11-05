package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchUserException;
import com.epam.fedunkiv.periodicals.exceptions.NotEnoughMoneyException;
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
        log.info("start method addUser() in userService: " + createUserDto.getEmail());
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
        } catch (IllegalArgumentException e) {
            log.info("This email was not found {}", email);
            throw new NoSuchUserException();
        }
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserDto updatedUser) {
        FullUserDto fullUserDto = null;
        try {
        fullUserDto = getByEmail(updatedUser.getOldEmail()).get();
        } catch (IllegalArgumentException e) {
            log.info("This user {} was not found ", updatedUser.getOldEmail());
            throw new NoSuchUserException();
        }
        CreateUserDto editUser = new CreateUserDto();
        String oldEmail = fullUserDto.getEmail();
        String newEmail = updatedUser.getEmail() == null
                ? updatedUser.getOldEmail() : updatedUser.getEmail();
        String fullName = updatedUser.getFullName() == null
                ? fullUserDto.getFullName() : updatedUser.getFullName();
        String address = updatedUser.getAddress() == null
                ? fullUserDto.getAddress() : updatedUser.getAddress();
        log.warn("User "+oldEmail+" was updated with fields:\n"
                + fullName +"\n"+ newEmail +"\n"+ address);

        userRepository.updateUser(oldEmail, fullName, newEmail, address);
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
    public Double writeOffFromBalance(String price, String email) throws NotEnoughMoneyException{
        log.info("start replenish the balance {}", email);
        FullUserDto user = getByEmail(email).get();
        if (Double.parseDouble(price) > Double.parseDouble(getByEmail(email).get().getBalance())){
            throw new NotEnoughMoneyException("Not enough money");
        }
        Double balance = Double.parseDouble(user.getBalance()) - Double.parseDouble(price);
        userRepository.updateBalance(round(balance), email);
        return balance;
    }

    @Override
    @Transactional
    public void deactivateUser(String email) { //fixme make try/catch block
        userRepository.deactivateUser(email);
        log.info("user {} is deactivated", email);
    }

    @Override
    public boolean isActive(String email){
        log.info("check if user with such email {} is active", email);
        return Boolean.parseBoolean(getByEmail(email).get().getIsActive());
    }

    private Double round(Double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
