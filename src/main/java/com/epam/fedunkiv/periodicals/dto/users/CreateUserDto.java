package com.epam.fedunkiv.periodicals.dto.users;

import com.epam.fedunkiv.periodicals.customValidation.ExistedEmail;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CreateUserDto {
    @Pattern(regexp = "(^CLIENT$)?(^ADMIN$)?",
    message = "role must be 'CLIENT'")
    private String role;
    @ExistedEmail
    @Email(message = "your entered email address isn't valid")
    private String email;
    @NotBlank(message = "fullName field can't be empty")
    private String fullName;
    @Pattern(regexp = "(^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=\"])(?=\\S+$).{8,}$)|(^(?=.*\\d)(?=.*[а-я])(?=.*[А-Я])(?=.*[@#$%^&+=\"])(?=\\S+$).{8,}$)",
    message = "{wrong.password}")
    private String password;

}
