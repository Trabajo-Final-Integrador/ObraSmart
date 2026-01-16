package com.obrasmart.gestionusuario.service;

import com.obrasmart.gestionusuario.dto.UsuarioCreateRequest;
import com.obrasmart.gestionusuario.dto.UsuarioResponse;
import com.obrasmart.gestionusuario.dto.UsuarioUpdateRequest;
import com.obrasmart.gestionusuario.entity.Role;
import com.obrasmart.gestionusuario.entity.Status;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UsuarioResponse create(UsuarioCreateRequest request);
    List<UsuarioResponse> list(String username, String email, Role role, Status status);
    UsuarioResponse getById(Long id);
    UsuarioResponse update(Long id, UsuarioUpdateRequest request);
    void delete(Long id);
    UsuarioResponse getCurrentUser(String usernameOrEmail);
    void uploadLicenciaFrente(Long id, MultipartFile file);
    void uploadLicenciaDorso(Long id, MultipartFile file);
    byte[] descargarLicenciaFrente(Long id);
    byte[] descargarLicenciaDorso(Long id);
    String getLicenciaContentTypeFrente(Long id);
    String getLicenciaContentTypeDorso(Long id);
}
