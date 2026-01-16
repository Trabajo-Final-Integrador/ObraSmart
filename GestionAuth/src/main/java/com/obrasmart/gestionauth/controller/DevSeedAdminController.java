package com.obrasmart.gestionauth.controller;

import com.obrasmart.gestionauth.service.SeedAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Profile("dev")
public class DevSeedAdminController {

    private final SeedAdminService seedAdminService;

    @PostMapping("/seed-admin")
    public ResponseEntity<String> seedAdmin() {
        seedAdminService.seedAdminIfMissing();
        return ResponseEntity.ok("OK");
    }
}
