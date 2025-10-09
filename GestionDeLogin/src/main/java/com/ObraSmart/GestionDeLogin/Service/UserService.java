package com.ObraSmart.GestionDeLogin.Service;



import com.ObraSmart.GestionDeLogin.Dto.RegisterUserDto;
import com.ObraSmart.GestionDeLogin.Entity.User;
import com.ObraSmart.GestionDeLogin.Entity.UserStatus;
import com.ObraSmart.GestionDeLogin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterUserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstname(dto.getFirstname());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setStatus(UserStatus.ACTIVO);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("system");
        return userRepository.save(user);
    }
}
