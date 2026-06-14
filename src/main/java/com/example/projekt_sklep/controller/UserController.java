package com.example.projekt_sklep.controller;

import com.example.projekt_sklep.model.Order;
import com.example.projekt_sklep.model.User;
import com.example.projekt_sklep.model.UserProfileForm;
import com.example.projekt_sklep.repository.OrderRepository;
import com.example.projekt_sklep.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/users/editRole/{id}")
    public String editRole(@PathVariable Long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).orElse(null));
        return "editRole";
    }

    @PostMapping("/users/updateRole")
    public String updateRole(User user) {
        User oldUser = userRepository.findById(user.getId()).orElse(null);
        if (oldUser != null) {
            oldUser.setRola(user.getRola());
            userRepository.save(oldUser);
        }
        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            List<Order> orders = orderRepository.findByUser(user);
            for (Order o : orders) {
                o.setUser(null);
            }
            orderRepository.saveAll(orders);
            userRepository.delete(user);
        }
        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String showProfile(Principal principal, Model model) {
        User user = userRepository.findByLogin(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(Principal principal, Model model) {
        User user = userRepository.findByLogin(principal.getName());
        UserProfileForm form = new UserProfileForm();
        form.setImie(user.getImie());
        form.setNazwisko(user.getNazwisko());
        form.setWiek(user.getWiek());
        model.addAttribute("form", form);
        return "editProfile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@Valid @ModelAttribute("form") UserProfileForm form,
                                BindingResult result,
                                Principal principal) {
        if (result.hasErrors()) {
            return "editProfile";
        }
        User user = userRepository.findByLogin(principal.getName());
        user.setImie(form.getImie());
        user.setNazwisko(form.getNazwisko());
        user.setWiek(form.getWiek());
        userRepository.save(user);
        return "redirect:/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteOwnAccount(Principal principal, HttpServletRequest request) throws Exception {
        User user = userRepository.findByLogin(principal.getName());
        if (user != null) {
            List<Order> orders = orderRepository.findByUser(user);
            for (Order o : orders) {
                o.setUser(null);
            }
            orderRepository.saveAll(orders);
            userRepository.delete(user);
            request.logout();
        }
        return "redirect:/login?logout";
    }
}
