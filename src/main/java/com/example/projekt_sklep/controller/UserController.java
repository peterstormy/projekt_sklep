package com.example.projekt_sklep.controller;


import com.example.projekt_sklep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.projekt_sklep.model.User;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String showUsers(Model model){

        model.addAttribute("users",
                userRepository.findAll());

        return "users";
    }

    @GetMapping("/users/editRole/{id}")
    public String editRole(@PathVariable Long id,
                           Model model){

        model.addAttribute("user",
                userRepository.findById(id).orElse(null));

        return "editRole";
    }

    @PostMapping("/users/updateRole")
    public String updateRole(User user){

        User oldUser =
                userRepository.findById(user.getId()).orElse(null);

        if(oldUser != null){
            oldUser.setRola(user.getRola());
            userRepository.save(oldUser);
        }

        return "redirect:/users";
    }
}