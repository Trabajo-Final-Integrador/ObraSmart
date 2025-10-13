package com.ObraSmart.Repository;

import com.ObraSmart.Entity.Role;
import com.ObraSmart.Entity.Status;
import com.ObraSmart.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 🔸 Buscar usuario por username (útil para login)
    Optional<User> findByUsername(String username);

    // 🔸 Buscar usuario por email
    Optional<User> findByEmail(String email);

    // 🔸 Verificar si ya existe email
    boolean existsByEmail(String email);

    // ✍️ Actualizar parcialmente usuario (sin cambiar contraseña ni username)
    @Modifying
    @Query("""
        UPDATE User u
        SET u.firstname = :firstname,
            u.lastname = :lastname,
            u.role = :role,
            u.status = :status
        WHERE u.id = :id
    """)
    void updateUser(
            @Param("id") Integer id,
            @Param("firstname") String firstname,
            @Param("lastname") String lastname,
            @Param("role") Role role,
            @Param("status") Status status
    );
}
