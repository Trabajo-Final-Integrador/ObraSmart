package com.ObraSmart.GestionAsistente.Service;

public interface IaService {

    /**
     * Flujo completo: primero IA local, luego IA avanzada.
     */
    String consultarIA(String mensaje);

    /**
     * Fuerza consulta solo con IA local (Ollama).
     */
    String consultarSoloOllama(String mensaje);

    /**
     * Fuerza consulta solo con IA avanzada (DeepSeek).
     */
    String consultarSoloDeepseek(String mensaje);
}
