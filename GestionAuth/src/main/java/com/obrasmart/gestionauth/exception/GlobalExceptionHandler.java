package com.obrasmart.gestionauth.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de negocio/control usando ResponseStatusException para
     * devolver el código y mensaje adecuado al cliente.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleRse(ResponseStatusException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    /**
     * Responde validaciones fallidas de @Valid en los DTOs.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed");
        body.put("fields", ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                .toList());
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Último recurso: evita que un error inesperado caiga sin respuesta controlada.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Internal error");
        return ResponseEntity.status(500).body(body);
    }
}
