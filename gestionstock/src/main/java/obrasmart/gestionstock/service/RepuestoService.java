package obrasmart.gestionstock.service;


import obrasmart.gestionstock.dto.RepuestoDto;

import java.util.List;

public interface RepuestoService {
    RepuestoDto crear(RepuestoDto dto);
    RepuestoDto actualizar(Long id, RepuestoDto dto);
    void eliminar(Long id);
    List<RepuestoDto> listar();
    RepuestoDto buscar(Long id);
}