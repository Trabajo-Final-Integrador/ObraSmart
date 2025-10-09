package com.ObraSmart.GestionDeLogin.Config;

import com.ObraSmart.GestionDeLogin.Entity.Role;
import com.ObraSmart.GestionDeLogin.Entity.User;
import com.ObraSmart.GestionDeLogin.Entity.UserStatus;
import com.ObraSmart.GestionDeLogin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (!repo.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("Admin123!"));
            admin.setFirstname("Admin");
            admin.setLastName("System");
            admin.setAddress("Admin Address");
            admin.setPhone("0000000000");
            admin.setEmail("admin@example.com");
            admin.setRole(Role.ADMIN);
            admin.setStatus(UserStatus.ACTIVO);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setCreatedBy("system");

            repo.save(admin);
            System.out.println("Admin creado: admin / Admin123!");
        }
    }
}
