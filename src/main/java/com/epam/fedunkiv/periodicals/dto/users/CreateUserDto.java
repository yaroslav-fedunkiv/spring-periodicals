package com.epam.fedunkiv.periodicals.dto.users;

import com.epam.fedunkiv.periodicals.validation.ExistedEmail;
import com.epam.fedunkiv.periodicals.validation.PasswordMatch;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@PasswordMatch(message = "{user.confirmPassword}")
public class CreateUserDto {
    @Pattern(regexp = "(^CLIENT$)?(^ADMIN$)?",
    message = "role must be 'CLIENT'")
    private String role;
    @ExistedEmail(message = "{user.existed.email}")//√
    @Email(message = "{user.wrong.email}")//√
    private String email;
    @NotBlank(message = "{user.empty.fullName}")//√
    private String fullName;
    @Pattern(regexp = "(^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=\"])(?=\\S+$).{8,}$)|(^(?=.*\\d)(?=.*[а-я])(?=.*[А-Я])(?=.*[@#$%^&+=\"])(?=\\S+$).{8,}$)",
    message = "{user.wrong.password}")//√
    private String password;
    private String confirmPassword;
}
