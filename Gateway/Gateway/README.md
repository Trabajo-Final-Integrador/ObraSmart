# Gateway ObraSmart (Spring Cloud Gateway)

Enrutamiento clave (modo sesión, sin JWT)
- `/auth/**` → GestionAuth (puerto 8082, JSESSIONID)
- `/actuator/health` → GestionAuth (health)
- Ejemplo: `/equipos/**` → micro correspondiente (ajustar puerto según servicio)

Cookies y CORS
- Las cookies `JSESSIONID` no se tocan (no hay filtros JWT).
- CORS permitido para `http://localhost:4200` con credenciales (`allowCredentials: true`).

Ejemplo con curl
```bash
# Login (guarda cookie)
curl -i -c cookies.txt -X POST "http://localhost:8085/auth/login" \
  -H "Content-Type: application/json" \
  -d '{ "username": "admin", "password": "admin123" }'

# Consultar usuario autenticado (reutiliza JSESSIONID)
curl -b cookies.txt "http://localhost:8085/auth/me"
```

Tip Angular
- Usar `withCredentials: true` en HttpClient para que se envíe la cookie a través del gateway.
