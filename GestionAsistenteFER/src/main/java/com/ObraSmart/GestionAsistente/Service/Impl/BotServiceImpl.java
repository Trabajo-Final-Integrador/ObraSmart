package com.ObraSmart.GestionAsistente.Service.Impl;

import com.ObraSmart.GestionAsistente.Service.BotService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BotServiceImpl implements BotService {

    private final Map<String, String> intents = new HashMap<>();

    public BotServiceImpl() {

        intents.put("crear reparacion", "Para crear una reparación: Menú → Reparaciones → Crear.");
        intents.put("reparacion", "Para crear una reparación: Menú → Reparaciones → Crear.");
        intents.put("equipo", "En Equipos podés registrar, consultar y cambiar estados.");
        intents.put("equipos", "Para ver los equipos: Menú → Equipos → Lista.");
        intents.put("estado", "Los equipos pueden estar Disponible, No disponible o En reparación.");
        intents.put("mantenimiento", "ObraSmart maneja mantenimiento correctivo y preventivo.");
        intents.put("mapa", "En Geolocalización podés ver equipos y reparaciones en el mapa.");
        intents.put("stock", "En Stock podés consultar insumos, proveedores y mínimos.");
        intents.put("reportes", "En Reportes podés obtener informes de uso, actividades y stock.");

        intents.put("ayuda", "Puedo ayudarte con reparaciones, equipos, estados, stock, reportes y mapa.");
        intents.put("manual", "Consultas disponibles: crear reparación, equipos, estados, mantenimiento, mapa, stock, reportes.");
        intents.put("tutorial", "Guía rápida: Registrar equipos → Crear reparaciones → Ver mapa → Stock → Reportes.");
    }

    @Override
    public String responder(String mensaje) {
        if (mensaje == null) return null;

        String lower = mensaje.toLowerCase().trim();

        if (lower.split(" ").length > 2)
            return null;

        String r = intents.get(lower);
        if (r == null) return null;

        return "🤖 Bot respondiendo:\n" + r;
    }
}
