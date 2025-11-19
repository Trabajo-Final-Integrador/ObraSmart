package obrasmart.gestionstock.service.impl;


import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.CategoriaRepuestoDto;
import obrasmart.gestionstock.entity.repuestos.CategoriaRepuesto;
import obrasmart.gestionstock.repository.CategoriaRepuestoRepository;
import obrasmart.gestionstock.service.CategoriaRepuestoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaRepuestoServiceImpl implements CategoriaRepuestoService {

    private final CategoriaRepuestoRepository repository;

    @Override
    public List<CategoriaRepuestoDto> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public CategoriaRepuestoDto buscar(Long id) {
        CategoriaRepuesto categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con id: " + id));
        return toDto(categoria);
    }

    @Override
    public CategoriaRepuestoDto crear(CategoriaRepuestoDto dto) {
        CategoriaRepuesto categoria = new CategoriaRepuesto();
        categoria.setNombre(dto.getNombre());

        categoria = repository.save(categoria);
        return toDto(categoria);
    }

    @Override
    public CategoriaRepuestoDto actualizar(Long id, CategoriaRepuestoDto dto) {
        CategoriaRepuesto categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con id: " + id));

        categoria.setNombre(dto.getNombre());
        categoria = repository.save(categoria);

        return toDto(categoria);
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Categoría no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }

    // --------- Mappers ---------

    private CategoriaRepuestoDto toDto(CategoriaRepuesto c) {
        return CategoriaRepuestoDto.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .build();
    }
}