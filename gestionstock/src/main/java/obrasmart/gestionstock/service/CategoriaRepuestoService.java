package obrasmart.gestionstock.service;

import obrasmart.gestionstock.dto.CategoriaRepuestoDto;

import java.util.List;

public interface CategoriaRepuestoService {

        List<CategoriaRepuestoDto> listar();

        CategoriaRepuestoDto buscar(Long id);

        CategoriaRepuestoDto crear(CategoriaRepuestoDto dto);

        CategoriaRepuestoDto actualizar(Long id, CategoriaRepuestoDto dto);

        void eliminar(Long id);

}
