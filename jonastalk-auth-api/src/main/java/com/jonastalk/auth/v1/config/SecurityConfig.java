package com.jonastalk.auth.v1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jonastalk.auth.v1.component.CustomPasswordEncoder;
import com.jonastalk.auth.v1.component.HeaderBasedAuthenticationProvider;
import com.jonastalk.auth.v1.component.HeaderBasedRequestFilter;
import com.jonastalk.auth.v1.component.JwtAuthenticationEntryPoint;

/**
 * @name SecurityConfig.java
 * @brief Spring Security Config
 * @author Jonas Lim
 * @date 2023.11.27
 */
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;
    
    @Autowired
    private HeaderBasedAuthenticationProvider headerBasedAuthenticationProvider;

    @Autowired
    private HeaderBasedRequestFilter headerBasedRequestFilter;

    /**
     * @name authenticationManager(HttpSecurity http)
     * @brief Select HTTP `Authentication` Provider
     * @process CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
     * @author Jonas Lim
     * @date 2023.11.25
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(jwtUserDetailsService).passwordEncoder(customPasswordEncoder);
        authenticationManagerBuilder.authenticationProvider(headerBasedAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    
    /**
     * @name filterChain(HttpSecurity http)
     * @brief HTTP `Authentication` or `Authorization` (Authentication Method and Authentication Process)
     * @author Jonas Lim
     * @date 2023.11.25
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf().disable() // Doesn't save authentication info in this server
            ;
        
        httpSecurity
        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
        ;
        
        httpSecurity
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Authentication with JWT, not with session
        ;
        
        httpSecurity.addFilterBefore(headerBasedRequestFilter, UsernamePasswordAuthenticationFilter.class);

    	return httpSecurity.build();
    }
}