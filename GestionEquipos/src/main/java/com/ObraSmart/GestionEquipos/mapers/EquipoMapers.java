package com.ObraSmart.GestionEquipos.mapers;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;
import com.ObraSmart.GestionEquipos.entity.Equipo;

public class EquipoMapers {
    public static EquipoDTO toDTO(Equipo equipo) {

        EquipoDTO dto = new EquipoDTO();
        dto.setId(equipo.getId());
        dto.setNombre(equipo.getNombre());
        dto.setIdTipoEquipo(equipo.getTipoEquipo() != null ? equipo.getTipoEquipo().getId() : null);
        dto.setIdMarca(equipo.getMarca() != null ? equipo.getMarca().getId() : null);
        dto.setIdModelo(equipo.getModelo() != null ? equipo.getModelo().getId() : null);
        dto.setNumeroSerie(equipo.getNumeroSerie());
        dto.setAnioFabricacion(equipo.getAnioFabricacion());
        dto.setPotenciaHp(equipo.getPotenciaHp());
        dto.setCombustible(equipo.getCombustible());
        dto.setEstadoOperativo(equipo.getEstadoOperativo());
        dto.setKilometrajeHorasUso(equipo.getKilometrajeHorasUso());
        dto.setFechaUltimoMantenimiento(equipo.getFechaUltimoMantenimiento());
        dto.setProximoMantenimiento(equipo.getProximoMantenimiento());
        dto.setResponsableMantenimiento(equipo.getResponsableMantenimiento());
        dto.setSeguroVigente(equipo.getSeguroVigente());
        dto.setFechaVencimientoSeguro(equipo.getFechaVencimientoSeguro());
        dto.setUbicacionActual(equipo.getUbicacionActual());
        dto.setActivo(equipo.getActivo());
        dto.setNumeroPatente(equipo.getNumeroPatente());

        return dto;
    }

    public static Equipo toEntity(EquipoDTO dto) {
        Equipo equipo = new Equipo();
        equipo.setId(dto.getId());
        equipo.setNombre(dto.getNombre());
        equipo.setNumeroSerie(dto.getNumeroSerie());
        equipo.setAnioFabricacion(dto.getAnioFabricacion());
        equipo.setPotenciaHp(dto.getPotenciaHp());
        equipo.setCombustible(dto.getCombustible());
        equipo.setEstadoOperativo(dto.getEstadoOperativo());
        equipo.setKilometrajeHorasUso(dto.getKilometrajeHorasUso());
        equipo.setFechaUltimoMantenimiento(dto.getFechaUltimoMantenimiento());
        equipo.setProximoMantenimiento(dto.getProximoMantenimiento());
        equipo.setResponsableMantenimiento(dto.getResponsableMantenimiento());
        equipo.setSeguroVigente(dto.getSeguroVigente());
        equipo.setFechaVencimientoSeguro(dto.getFechaVencimientoSeguro());
        equipo.setUbicacionActual(dto.getUbicacionActual());
        equipo.setActivo(dto.getActivo());
        equipo.setNumeroPatente(dto.getNumeroPatente());

        return equipo;
    }
}
