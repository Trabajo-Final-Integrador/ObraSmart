# Arquitectura del Microservicio Gestión Asistente

```mermaid
flowchart LR
    FE[Frontend Angular] --> GW[API Gateway]
    GW --> ASIS[Microservicio Gestión Asistente]
    ASIS --> IA[(Proveedor IA<br>OpenAI / Ollama)]
```
