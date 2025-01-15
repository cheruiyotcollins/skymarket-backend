package com.gigster.skymarket.unit.mapper;

import com.gigster.skymarket.dto.UserDto;
import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.mapper.UserMapper;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void testToDto() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");

        Role role1 = new Role(1L, RoleName.ROLE_SUPER_ADMIN);
        Role role2 = new Role(2L, RoleName.ROLE_ADMIN);
        Role role3 = new Role(3L, RoleName.ROLE_CUSTOMER);
        user.setRoles(Set.of(role1, role2, role3));

        // Act
        UserDto userDto = userMapper.toDto(user);

        // Assert
        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
        assertThat(userDto.getRoles()).containsExactlyInAnyOrder(RoleName.ROLE_SUPER_ADMIN, RoleName.ROLE_ADMIN, RoleName.ROLE_CUSTOMER);
    }

    @Test
    void testToEntity() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("tester");
        userDto.setRoles(Set.of(RoleName.ROLE_SUPER_ADMIN, RoleName.ROLE_ADMIN, RoleName.ROLE_CUSTOMER));

        // Act
        User user = userMapper.toEntity(userDto);

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(user.getRoles())
                .extracting(Role::getName)
                .containsExactlyInAnyOrder(RoleName.ROLE_SUPER_ADMIN, RoleName.ROLE_ADMIN, RoleName.ROLE_CUSTOMER);
    }
}
