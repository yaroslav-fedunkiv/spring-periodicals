package com.epam.fedunkiv.periodicals.dto.users;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FullUserDto {
    @ApiModelProperty(notes = "User id", example = "3")
    private String id;
    @ApiModelProperty(notes = "User full name", example = "Joe Biden")
    private String fullName;
    @ApiModelProperty(notes = "User role", example = "CLIENT")
    private String role;
    @ApiModelProperty(notes = "User email", example = "joe@gmail.com")
    private String email;
    @ApiModelProperty(notes = "User balance", example = "333.25")
    private String balance;

    private String address;
    @ApiModelProperty(notes = "User status", example = "false")
    private String isActive;
    @ApiModelProperty(notes = "User password", example = "123456Q@q")
    private String password;
    @ApiModelProperty(notes = "User created date", example = "2022-10-31T11:46:11")
    private String created;
    @ApiModelProperty(notes = "User updated date", example = "2022-10-31T11:46:11")
    private String updated;
}
