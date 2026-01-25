package com.ObraSmart.GestionGeolocalizacion.Service;

import com.ObraSmart.GestionGeolocalizacion.Dto.ReparacionLiteDTO;
import java.util.List;

public interface IReparacionesClientService {
    List<ReparacionLiteDTO> obtenerTodas();
}
