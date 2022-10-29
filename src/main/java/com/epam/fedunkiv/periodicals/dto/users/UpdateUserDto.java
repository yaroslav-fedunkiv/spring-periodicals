package com.epam.fedunkiv.periodicals.dto.users;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UpdateUserDto {
    @Email(message = "your oldEmail address isn't valid")
    private String oldEmail;
    @Email(message = "{user.wrong.email}")
    private String email;
    private String fullName;
    @Pattern(regexp = "(^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=\"])(?=\\S+$).{8,}$)|(^(?=.*\\d)(?=.*[а-я])(?=.*[А-Я])(?=.*[@#$%^&+=\"])(?=\\S+$).{8,}$)",
            message = "{user.wrong.password}")
    private String password;
    @Pattern(regexp = "^\\d{1,5}\\.\\d{2}", message = "{user.wrong.balance}")
    private String balance;
}
