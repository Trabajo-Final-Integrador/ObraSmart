package com.obrasmart.gestionusuario.service;

import com.obrasmart.gestionusuario.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprueba que el contexto de seguridad carga y se puede leer el principal.
 */
@SpringBootTest
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class UserServiceSecurityTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    @WithMockUser(username = "testuser", roles = "ADMINISTRACION")
    void contextoSecurityDisponible() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("testuser");
        assertThat(authenticationManager).isNotNull();
    }
}
