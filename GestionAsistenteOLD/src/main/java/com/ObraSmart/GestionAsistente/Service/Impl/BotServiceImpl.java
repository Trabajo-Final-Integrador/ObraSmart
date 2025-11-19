package com.ObraSmart.GestionAsistente.Service.Impl;

import com.ObraSmart.GestionAsistente.Service.BotService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BotServiceImpl implements BotService {

    private final Map<String, String> intents = new HashMap<>();

    public BotServiceImpl() {

        // Guía / Manual interactivo
        intents.put("ayuda", "Soy el asistente de ObraSmart. Puedo ayudarte con equipos, reparaciones, mapa, stock y navegación general.");
        intents.put("tutorial", "Guía rápida: 1) Registrar equipos. 2) Crear reparaciones. 3) Ver mapa. 4) Consultar stock. 5) Generar reportes.");
        intents.put("manual", "Puedes pedirme: 'crear reparación', 'ver equipos', 'ver mapa', 'stock', 'reportes'.");

        // Funciones comunes
        intents.put("crear reparacion", "Para crear una reparación: Menú → Reparaciones → Crear.");
        intents.put("equipo", "Para registrar un equipo: Menú → Equipos → Crear.");
        intents.put("mapa", "Para ver el mapa: Menú → Geolocalización → Mapa.");
        intents.put("stock", "En Stock puedes ver insumos, proveedores y reposición mínima.");
        intents.put("reportes", "En Reportes puedes obtener informes de ventas, equipos, insumos y clientes.");
    }

    @Override
    public String responder(String mensaje) {
        if (mensaje == null) return null;

        String lower = mensaje.toLowerCase();

        for (String clave : intents.keySet()) {
            if (lower.contains(clave)) {
                return intents.get(clave);
            }
        }

        return null;
    }
}
