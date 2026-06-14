package com.example.projekt_sklep.config;

import com.example.projekt_sklep.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;

    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http)
            throws Exception {

        http

                .userDetailsService(userDetailsService)

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/login",
                                "/register",
                                "/register/save",
                                "/public/**",
                                "/css/**"
                        )
                        .permitAll()

                        .requestMatchers("/users/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/categories/add",
                                "/categories/save",
                                "/categories/edit/**",
                                "/categories/update",
                                "/categories/delete/**"
                        )
                        .hasAnyRole("ADMIN", "EMPLOYEE")

                        .anyRequest()
                        .authenticated()

                )

                .formLogin(form -> form

                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()

                )

                .logout(logout -> logout

                        .logoutSuccessUrl("/login?logout")
                        .permitAll()

                );

        return http.build();

    }

}
