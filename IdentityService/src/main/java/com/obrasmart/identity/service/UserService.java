package com.obrasmart.identity.service;

import com.obrasmart.identity.dto.UserCreateRequest;
import com.obrasmart.identity.dto.UserDto;
import com.obrasmart.identity.dto.UserUpdateRequest;
import com.obrasmart.identity.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDto> list();
    UserDto create(UserCreateRequest request);
    UserDto update(Long id, UserUpdateRequest request);
    void delete(Long id);
    User findById(Long id);
    UserDto get(Long id);
    void uploadProfilePhoto(Long userId, MultipartFile file);
    byte[] downloadProfilePhoto(Long userId);
    void uploadDocument(Long userId, String type, MultipartFile file);
    List<?> listDocuments(Long userId);
    byte[] downloadDocument(Long userId, UUID docId);
    void deleteDocument(Long userId, UUID docId);
}
