package obrasmart.gestionstock.service.impl;



import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.MovimientoStockDto;
import obrasmart.gestionstock.entity.movimiento.MovimientoStock;
import obrasmart.gestionstock.entity.repuestos.Repuesto;
import obrasmart.gestionstock.entity.movimiento.TipoMovimiento;
import obrasmart.gestionstock.repository.MovimientoStockRepository;
import obrasmart.gestionstock.repository.RepuestoRepository;
import obrasmart.gestionstock.service.MovimientoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoStockRepository repo;
    private final RepuestoRepository repuestoRepo;

    private MovimientoStockDto map(MovimientoStock m){
        return MovimientoStockDto.builder()
                .id(m.getId())
                .idRepuesto(m.getRepuesto().getId())
                .tipo(m.getTipo())
                .cantidad(m.getCantidad())
                .observacion(m.getObservacion())
                .build();
    }

    @Override
    public MovimientoStockDto registrar(MovimientoStockDto dto) {
        Repuesto r = repuestoRepo.findById(dto.getIdRepuesto()).orElseThrow();

        int nuevo = r.getStock();
        if (dto.getTipo() == TipoMovimiento.ENTRADA) nuevo += dto.getCantidad();
        else if (dto.getTipo() == TipoMovimiento.SALIDA) nuevo -= dto.getCantidad();
        else /* AJUSTE */ nuevo = dto.getCantidad();

        if (nuevo < 0) throw new IllegalArgumentException("Stock insuficiente");

        r.setStock(nuevo);
        repuestoRepo.save(r);

        var m = MovimientoStock.builder()
                .repuesto(r)
                .tipo(dto.getTipo())
                .cantidad(dto.getCantidad())
                .observacion(dto.getObservacion())
                .build();

        return map(repo.save(m));
    }

    @Override
    public List<MovimientoStockDto> listar() {
        return repo.findAll().stream().map(this::map).toList();
    }
}