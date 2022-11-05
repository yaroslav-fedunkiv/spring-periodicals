package com.epam.fedunkiv.periodicals.validation;

import com.epam.fedunkiv.periodicals.exceptions.NoSuchUserException;
import com.epam.fedunkiv.periodicals.repositories.UserRepository;
import com.epam.fedunkiv.periodicals.services.UserService;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Log4j2
public class ExistedUserValidator implements
        ConstraintValidator<ExistedUser, String> {
    @Resource
    private UserRepository userRepository;

    @Resource
    private UserService userService;

    @Override
    public void initialize(ExistedUser constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx){
//        return false;
        int res = 0;
        try{
            log.error("error in try block with email: {}", s);
            userService.getByEmail(s);
            return true;
        }catch (NoSuchUserException e){
            log.error("error in catch block with email: {}", s);
            return false;
        }
//        catch (NullPointerException e){
//            return false;
//        }
//        return !userService.getByEmail(s).isEmpty();
    }
}
