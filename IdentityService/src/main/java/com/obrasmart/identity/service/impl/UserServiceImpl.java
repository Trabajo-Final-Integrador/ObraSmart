package com.obrasmart.identity.service.impl;

import com.obrasmart.identity.dto.UserCreateRequest;
import com.obrasmart.identity.dto.UserDto;
import com.obrasmart.identity.dto.UserUpdateRequest;
import com.obrasmart.identity.entity.*;
import com.obrasmart.identity.repository.UserDocumentRepository;
import com.obrasmart.identity.repository.UserRepository;
import com.obrasmart.identity.service.StorageService;
import com.obrasmart.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDocumentRepository documentRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> list() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public UserDto create(UserCreateRequest request) {
        validateUnique(request.getUsername(), request.getEmail(), null);
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(parseRole(request.getRole()))
                .status(parseStatus(request.getStatus()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .observaciones(request.getObservaciones())
                .enabled(true)
                .build();
        return toDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long id, UserUpdateRequest request) {
        User existing = findById(id);
        validateUnique(request.getUsername(), request.getEmail(), id);
        existing.setUsername(request.getUsername());
        existing.setEmail(request.getEmail());
        existing.setFirstname(request.getFirstname());
        existing.setLastname(request.getLastname());
        existing.setObservaciones(request.getObservaciones());
        existing.setRole(parseRole(request.getRole()));
        existing.setStatus(parseStatus(request.getStatus()));
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        return toDto(userRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        User existing = findById(id);
        existing.setStatus(Status.INACTIVO);
        userRepository.save(existing);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @Override
    public UserDto get(Long id) {
        return toDto(findById(id));
    }

    @Override
    @Transactional
    public void uploadProfilePhoto(Long userId, MultipartFile file) {
        User user = findById(userId);
        // desactivar fotos previas
        documentRepository.findByUser(user).stream()
                .filter(doc -> doc.getType() == DocumentType.PROFILE_PHOTO && doc.isActive())
                .forEach(doc -> {
                    doc.setActive(false);
                    documentRepository.save(doc);
                });
        saveDocument(user, DocumentType.PROFILE_PHOTO, file);
    }

    @Override
    public byte[] downloadProfilePhoto(Long userId) {
        User user = findById(userId);
        return documentRepository.findByUser(user).stream()
                .filter(doc -> doc.getType() == DocumentType.PROFILE_PHOTO && doc.isActive())
                .findFirst()
                .map(doc -> storageService.getObject(doc.getStorageKey()))
                .map(input -> {
                    try {
                        return input.readAllBytes();
                    } catch (IOException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo leer el archivo");
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto no encontrada"));
    }

    @Override
    public void uploadDocument(Long userId, String type, MultipartFile file) {
        User user = findById(userId);
        DocumentType t = DocumentType.valueOf(type);
        saveDocument(user, t, file);
    }

    @Override
    public List<?> listDocuments(Long userId) {
        User user = findById(userId);
        return documentRepository.findByUser(user).stream()
                .filter(UserDocument::isActive)
                .toList();
    }

    @Override
    public byte[] downloadDocument(Long userId, UUID docId) {
        User user = findById(userId);
        UserDocument doc = documentRepository.findById(docId)
                .filter(d -> d.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado"));
        try {
            return storageService.getObject(doc.getStorageKey()).readAllBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo leer el archivo");
        }
    }

    @Override
    public void deleteDocument(Long userId, UUID docId) {
        User user = findById(userId);
        UserDocument doc = documentRepository.findById(docId)
                .filter(d -> d.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado"));
        doc.setActive(false);
        documentRepository.save(doc);
    }

    private void saveDocument(User user, DocumentType type, MultipartFile file) {
        try {
            String key = "users/" + user.getId() + "/" + UUID.randomUUID();
            storageService.putObject(key, file.getInputStream(), file.getContentType(), file.getSize());
            UserDocument doc = UserDocument.builder()
                    .user(user)
                    .type(type)
                    .originalFilename(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .sizeBytes(file.getSize())
                    .storageKey(key)
                    .active(true)
                    .build();
            documentRepository.save(doc);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo guardar el archivo");
        }
    }

    private UserDto toDto(User u) {
        return UserDto.builder()
                .id(u.getId())
                .email(u.getEmail())
                .firstname(u.getFirstname())
                .lastname(u.getLastname())
                .username(u.getUsername())
                .role(u.getRole().name())
                .status(u.getStatus().name())
                .observaciones(u.getObservaciones())
                .build();
    }

    private void validateUnique(String username, String email, Long currentId) {
        userRepository.findByUsernameIgnoreCase(username).ifPresent(u -> {
            if (!u.getId().equals(currentId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username ya existe");
        });
        userRepository.findByEmailIgnoreCase(email).ifPresent(u -> {
            if (!u.getId().equals(currentId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email ya existe");
        });
    }

    private Role parseRole(String raw) {
        if (raw == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol requerido");
        String normalized = raw.trim().toUpperCase();
        if (normalized.startsWith("ROLE_")) {
            normalized = normalized.substring(5);
        }
        try {
            return Role.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol inválido: " + raw);
        }
    }

    private Status parseStatus(String raw) {
        if (raw == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado requerido");
        String normalized = raw.trim().toUpperCase();
        try {
            return Status.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido: " + raw);
        }
    }
}
