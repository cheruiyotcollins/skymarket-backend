package com.gigster.skymarket.dto;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.User;
import lombok.*;

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

