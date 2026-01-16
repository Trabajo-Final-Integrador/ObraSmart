package com.obrasmart.gestionusuario.service;

import com.obrasmart.gestionusuario.dto.UsuarioCreateRequest;
import com.obrasmart.gestionusuario.dto.UsuarioUpdateRequest;
import com.obrasmart.gestionusuario.entity.Role;
import com.obrasmart.gestionusuario.entity.Status;
import com.obrasmart.gestionusuario.entity.Usuario;
import com.obrasmart.gestionusuario.repository.UsuarioRepository;
import com.obrasmart.gestionusuario.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_validaDuplicados() {
        UsuarioCreateRequest req = new UsuarioCreateRequest();
        req.setUsername("user1");
        req.setEmail("u1@test.com");
        req.setRole(Role.ADMINISTRACION);
        req.setStatus(Status.ACTIVE);

        when(usuarioRepository.findByUsernameIgnoreCase("user1")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmailIgnoreCase("u1@test.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        var res = userService.create(req);
        assertThat(res.getId()).isEqualTo(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void create_lanzaErrorSiDuplicado() {
        UsuarioCreateRequest req = new UsuarioCreateRequest();
        req.setUsername("user1");
        req.setEmail("u1@test.com");
        req.setRole(Role.ADMINISTRACION);
        req.setStatus(Status.ACTIVE);

        when(usuarioRepository.findByUsernameIgnoreCase("user1")).thenReturn(Optional.of(new Usuario()));

        assertThrows(ResponseStatusException.class, () -> userService.create(req));
    }

    @Test
    void update_actualizaCampos() {
        UsuarioUpdateRequest req = new UsuarioUpdateRequest();
        req.setUsername("user1");
        req.setEmail("u1@test.com");
        req.setRole(Role.ADMINISTRACION);
        req.setStatus(Status.ACTIVE);

        Usuario existing = new Usuario();
        existing.setId(1L);
        existing.setUsername("old");
        existing.setEmail("old@test.com");
        existing.setRole(Role.SUPERVISOR);
        existing.setStatus(Status.INACTIVE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(usuarioRepository.findByUsernameIgnoreCase("user1")).thenReturn(Optional.of(existing));
        when(usuarioRepository.findByEmailIgnoreCase("u1@test.com")).thenReturn(Optional.of(existing));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        var res = userService.update(1L, req);
        assertThat(res.getUsername()).isEqualTo("user1");
        assertThat(res.getRole()).isEqualTo(Role.ADMINISTRACION);
    }

    @Test
    void delete_softDelete() {
        Usuario existing = new Usuario();
        existing.setId(1L);
        existing.setStatus(Status.ACTIVE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existing));

        userService.delete(1L);
        assertThat(existing.getStatus()).isEqualTo(Status.INACTIVE);
        verify(usuarioRepository).save(existing);
    }
}
