# 🧠 Microservicio “Gestión Asistente” – ObraSmart

ObraSmart es un sistema integral de gestión para empresas constructoras, diseñado para digitalizar procesos clave como administración de equipos, stock, reparaciones y geolocalización. Dentro de este ecosistema surge la necesidad de contar con una herramienta capaz de **asistir en tiempo real**, guiar a los usuarios y agilizar consultas frecuentes.

El microservicio **Gestión Asistente** cumple exactamente ese rol: un servicio basado en **Inteligencia Artificial**, capaz de centralizar preguntas y responderlas automáticamente mediante modelos de lenguaje (LLM) como **OpenAI** u **Ollama** local.

Funciona como un soporte técnico operativo dentro del sistema, facilitando la adopción de ObraSmart, reduciendo errores y minimizando la necesidad de capacitación directa.

---

## 📌 Necesidad del proyecto

En la operación diaria, los usuarios de ObraSmart suelen necesitar:

- Consultar pasos para registrar equipos o completar formularios.  
- Obtener explicaciones de validaciones o mensajes de error.  
- Interpretar reportes, estados de equipos o procesos internos.  
- Realizar consultas sobre direcciones o coordenadas de otros módulos.  
- Agilizar tareas repetitivas que consumen tiempo del personal.

Antes de implementar este microservicio, estas consultas dependían del manual o de otros usuarios, lo que generaba:

- Errores repetitivos.  
- Tiempos muertos.  
- Dudas operativas.  
- Saturación del soporte técnico interno.  

El Asistente soluciona esto proporcionando respuestas inmediatas y contextualizadas.

---

## 🚀 Procesos actuales que mejora

### 1. Asistencia a la carga de datos

Los usuarios pueden preguntar cosas como:

- “¿Cómo cargo un equipo nuevo?”  
- “¿Por qué no me toma la marca?”  
- “¿Qué significa estado ‘PENDIENTE’?”  

Reciben respuestas breves, claras y guiadas.

---

### 2. Consultas técnicas

El asistente ayuda a interpretar:

- Validaciones del backend.  
- Errores de formularios.  
- Restricciones de campos.  

Evitando soporte manual y tiempos muertos.

---

### 3. Integración con otros módulos

Ayuda a entender:

- Qué endpoint usar.  
- Qué ID falta.  
- Qué flujo sigue cada módulo.  

Desde **Equipos**, **Reparaciones**, **Stock** o **Geolocalización**.

---

### 4. Acompañamiento a usuarios nuevos

Un usuario nuevo puede:

- Preguntar cualquier cosa del sistema.  
- Recibir guías paso a paso.  
- Evitar capacitaciones extensas al inicio.  

---

## ⚠️ Desafíos previos

Antes de este microservicio existían:

- Muchas dudas operativas por parte de usuarios.  
- Saturación del equipo de soporte.  
- Errores frecuentes en la carga de información.  
- Falta de un asistente centralizado.  
- Baja productividad por esperas innecesarias.  

---

## 🎯 Objetivos del microservicio

El microservicio de Asistente permite:

- Responder preguntas en tiempo real.  
- Guiar procesos internos de ObraSmart.  
- Explicar errores y validaciones.  
- Integrarse con otros microservicios.  
- Brindar disponibilidad 24/7.  
- Reducir soporte humano.  

---

## 🧩 Microservicio: Gestión Asistente

### 📍 Descripción general

El microservicio Gestión Asistente corre en el puerto **8099** y actúa como un **centro conversacional**.

- Recibe mensajes desde el frontend mediante el **Gateway**.  
- Los envía al proveedor IA seleccionado (OpenAI u Ollama local).  
- Devuelve una respuesta automática lista para mostrar al usuario.  

> 🔎 No posee base de datos propia: toda la lógica es **stateless**, en memoria y apoyada en el proveedor IA externo.

---

## 📡 Endpoints principales

> Nota: los nombres pueden adaptarse al código final del proyecto, pero la idea general es esta.

### 🗨️ Chat con el asistente

- **Método:** `POST`  
- **URL (vía Gateway):** `/asistente/chat`  
- **URL directa (sin Gateway, solo backend):** `http://localhost:8099/api/asistente/chat`  

**Request (JSON):**

```json
{
  "mensaje": "¿Cómo registro un equipo nuevo?",
  "contexto": "modulo-equipos"
}
```

**Response (JSON):**

```json
{
  "respuesta": "Para registrar un equipo nuevo, debes ir al módulo Equipos, hacer clic en 'Nuevo', completar marca, modelo, tipo y estado, y luego guardar.",
  "modeloUsado": "openai/gpt-4.1-mini"
}
```

---

### ❤️ Health check

- **Método:** `GET`  
- **URL directa:** `http://localhost:8099/api/asistente/health`  

**Response ejemplo:**

```json
{
  "status": "OK",
  "proveedorIA": "OLLAMA",
  "modelo": "llama3.2"
}
```

---

## 🧱 Estructura de paquetes sugerida (Java / Spring Boot)

```text
com.ObraSmart.GestionAsistente
 ├── controller
 │    └── AsistenteController.java
 ├── service
 │    ├── AsistenteService.java
 │    └── impl
 │         └── AsistenteServiceImpl.java
 ├── client
 │    ├── LlmClient.java        # interfaz genérica
 │    ├── OpenAiClient.java     # implementación para OpenAI
 │    └── OllamaClient.java     # implementación para Ollama local
 ├── dto
 │    ├── ChatRequestDto.java
 │    └── ChatResponseDto.java
 ├── config
 │    └── LlmConfig.java        # configuración de API keys / URLs
 └── GestionAsistenteApplication.java
```

---

## ⚙️ Configuración básica (ejemplo)

En `application.properties` o `application.yml` del microservicio Gestión Asistente:

```properties
server.port=8099
spring.application.name=gestion-asistente

# Modo de proveedor IA: OPENAI u OLLAMA
asistente.ia.provider=OLLAMA

# Configuración OpenAI (si se usa)
asistente.ia.openai.api-key=TU_API_KEY
asistente.ia.openai.model=gpt-4.1-mini

# Configuración Ollama local (si se usa)
asistente.ia.ollama.base-url=http://localhost:11434
asistente.ia.ollama.model=llama3.2
```

---

## 🧬 Diagrama de clases (resumen)

Ver archivo: `docs/diagramas/gestion-asistente-clases.puml`

Representa las relaciones entre:

- `AsistenteController`  
- `AsistenteService` / `AsistenteServiceImpl`  
- `LlmClient` y sus implementaciones (`OpenAiClient`, `OllamaClient`)  
- DTOs (`ChatRequestDto`, `ChatResponseDto`)  

---

## 🔁 Flujo de interacción (alto nivel)

Ver archivo: `docs/diagramas/gestion-asistente-flujo.puml`

Flujo típico:

1. El usuario escribe una consulta en el frontend.  
2. El frontend envía la petición al **Gateway**.  
3. El Gateway redirige al microservicio **Gestión Asistente**.  
4. Gestión Asistente invoca al proveedor IA (OpenAI u Ollama).  
5. El proveedor IA devuelve la respuesta generada.  
6. Gestión Asistente la adapta al formato esperado y la devuelve al Gateway.  
7. El frontend muestra la respuesta al usuario en la interfaz de chat.  

---

## 📁 Ubicación sugerida de la documentación en el proyecto

Dentro del repositorio de backend de ObraSmart:

```text
ObraSmart/
 └── GestionAsistente/
      ├── README.md
      └── docs/
           └── diagramas/
                ├── gestion-asistente-clases.puml
                └── gestion-asistente-flujo.puml
```

Con esto, el microservicio queda documentado a nivel funcional, técnico y visual para ser presentado en la tesis o en la documentación del proyecto.
