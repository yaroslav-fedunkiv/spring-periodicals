package com.epam.fedunkiv.periodicals.dto.users;

import com.epam.fedunkiv.periodicals.validation.ExistedEmail;
import com.epam.fedunkiv.periodicals.validation.PasswordMatch;
import io.swagger.annotations.ApiModelProperty;
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
//    @ApiModelProperty(notes = "User role. By default: 'CLIENT'", example = "CLIENT", required = true)
    @Pattern(regexp = "(^CLIENT$)?(^ADMIN$)?", message = "role must be 'CLIENT'")
    private String role;

//    @ApiModelProperty(notes = "User email", example = "joe@gmail.com", required = true)
    @ExistedEmail(message = "{user.existed.email}")//√
    @NotBlank(message = "{user.empty.email}")//√
    @Email(message = "{user.wrong.email}")//√
    private String email;

//    @ApiModelProperty(notes = "User full name", example = "Joe Biden", required = true)
    @NotBlank(message = "{user.empty.fullName}")//√
    private String fullName;

    @ApiModelProperty(notes = "User password", example = "123456Q@q", required = true)
    @Pattern(regexp = "(^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=\"])(?=\\S+$).{8,}$)|(^(?=.*\\d)(?=.*[а-я])(?=.*[А-Я])(?=.*[@#$%^&+=\"])(?=\\S+$).{8,}$)",
    message = "{user.wrong.password}")//√
    private String password;

//    @ApiModelProperty(notes = "Password confirmation", example = "123456Q@q", required = true)
    private String confirmPassword;
}
