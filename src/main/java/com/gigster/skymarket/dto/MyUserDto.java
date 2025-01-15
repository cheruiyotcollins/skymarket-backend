package com.gigster.skymarket.dto;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.MyUser;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyUserDto {
    private String email;
    private String username;
    private Set<RoleName> roles;

    public MyUserDto(MyUser myUser) {
        this.email = myUser.getEmail();
        this.username = myUser.getUsername();
        this.roles = myUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
