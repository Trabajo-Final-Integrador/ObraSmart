package com.ObraSmart.GestionGeolocalizacion.Service;

import com.ObraSmart.GestionGeolocalizacion.Dto.EquipoUbicacionResponse;
import java.util.List;

public interface IUbicacionesService {
    List<EquipoUbicacionResponse> listarParaMapa();
}
