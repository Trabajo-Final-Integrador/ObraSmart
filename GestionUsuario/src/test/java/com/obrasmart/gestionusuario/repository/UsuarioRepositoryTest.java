package com.obrasmart.gestionusuario.repository;

import com.obrasmart.gestionusuario.entity.Role;
import com.obrasmart.gestionusuario.entity.Status;
import com.obrasmart.gestionusuario.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void guardarYBuscarPorUsernameEmail() {
        Usuario u = Usuario.builder()
                .username("admin")
                .email("admin@test.com")
                .role(Role.ADMINISTRACION)
                .status(Status.ACTIVE)
                .build();
        usuarioRepository.save(u);

        Optional<Usuario> byUsername = usuarioRepository.findByUsernameIgnoreCase("ADMIN");
        Optional<Usuario> byEmail = usuarioRepository.findByEmailIgnoreCase("ADMIN@test.com");

        assertThat(byUsername).isPresent();
        assertThat(byEmail).isPresent();
    }

    @Test
    void validaUnicidadUsernameEmail() {
        Usuario u1 = Usuario.builder()
                .username("user1")
                .email("u1@test.com")
                .role(Role.ADMINISTRACION)
                .status(Status.ACTIVE)
                .build();
        usuarioRepository.save(u1);

        Usuario u2 = Usuario.builder()
                .username("user1")
                .email("u2@test.com")
                .role(Role.SUPERVISOR)
                .status(Status.ACTIVE)
                .build();

        assertThrows(Exception.class, () -> {
            usuarioRepository.saveAndFlush(u2);
        });
    }
}
