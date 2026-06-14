package com.example.projekt_sklep.controller;

import com.example.projekt_sklep.model.User;
import com.example.projekt_sklep.repository.UserRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register/save")
    public String saveUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        if (userRepository.findByLogin(user.getLogin()) != null) {
            result.rejectValue("login", "duplicate", "Ten login jest już zajęty");
            return "register";
        }

        user.setRola("USER");
        user.setHaslo(passwordEncoder.encode(user.getHaslo()));
        userRepository.save(user);

        return "redirect:/login";
    }
}
