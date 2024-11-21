package org.themullers.vocab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public UserDetailsService userDetailsService() {

        List<UserDetails> users = new ArrayList<>();

        UserDetails mike = User.builder()
                .username("mike")
                .password("{noop}dog")
                .roles("USER")
                .build();

        UserDetails maddie = User.builder()
                .username("maddie")
                .password("{noop}penguin")
                .roles("USER")
                .build();

        users.add(mike);
        users.add(maddie);

        return new InMemoryUserDetailsManager(users);
    }

}
