package com.gigster.skymarket.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gigster.skymarket.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    @EqualsAndHashCode.Include
    private final Long id;

    private final String name;
    private final String username;
    private final String email;
    private final String phoneNo;

    @JsonIgnore
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getContact(),
                user.getPassword(),
                mapRolesToAuthorities(user)
        );
    }

    private static List<GrantedAuthority> mapRolesToAuthorities(User user) {
        return List.of(new SimpleGrantedAuthority(user.getRole().getName().name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
