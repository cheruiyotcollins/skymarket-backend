package com.gigster.skymarket.dto;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
        private String email;
        private String username;
        private Set<RoleName> roles;

        public UserDto(User user) {
            this.email = user.getEmail();
            this.username = user.getUsername();
            this.roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
        }




}

