package com.ObraSmart.GestionGeolocalizacion.Service;

import com.ObraSmart.GestionGeolocalizacion.Dto.EquipoUbicacionDTO;
import java.util.List;

public interface IEquiposClientService {
    List<EquipoUbicacionDTO> obtenerTodos();
}
