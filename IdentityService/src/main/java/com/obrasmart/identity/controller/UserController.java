package com.obrasmart.identity.controller;

import com.obrasmart.identity.dto.UserCreateRequest;
import com.obrasmart.identity.dto.UserDto;
import com.obrasmart.identity.dto.UserUpdateRequest;
import com.obrasmart.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> list() {
        return ResponseEntity.ok(userService.list());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @PostMapping("/{id}/profile-photo")
    public ResponseEntity<Void> uploadProfile(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        userService.uploadProfilePhoto(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/profile-photo")
    public ResponseEntity<byte[]> downloadProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.downloadProfilePhoto(id));
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<Void> uploadDocument(
            @PathVariable Long id,
            @RequestParam("type") String type,
            @RequestPart("file") MultipartFile file) {
        userService.uploadDocument(id, type, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<List<?>> listDocuments(@PathVariable Long id) {
        return ResponseEntity.ok(userService.listDocuments(id));
    }

    @GetMapping("/{id}/documents/{docId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id, @PathVariable UUID docId) {
        return ResponseEntity.ok(userService.downloadDocument(id, docId));
    }

    @DeleteMapping("/{id}/documents/{docId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id, @PathVariable UUID docId) {
        userService.deleteDocument(id, docId);
        return ResponseEntity.noContent().build();
    }
}
