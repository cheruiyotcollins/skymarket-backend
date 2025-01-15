package com.gigster.skymarket.unit.mapper;

import com.gigster.skymarket.dto.MyUserDto;
import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.mapper.UserMapper;
import com.gigster.skymarket.model.MyUser;
import com.gigster.skymarket.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MyUserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void testToDto() {
        // Arrange
        MyUser myUser = new MyUser();
        myUser.setEmail("test@example.com");
        myUser.setUsername("testuser");

        Role role1 = new Role(1L, RoleName.ROLE_SUPER_ADMIN);
        Role role2 = new Role(2L, RoleName.ROLE_ADMIN);
        Role role3 = new Role(3L, RoleName.ROLE_CUSTOMER);
        myUser.setRoles(Set.of(role1, role2, role3));

        // Act
        MyUserDto myUserDto = userMapper.toDto(myUser);

        // Assert
        assertThat(myUserDto).isNotNull();
        assertThat(myUserDto.getEmail()).isEqualTo(myUser.getEmail());
        assertThat(myUserDto.getUsername()).isEqualTo(myUser.getUsername());
        assertThat(myUserDto.getRoles()).containsExactlyInAnyOrder(RoleName.ROLE_SUPER_ADMIN, RoleName.ROLE_ADMIN, RoleName.ROLE_CUSTOMER);
    }

    @Test
    void testToEntity() {
        // Arrange
        MyUserDto myUserDto = new MyUserDto();
        myUserDto.setEmail("test@example.com");
        myUserDto.setUsername("tester");
        myUserDto.setRoles(Set.of(RoleName.ROLE_SUPER_ADMIN, RoleName.ROLE_ADMIN, RoleName.ROLE_CUSTOMER));

        // Act
        MyUser myUser = userMapper.toEntity(myUserDto);

        // Assert
        assertThat(myUser).isNotNull();
        assertThat(myUser.getEmail()).isEqualTo(myUserDto.getEmail());
        assertThat(myUser.getUsername()).isEqualTo(myUserDto.getUsername());
        assertThat(myUser.getRoles())
                .extracting(Role::getName)
                .containsExactlyInAnyOrder(RoleName.ROLE_SUPER_ADMIN, RoleName.ROLE_ADMIN, RoleName.ROLE_CUSTOMER);
    }
}
