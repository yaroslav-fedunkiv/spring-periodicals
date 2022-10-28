package com.epam.fedunkiv.periodicals.dto.users;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FullUserDto {
    private String id;
    private String fullName;
    private String role;
    private String email;
    private String balance;
    private String isActive;
    private String password;
    private String created;
    private String updated;
}
