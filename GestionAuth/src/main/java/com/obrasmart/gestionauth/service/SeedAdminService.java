package com.obrasmart.gestionauth.service;

import com.obrasmart.gestionauth.entity.Role;
import com.obrasmart.gestionauth.entity.Status;
import com.obrasmart.gestionauth.entity.User;
import com.obrasmart.gestionauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeedAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void seedAdminIfMissing() {
        userRepository.findByUsernameIgnoreCase("admin").ifPresent(u -> { return; });

        User u = new User();
        u.setUsername("admin");
        u.setEmail("admin@obrasmart.local");
        u.setRole(Role.ADMINISTRACION);
        u.setStatus(Status.ACTIVO);
        u.setPasswordHash(passwordEncoder.encode("admin123"));

        userRepository.save(u);
    }
}
