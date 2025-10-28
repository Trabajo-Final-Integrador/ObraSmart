package com.ObraSmart.GestionInspeccion.service;

import com.ObraSmart.GestionInspeccion.dto.CreateEmpleadoRequest;
import com.ObraSmart.GestionInspeccion.dto.EmpleadoDTO;
import com.ObraSmart.GestionInspeccion.dto.UpdateEmpleadoRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmpleadoService {
    EmpleadoDTO crear(CreateEmpleadoRequest request, MultipartFile foto);
    EmpleadoDTO actualizar(Long id, UpdateEmpleadoRequest request);
    void bajaLogica(Long id);
    List<EmpleadoDTO> listar();
    List<EmpleadoDTO> buscarPorApellido(String apellido);
}
