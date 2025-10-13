package com.ObraSmart.Controller;



import com.ObraSmart.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserPublicController {

    private final UserRepository userRepository;

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        boolean exists = userRepository.findByEmail(email).isPresent();
        return ResponseEntity.ok(exists);
    }
}

