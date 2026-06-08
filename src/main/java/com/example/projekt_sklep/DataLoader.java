package com.example.projekt_sklep;

import com.example.projekt_sklep.model.User;
import com.example.projekt_sklep.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void run(String... args) {

        if(userRepository.count() == 0){

            User admin = new User();
            admin.setImie("Jan");
            admin.setNazwisko("Kowalski");
            admin.setLogin("admin");
            admin.setHaslo(
                    passwordEncoder.encode("admin")
            );
            admin.setWiek(25);
            admin.setRola("ADMIN");

            User user = new User();
            user.setImie("Adam");
            user.setNazwisko("Nowak");
            user.setLogin("user");
            user.setHaslo(
                    passwordEncoder.encode("user")
            );
            user.setWiek(22);
            user.setRola("USER");

            User employee = new User();
            employee.setImie("Karol");
            employee.setNazwisko("Wisniewski");
            employee.setLogin("employee");
            employee.setHaslo(
                    passwordEncoder.encode("employee")
            );
            employee.setWiek(30);
            employee.setRola("EMPLOYEE");

            userRepository.save(admin);
            userRepository.save(user);
            userRepository.save(employee);

        }

    }

}