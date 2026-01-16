package com.obrasmart.gestionauth.repository;

import com.obrasmart.gestionauth.entity.PasswordResetToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    // Consulta el token en la base para validar y permitir el reseteo de contraseña
    Optional<PasswordResetToken> findByToken(String token);
}
