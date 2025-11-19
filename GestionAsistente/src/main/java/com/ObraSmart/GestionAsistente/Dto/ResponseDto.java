package com.ObraSmart.GestionAsistente.Dto;

/**
 * DTO utilizado para devolver la respuesta generada por:
 * - el bot interno
 * - la IA local
 * - la IA avanzada
 * - o el menú del asistente
 */
public record ResponseDto(String respuesta) {}
