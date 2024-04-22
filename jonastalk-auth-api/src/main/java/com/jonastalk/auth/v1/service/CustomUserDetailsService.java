package com.jonastalk.auth.v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jonastalk.auth.v1.entity.AuthUserAccountListEntity;
import com.jonastalk.auth.v1.repository.AccountListRepository;

/**
 * @name CustomUserDetailsService.java
 * @brief `ACCOUNT_LIST` Dtail Service
 * @author Jonas Lim
 * @date 2023.11.27
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountListRepository userRepository; // Assuming UserRepository handles database interactions

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	AuthUserAccountListEntity user = userRepository.findByUsername(username);
//      .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    	if (user == null) {
    		throw new UsernameNotFoundException("User not found with username: " + username);
    	}

        return org.springframework.security.core.userdetails.User
            .withUsername(username)
            .password(user.getPassword())
            .roles(user.getAuthority().name()) // You can set user roles here
            .build();
    }
    
    public AuthUserAccountListEntity fetchAccountListByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public UserDetails buildCustomUserDetails(String username, String password, String role) {
        return org.springframework.security.core.userdetails.User
            .withUsername(username)
            .password(password)
            .roles(role) // You can set user roles here
            .build();
    }
}
