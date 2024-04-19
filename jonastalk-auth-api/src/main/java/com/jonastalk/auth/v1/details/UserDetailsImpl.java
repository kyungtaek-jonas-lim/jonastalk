package com.jonastalk.auth.v1.details;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jonastalk.auth.v1.entity.AuthUserAccountListEntity;

import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails {

    private final AuthUserAccountListEntity authUserAccountListEntity;

    public UserDetailsImpl(AuthUserAccountListEntity authUserAccountListEntity) {
        this.authUserAccountListEntity = authUserAccountListEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(authUserAccountListEntity.getAuthority().name()));
    }

    @Override
    public String getPassword() {
        return authUserAccountListEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return authUserAccountListEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Implement your logic for account expiration
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Implement your logic for account locking
        return authUserAccountListEntity.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Implement your logic for credentials expiration
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Implement your logic for account enabling
        return authUserAccountListEntity.isEnabled();
    }
}