package com.example.projekt_sklep.config;

import com.example.projekt_sklep.model.User;
import com.example.projekt_sklep.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessionListener implements HttpSessionListener {

    private final UserRepository userRepository;

    public SessionListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {}

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        try {
            SecurityContext context = (SecurityContext)
                    session.getAttribute("SPRING_SECURITY_CONTEXT");
            if (context == null || context.getAuthentication() == null) return;

            String login = context.getAuthentication().getName();
            User user = userRepository.findByLogin(login);
            if (user != null) {
                user.setOstatnieLogowanie(LocalDateTime.now());
                userRepository.save(user);
            }
        } catch (Exception ignored) {}
    }
}
