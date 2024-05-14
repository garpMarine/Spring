package com.adobe.authserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    UserDetailsService inMemoryUserDetailsManager() {
        var userBuilder = User.builder();
        UserDetails user = userBuilder.username("banu").
                password("{noop}secret").roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user);
    }

}