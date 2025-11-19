# Gestión Asistente – ObraSmart

El microservicio **Gestión Asistente** forma parte del ecosistema ObraSmart.  
Su función es proveer un asistente inteligente capaz de:

- Responder preguntas frecuentes.
- Guiar al usuario mediante un **bot interactivo** (menús, pasos guiados).
- Consultar IA local (Ollama) para respuestas rápidas.
- Consultar IA en la nube (Mistral AI) para respuestas avanzadas.
- Fallback automático si no hay internet.

## 🧠 Flujo de funcionamiento

1. **Bot interno** — Respuestas rápidas y menús guiados.  
2. **IA Local (Ollama)** — Preguntas simples, matemáticas, definiciones cortas.  
3. **IA Cloud (Mistral AI)** — Explicaciones largas, análisis profundo.  
4. **Fallback** — Mensaje de error cuando todo falla.

---

## 📡 Endpoint principal

### POST `/api/asistente/chat`

Envía un mensaje y recibe la respuesta del asistente.

**Ejemplo**
```json
{
  "mensaje": "¿Cómo registro un equipo?"
}
```

---

## ⚙️ Configuración en `application.properties`

```properties
server.port=8099
spring.application.name=GestionAsistente

# BOT
ia.bot.enabled=true

# IA LOCAL (Ollama)
ia.ollama.enabled=true
ia.ollama.url=http://localhost:11434/api/generate
ia.ollama.model=llama3.2:1b

# IA CLOUD (Mistral)
ia.cloud.enabled=true
ia.cloud.url=https://api.mistral.ai/v1/chat/completions
ia.cloud.apikey=TU_API_KEY
ia.cloud.model=mistral-small-latest

# UTF-8
server.servlet.encoding.charset=UTF-8
server.servlet.encoding.enabled=true
server.servlet.encoding.force=true
```

---

## 🧩 Arquitectura lógica

```
Usuario → AsistenteService → Bot → IA Local → IA Cloud → Fallback
```

---

## 🤖 Modo Bot Interactivo

Para activar el modo guiado:

```
info
```

Incluye menús interactivos:

### 1️⃣ Equipos  
Registrar, consultar, generar JSON listo.

### 2️⃣ Reparaciones  
Alta guiada, descripción, equipo.

### 3️⃣ Stock  
Repuestos, movimientos, órdenes de compra.

### 4️⃣ Proveedores  
Alta guiada con JSON final, búsqueda por CUIT.

### 5️⃣ Geolocalización  
Equipos en mapa y reparaciones activas.

### 6️⃣ Login  
Ayuda sobre acceso y errores comunes.

Para salir del bot:
```
salir
```

---

## 🚀 Cómo ejecutar

```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

Requisitos:
- Java 17
- Spring Boot 3.5+
- Ollama instalado
- Cuenta Mistral AI si usás nube

---

## ✨ Autores

- **Fernando Gabriel Ferreyra**  
- **Leticia Silcana Castro Altamirano**
