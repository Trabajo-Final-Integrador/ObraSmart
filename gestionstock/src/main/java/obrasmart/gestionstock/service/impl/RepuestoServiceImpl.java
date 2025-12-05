package obrasmart.gestionstock.service.impl;


import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.RepuestoDto;
import obrasmart.gestionstock.entity.repuestos.CategoriaRepuesto;
import obrasmart.gestionstock.entity.repuestos.Repuesto;
import obrasmart.gestionstock.repository.CategoriaRepuestoRepository;
import obrasmart.gestionstock.repository.RepuestoRepository;
import obrasmart.gestionstock.service.RepuestoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class RepuestoServiceImpl implements RepuestoService {

    private final RepuestoRepository repo;
    private final CategoriaRepuestoRepository catRepo;

    private RepuestoDto map(Repuesto r){
        return RepuestoDto.builder()
                .id(r.getId())
                .codigo(r.getCodigo())
                .nombre(r.getNombre())
                .idCategoria(r.getCategoria().getId())
                .stock(r.getStock())
                .stockMinimo(r.getStockMinimo())
                .unidadMedida(r.getUnidadMedida())
                .build();
    }
    private void fill(Repuesto r, RepuestoDto d){
        r.setCodigo(d.getCodigo());
        r.setNombre(d.getNombre());
        CategoriaRepuesto c = catRepo.findById(d.getIdCategoria()).orElseThrow();
        r.setCategoria(c);
        r.setStock(d.getStock());
        r.setStockMinimo(d.getStockMinimo());
        r.setUnidadMedida(d.getUnidadMedida());
    }

    @Override
    public RepuestoDto crear(RepuestoDto dto) {
        if (repo.existsByCodigo(dto.getCodigo())) throw new IllegalArgumentException("Código duplicado");
        var r = new Repuesto();
        fill(r, dto);
        return map(repo.save(r));
    }

    @Override
    public RepuestoDto actualizar(Long id, RepuestoDto dto) {
        var r = repo.findById(id).orElseThrow();
        fill(r, dto);
        return map(repo.save(r));
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<RepuestoDto> listar() {
        return repo.findAll().stream().map(this::map).toList();
    }

    @Override
    public RepuestoDto buscar(Long id) {
        return repo.findById(id).map(this::map).orElseThrow();
    }
}