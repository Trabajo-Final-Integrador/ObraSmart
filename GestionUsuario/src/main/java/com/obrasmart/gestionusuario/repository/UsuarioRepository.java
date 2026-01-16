package com.obrasmart.gestionusuario.repository;

import com.obrasmart.gestionusuario.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsernameIgnoreCase(String username);
    Optional<Usuario> findByEmailIgnoreCase(String email);

    @Query("""
           SELECT u FROM Usuario u
           WHERE (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
             AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
             AND (:role IS NULL OR u.role = :role)
             AND (:status IS NULL OR u.status = :status)
           """)
    List<Usuario> search(
            @Param("username") String username,
            @Param("email") String email,
            @Param("role") com.obrasmart.gestionusuario.entity.Role role,
            @Param("status") com.obrasmart.gestionusuario.entity.Status status
    );
}
