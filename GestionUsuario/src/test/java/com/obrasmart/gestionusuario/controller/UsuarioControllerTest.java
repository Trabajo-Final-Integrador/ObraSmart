package com.obrasmart.gestionusuario.controller;

import com.obrasmart.gestionusuario.dto.UsuarioResponse;
import com.obrasmart.gestionusuario.entity.Role;
import com.obrasmart.gestionusuario.entity.Status;
import com.obrasmart.gestionusuario.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioController.class)
@Import({com.obrasmart.gestionusuario.config.SecurityConfig.class, com.obrasmart.gestionusuario.config.CorsConfig.class})
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private com.obrasmart.gestionusuario.config.CurrentUserProvider currentUserProvider;

    @Test
    void sinAutenticacionRetorna401() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRACION"})
    void listadoOkConRolAdmin() throws Exception {
        when(userService.list(any(), any(), any(), any())).thenReturn(List.of());
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRACION"})
    void createBadRequestPorBodyInvalido() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRACION"})
    void meDevuelve200ConSesion() throws Exception {
        when(currentUserProvider.getCurrentUsernameOrEmail()).thenReturn("admin");
        when(userService.getCurrentUser("admin")).thenReturn(
                UsuarioResponse.builder()
                        .id(1L).username("admin").email("a@a.com")
                        .role(Role.ADMINISTRACION).status(Status.ACTIVE)
                        .build()
        );
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk());
    }
}
