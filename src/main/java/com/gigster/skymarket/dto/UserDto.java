package com.gigster.skymarket.dto;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
        private String email;
        private String username;
        private RoleName role;

        public UserDto(User user) {
            this.email = user.getEmail();
            this.username = user.getUsername();
            this.role = user.getRole().getName();
        }

}

