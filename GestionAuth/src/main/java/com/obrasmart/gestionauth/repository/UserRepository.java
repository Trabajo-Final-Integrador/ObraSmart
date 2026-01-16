package com.obrasmart.gestionauth.repository;

import com.obrasmart.gestionauth.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Permite login y búsquedas de usuarios por username (case-insensitive)
    Optional<User> findByUsernameIgnoreCase(String username);
    // Permite login y búsquedas de usuarios por email (case-insensitive)
    Optional<User> findByEmailIgnoreCase(String email);
}
