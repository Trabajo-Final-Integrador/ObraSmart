package com.obrasmart.identity.startup;

import com.obrasmart.identity.entity.Role;
import com.obrasmart.identity.entity.Status;
import com.obrasmart.identity.entity.User;
import com.obrasmart.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username:admin}")
    private String adminUser;
    @Value("${admin.email:admin@local}")
    private String adminEmail;
    @Value("${admin.password:admin123}")
    private String adminPass;
    @Value("${admin.require-env:false}")
    private boolean requireEnv;

    @Override
    public void run(String... args) {
        if (userRepository.existsByRole(Role.ADMINISTRACION)) {
            return;
        }
        if (requireEnv && (isBlank(adminUser) || isBlank(adminEmail) || isBlank(adminPass))) {
            throw new ResponseStatusException(BAD_REQUEST, "ADMIN_* requeridos en este entorno");
        }
        User admin = User.builder()
                .username(adminUser)
                .email(adminEmail)
                .passwordHash(passwordEncoder.encode(adminPass))
                .role(Role.ADMINISTRACION)
                .status(Status.ACTIVO)
                .enabled(true)
                .build();
        userRepository.save(admin);
    }

    private boolean isBlank(String v) {
        return v == null || v.isBlank();
    }
}
