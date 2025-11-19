package com.ObraSmart.GestionAsistente.Service.Impl;

import com.ObraSmart.GestionAsistente.Service.BotService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class BotServiceImpl implements BotService {

    // ===========================
    // CONTEXTOS
    // ===========================
    private String estado = null;      // para wizards (registrar equipo, etc.)
    private String modo = "normal";    // normal / info
    private final Map<String, Object> memoria = new LinkedHashMap<>();

    private final Map<String, String> intents = new LinkedHashMap<>();

    public BotServiceImpl() {
        cargarIntentsGenerales();
        cargarIntentsEquipos();
        cargarIntentsReparaciones();
        cargarIntentsGeolocalizacion();
        cargarIntentsStock();
        cargarIntentsLogin();
    }

    @Override
    public String responder(String mensaje) {

        if (mensaje == null || mensaje.isBlank()) return null;
        String msg = mensaje.toLowerCase(Locale.ROOT).trim();

        // ======================================================
        // 0) MODO INFO → MENÚ INTERACTIVO
        // ======================================================
        if (msg.equals("info")) {
            modo = "info";
            estado = "MENU_PRINCIPAL";
            return menuPrincipal();
        }

        if (msg.equals("salir")) {
            modo = "normal";
            estado = null;
            memoria.clear();
            return "Saliendo del modo interactivo ✔\nVolvés al asistente normal.";
        }

        if (modo.equals("info")) {
            return manejarMenuInteractivo(msg);
        }

        // ======================================================
        // 1) MANEJAR WIZARDS ACTIVOS
        // ======================================================
        if (estado != null) {
            return manejarConversacion(msg);
        }

        // ======================================================
        // 2) COMANDOS DE SISTEMA
        // ======================================================
        if (msg.equals("/ollama")) return "__FORZAR_OLLAMA__";
        if (msg.equals("/cloud")) return "__FORZAR_CLOUD__";

        // ======================================================
        // 3) INTENTS
        // ======================================================
        for (var entry : intents.entrySet()) {
            if ((" " + msg + " ").contains(" " + entry.getKey() + " ")) {
                return entry.getValue();
            }
        }

        // ======================================================
        // 4) DETECCIÓN DE INTENCIONES DIRECTAS
        // ======================================================
        if (msg.contains("registrar equipo")) {
            estado = "REGISTRAR_EQUIPO_NOMBRE";
            return "Perfecto 👍 ¿Cuál es el *nombre del equipo*?";
        }

        if (msg.contains("registrar reparación")) {
            estado = "REPA_NUEVA_EQUIPO";
            return "Entendido 🔧 ¿Para qué *equipo* es la reparación?";
        }

        if (msg.contains("alta proveedor")) {
            estado = "PROV_NUEVO_NOMBRE";
            return "Perfecto 👤 ¿Cuál es el *nombre comercial* del proveedor?";
        }

        if (msg.contains("buscar proveedor")) {
            estado = "PROV_BUSCAR_CUIT";
            return "Decime el *CUIT* del proveedor que querés buscar.";
        }

        return null; // Pasa a IA
    }


    // ==========================================================
    // 🔥 MODO INTERACTIVO — MENÚ PRINCIPAL
    // ==========================================================
    private String menuPrincipal() {
        return """
                📘 *Asistente interactivo de ObraSmart*

                Elegí una opción:
                1️⃣ Equipos
                2️⃣ Reparaciones
                3️⃣ Stock
                4️⃣ Proveedores
                5️⃣ Geolocalización
                6️⃣ Login

                ✨ Para salir escribí: *salir*
                """;
    }


    // ==========================================================
    // 🔥 MANEJAR NAVEGACIÓN ENTRE MENÚS
    // ==========================================================
    private String manejarMenuInteractivo(String msg) {

        if (estado.equals("MENU_PRINCIPAL")) {
            return manejarMenu(msg);
        }

        return switch (estado) {
            case "MENU_EQUIPOS" -> manejarSubMenuEquipos(msg);
            case "MENU_REPA" -> manejarSubMenuRepa(msg);
            case "MENU_STOCK" -> manejarSubMenuStock(msg);
            case "MENU_PROV" -> manejarSubMenuProveedores(msg);
            case "MENU_MAPA" -> manejarSubMenuMapa(msg);
            case "MENU_LOGIN" -> manejarSubMenuLogin(msg);
            default -> "No entendí esa opción. Escribí *salir* para terminar.";
        };
    }


    // ==========================
    // MENÚ PRINCIPAL
    // ==========================
    private String manejarMenu(String msg) {

        switch (msg) {
            case "1", "equipos" -> {
                estado = "MENU_EQUIPOS";
                return """
                        🔧 *Menú Equipos*
                        a) Registrar equipo
                        b) Consultar equipo

                        Elegí una opción o escribí *salir*.
                        """;
            }

            case "2", "reparaciones" -> {
                estado = "MENU_REPA";
                return """
                        🔧 *Menú Reparaciones*
                        a) Registrar reparación
                        b) Ver reparaciones

                        Elegí una opción o escribí *salir*.
                        """;
            }

            case "3", "stock" -> {
                estado = "MENU_STOCK";
                return """
                        📦 *Menú Stock*
                        a) Ingresar repuesto
                        b) Movimientos
                        c) Órdenes de compra

                        Elegí una opción o escribí *salir*.
                        """;
            }

            case "4", "proveedores" -> {
                estado = "MENU_PROV";
                return """
                        👤 *Menú Proveedores*
                        a) Registrar proveedor
                        b) Buscar proveedor

                        Elegí una opción o escribí *salir*.
                        """;
            }

            case "5", "geolocalizacion" -> {
                estado = "MENU_MAPA";
                return """
                        🗺️ *Geolocalización*
                        a) Ver equipos en mapa
                        b) Ver reparaciones activas

                        Elegí una opción o escribí *salir*.
                        """;
            }

            case "6", "login" -> {
                estado = "MENU_LOGIN";
                return """
                        🔐 *Login*
                        a) Cómo iniciar sesión
                        b) Errores comunes

                        Elegí una opción o escribí *salir*.
                        """;
            }

            default -> {
                return "No entendí esa opción. Elegí un número del menú o escribí *salir*.";
            }
        }
    }


    // ==========================================================
    // SUBMENÚS (respuestas rápidas)
    // ==========================================================

    private String manejarSubMenuEquipos(String msg) {
        return switch (msg) {
            case "a" -> "👉 Para registrar un equipo: decí *registrar equipo* y te guío paso a paso.";
            case "b" -> "👉 Para consultar equipos usá: GET /api/equipos";
            default -> "Opción no válida. Escribí *salir* para volver.";
        };
    }

    private String manejarSubMenuRepa(String msg) {
        return switch (msg) {
            case "a" -> "👉 Para registrar una reparación decí *registrar reparación* y te guío.";
            case "b" -> "👉 Endpoint: GET /api/reparaciones/activas";
            default -> "Opción no válida.";
        };
    }

    private String manejarSubMenuStock(String msg) {
        return switch (msg) {
            case "a" -> "👉 POST /api/repuestos";
            case "b" -> "👉 GET /api/stock/movimientos";
            case "c" -> "👉 POST /api/ordenes";
            default -> "Opción incorrecta.";
        };
    }

    private String manejarSubMenuProveedores(String msg) {
        return switch (msg) {
            case "a" -> "👉 Para registrar un proveedor decí *alta proveedor*.";
            case "b" -> "👉 Para buscar proveedores decí *buscar proveedor*.";
            default -> "Opción incorrecta.";
        };
    }

    private String manejarSubMenuMapa(String msg) {
        return switch (msg) {
            case "a" -> "👉 Mapa equipos: GET /api/mapa/equipos";
            case "b" -> "👉 Mapa reparaciones: GET /api/mapa/reparaciones";
            default -> "Opción incorrecta.";
        };
    }

    private String manejarSubMenuLogin(String msg) {
        return switch (msg) {
            case "a" -> "👉 POST /api/auth/login { username, password }";
            case "b" -> "👉 Error común: credenciales inválidas o token expirado.";
            default -> "Opción incorrecta.";
        };
    }


    // ==========================================================
    // WIZARDS (YA EXISTENTES – NO SE TOCAN)
    // ==========================================================
    private String manejarConversacion(String msg) {
        switch (estado) {

            case "REGISTRAR_EQUIPO_NOMBRE" -> {
                memoria.put("nombre", msg);
                estado = "REGISTRAR_EQUIPO_MARCA";
                return "Nombre registrado ✔ ¿Cuál es la *marca* del equipo?";
            }

            case "REGISTRAR_EQUIPO_MARCA" -> {
                memoria.put("marca", msg);
                estado = "REGISTRAR_EQUIPO_MODELO";
                return "Perfecto. ¿Cuál es el *modelo*?";
            }

            case "REGISTRAR_EQUIPO_MODELO" -> {
                memoria.put("modelo", msg);
                estado = null;

                return """
                        Equipo listo para registrar 🛠️

                        Mandá este JSON al microservicio de *Equipos*:

                        {
                          "nombre": "%s",
                          "marca": "%s",
                          "modelo": "%s"
                        }

                        Endpoint:
                        POST /api/equipos
                        """
                        .formatted(
                                memoria.get("nombre"),
                                memoria.get("marca"),
                                memoria.get("modelo")
                        );
            }

            case "PROV_BUSCAR_CUIT" -> {
                estado = null;
                return """
                        Podés buscar el proveedor con este endpoint:

                        GET /api/proveedores/buscar?cuit=%s
                        """.formatted(msg);
            }

            case "PROV_NUEVO_NOMBRE" -> {
                memoria.put("nombreProv", msg);
                estado = "PROV_NUEVO_CUIT";
                return "Bien ✔ ¿Cuál es el *CUIT* del proveedor?";
            }

            case "PROV_NUEVO_CUIT" -> {
                memoria.put("cuitProv", msg);
                estado = "PROV_NUEVO_ESPECIALIDAD";
                return "Perfecto. ¿Cuál es la *especialidad* del proveedor?";
            }

            case "PROV_NUEVO_ESPECIALIDAD" -> {
                memoria.put("especialidad", msg);
                estado = null;

                return """
                        Proveedor preparado ✔

                        Mandá este JSON al microservicio de *Stock*:

                        {
                          "nombreComercial": "%s",
                          "cuit": "%s",
                          "especialidad": "%s"
                        }

                        Endpoint:
                        POST /api/proveedores
                        """
                        .formatted(
                                memoria.get("nombreProv"),
                                memoria.get("cuitProv"),
                                memoria.get("especialidad")
                        );
            }

            case "REPA_NUEVA_EQUIPO" -> {
                memoria.put("equipo", msg);
                estado = "REPA_NUEVA_DESC";
                return "¿Cuál es la *descripción* de la reparación?";
            }

            case "REPA_NUEVA_DESC" -> {
                memoria.put("descripcion", msg);
                estado = null;

                return """
                        Reparación lista para registrar 🔧

                        JSON sugerido:
                        {
                          "equipoId": "%s",
                          "descripcion": "%s"
                        }

                        Endpoint:
                        POST /api/reparaciones
                        """
                        .formatted(
                                memoria.get("equipo"),
                                memoria.get("descripcion")
                        );
            }
        }

        estado = null;
        return "No entendí esa parte 🤔 ¿Podés repetir?";
    }


    // ==========================================================
    // INTENTS NORMALES
    // ==========================================================
    private void cargarIntentsGenerales() {
        intents.put("obrasmart", """
                ObraSmart es un sistema integral de gestión para empresas constructoras.

                Permite administrar:
                • Equipos
                • Reparaciones
                • Stock y proveedores
                • Movimientos
                • Órdenes de compra
                • Geolocalización
                • Usuarios y roles
                • Asistencia inteligente

                Su objetivo es digitalizar tareas de obra y optimizar procesos.
                """);

        intents.put("hola", "¡Hola! Soy el Asistente de ObraSmart. ¿En qué puedo ayudarte?");
        intents.put("ayuda", "Tengo ayuda para Equipos, Reparaciones, Stock, Mapa y Login. ¿Sobre cuál módulo querés consultar?");
    }

    private void cargarIntentsEquipos() {
        intents.put("equipo", """
                📘 *Módulo Equipos*
                Puedo ayudarte con:
                • Registrar un equipo
                • Consultar equipos
                • Ver estado operativo
                """);
    }

    private void cargarIntentsReparaciones() {
        intents.put("reparacion", """
                🔧 *Módulo Reparaciones*
                • Registrar reparación
                • Ver reparaciones activas
                • Consultar estado
                """);
    }

    private void cargarIntentsGeolocalizacion() {
        intents.put("mapa", """
                🗺️ *Geolocalización*
                • Ver equipos en mapa
                • Ver reparaciones activas
                • Última ubicación registrada
                """);
    }

    private void cargarIntentsStock() {
        intents.put("stock", """
                📦 *Módulo Stock*
                • Repuestos
                • Categorías
                • Movimientos
                • Órdenes de compra
                • Proveedores
                """);
        intents.put("proveedor", "Querés registrar un proveedor o buscar uno?");
    }

    private void cargarIntentsLogin() {
        intents.put("login", """
                🔐 *Login*
                • Cómo iniciar sesión
                • Roles
                • Errores comunes
                """);
    }
}
