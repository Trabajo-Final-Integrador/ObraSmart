package com.obrasmart.gestionusuario.service.impl;

import com.obrasmart.gestionusuario.dto.UsuarioCreateRequest;
import com.obrasmart.gestionusuario.dto.UsuarioResponse;
import com.obrasmart.gestionusuario.dto.UsuarioUpdateRequest;
import com.obrasmart.gestionusuario.entity.Role;
import com.obrasmart.gestionusuario.entity.Status;
import com.obrasmart.gestionusuario.entity.Usuario;
import com.obrasmart.gestionusuario.exception.NotFoundException;
import com.obrasmart.gestionusuario.repository.UsuarioRepository;
import com.obrasmart.gestionusuario.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioResponse create(UsuarioCreateRequest request) {
        checkDuplicados(request.getUsername(), request.getEmail(), null);
        Usuario nuevo = mapToEntity(request);
        return mapToResponse(usuarioRepository.save(nuevo));
    }

    @Override
    public List<UsuarioResponse> list(String username, String email, Role role, Status status) {
        return usuarioRepository.search(username, email, role, status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UsuarioResponse getById(Long id) {
        return mapToResponse(findByIdOrThrow(id));
    }

    @Override
    public UsuarioResponse update(Long id, UsuarioUpdateRequest request) {
        Usuario existente = findByIdOrThrow(id);
        checkDuplicados(request.getUsername(), request.getEmail(), id);

        existente.setUsername(request.getUsername());
        existente.setEmail(request.getEmail());
        existente.setNombre(request.getNombre());
        existente.setApellido(request.getApellido());
        existente.setTelefono(request.getTelefono());
        existente.setRole(request.getRole());
        existente.setStatus(request.getStatus());

        return mapToResponse(usuarioRepository.save(existente));
    }

    @Override
    public void delete(Long id) {
        Usuario existente = findByIdOrThrow(id);
        existente.setStatus(Status.INACTIVE);
        usuarioRepository.save(existente);
    }

    @Override
    public UsuarioResponse getCurrentUser(String usernameOrEmail) {
        Usuario u = usuarioRepository.findByUsernameIgnoreCase(usernameOrEmail)
                .or(() -> usuarioRepository.findByEmailIgnoreCase(usernameOrEmail))
                .orElseThrow(() -> new NotFoundException("Usuario actual no encontrado"));
        return mapToResponse(u);
    }

    @Override
    public void uploadLicenciaFrente(Long id, MultipartFile file) {
        Usuario u = findByIdOrThrow(id);
        byte[] data = toBytes(file);
        u.setLicenciaConducirFrente(data);
        u.setLicenciaContentTypeFrente(file.getContentType());
        usuarioRepository.save(u);
    }

    @Override
    public void uploadLicenciaDorso(Long id, MultipartFile file) {
        Usuario u = findByIdOrThrow(id);
        byte[] data = toBytes(file);
        u.setLicenciaConducirDorso(data);
        u.setLicenciaContentTypeDorso(file.getContentType());
        usuarioRepository.save(u);
    }

    @Override
    public byte[] descargarLicenciaFrente(Long id) {
        return Optional.ofNullable(findByIdOrThrow(id).getLicenciaConducirFrente())
                .orElseThrow(() -> new NotFoundException("Licencia frente no cargada"));
    }

    @Override
    public byte[] descargarLicenciaDorso(Long id) {
        return Optional.ofNullable(findByIdOrThrow(id).getLicenciaConducirDorso())
                .orElseThrow(() -> new NotFoundException("Licencia dorso no cargada"));
    }

    @Override
    public String getLicenciaContentTypeFrente(Long id) {
        return findByIdOrThrow(id).getLicenciaContentTypeFrente();
    }

    @Override
    public String getLicenciaContentTypeDorso(Long id) {
        return findByIdOrThrow(id).getLicenciaContentTypeDorso();
    }

    private Usuario findByIdOrThrow(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    private void checkDuplicados(String username, String email, Long idActual) {
        usuarioRepository.findByUsernameIgnoreCase(username).ifPresent(u -> {
            if (idActual == null || !u.getId().equals(idActual)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username ya existe");
            }
        });
        usuarioRepository.findByEmailIgnoreCase(email).ifPresent(u -> {
            if (idActual == null || !u.getId().equals(idActual)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email ya existe");
            }
        });
    }

    private Usuario mapToEntity(UsuarioCreateRequest req) {
        return Usuario.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .nombre(req.getNombre())
                .apellido(req.getApellido())
                .telefono(req.getTelefono())
                .role(req.getRole())
                .status(req.getStatus())
                .build();
    }

    private byte[] toBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo leer el archivo");
        }
    }

    private UsuarioResponse mapToResponse(Usuario u) {
        return UsuarioResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .telefono(u.getTelefono())
                .role(u.getRole())
                .status(u.getStatus())
                .tieneLicenciaFrente(u.getLicenciaConducirFrente() != null)
                .tieneLicenciaDorso(u.getLicenciaConducirDorso() != null)
                .build();
    }
}
