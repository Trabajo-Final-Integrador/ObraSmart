package com.ObraSmart.GestionInspeccion.service;

import com.ObraSmart.GestionInspeccion.dto.CreateEmpleadoRequest;
import com.ObraSmart.GestionInspeccion.dto.EmpleadoDTO;
import com.ObraSmart.GestionInspeccion.dto.UpdateEmpleadoRequest;
import com.ObraSmart.GestionInspeccion.entity.Empleado;
import com.ObraSmart.GestionInspeccion.entity.Status;
import com.ObraSmart.GestionInspeccion.repository.EmpleadoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository repo;

    @Override
    public EmpleadoDTO crear(CreateEmpleadoRequest request, MultipartFile foto) {
        try {
            Empleado e = Empleado.builder()
                    .nombre(request.getNombre())
                    .apellido(request.getApellido())
                    .dni(request.getDni())
                    .email(request.getEmail())
                    .direccion(request.getDireccion())
                    .telefono(request.getTelefono())
                    .role(request.getRole())
                    .status(Status.ACTIVO)
                    .carnetFoto(foto.getBytes())
                    .build();
            repo.save(e);
            return toDTO(e);
        } catch (IOException ex) {
            throw new RuntimeException("Error al procesar la imagen");
        }
    }

    @Override
    public EmpleadoDTO actualizar(Long id, UpdateEmpleadoRequest request) {
        Empleado e = repo.findById(id).orElseThrow();
        if (request.getDireccion() != null) e.setDireccion(request.getDireccion());
        if (request.getTelefono() != null) e.setTelefono(request.getTelefono());
        if (request.getStatus() != null) e.setStatus(request.getStatus());
        return toDTO(e);
    }

    @Override
    public void bajaLogica(Long id) {
        Empleado e = repo.findById(id).orElseThrow();
        e.setStatus(Status.INACTIVO);
    }

    @Override
    public List<EmpleadoDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<EmpleadoDTO> buscarPorApellido(String apellido) {
        return repo.findByApellidoContainingIgnoreCase(apellido).stream().map(this::toDTO).toList();
    }

    private EmpleadoDTO toDTO(Empleado e) {
        return EmpleadoDTO.builder()
                .id(e.getId())
                .nombre(e.getNombre())
                .apellido(e.getApellido())
                .email(e.getEmail())
                .telefono(e.getTelefono())
                .role(e.getRole())
                .status(e.getStatus())
                .build();
    }
}
