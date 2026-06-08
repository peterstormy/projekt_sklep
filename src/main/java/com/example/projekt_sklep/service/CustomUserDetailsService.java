package com.example.projekt_sklep.service;

import com.example.projekt_sklep.model.User;
import com.example.projekt_sklep.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        User user =
                userRepository.findByLogin(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "Brak użytkownika"
            );
        }

        return new org.springframework.security.core.userdetails.User(

                user.getLogin(),

                user.getHaslo(),

                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRola()
                        )
                )

        );
    }
}