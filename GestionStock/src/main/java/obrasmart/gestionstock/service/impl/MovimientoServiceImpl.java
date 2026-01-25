package obrasmart.gestionstock.service.impl;



import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.MovimientoStockDto;
import obrasmart.gestionstock.dto.MovimientoStockResponseDto;
import obrasmart.gestionstock.entity.movimiento.MovimientoStock;
import obrasmart.gestionstock.entity.repuestos.Repuesto;
import obrasmart.gestionstock.entity.movimiento.TipoMovimiento;
import obrasmart.gestionstock.repository.MovimientoStockRepository;
import obrasmart.gestionstock.repository.RepuestoRepository;
import obrasmart.gestionstock.service.MovimientoService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service @RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoStockRepository repo;
    private final RepuestoRepository repuestoRepo;


    // ---------------------------
    //       MAPPER RESPONSE
    // ---------------------------
    private MovimientoStockResponseDto mapToResponse(MovimientoStock m) {
        return MovimientoStockResponseDto.builder()
                .id(m.getId())
                .idRepuesto(m.getRepuesto().getId())
                .repuestoNombre(m.getRepuesto().getNombre())
                .repuestoCodigo(m.getRepuesto().getCodigo())
                .tipo(m.getTipo())
                .cantidad(m.getCantidad())
                .observacion(m.getObservacion())
                .fecha(m.getFecha())
                .build();
    }

    // ---------------------------
    //       REGISTRAR
    // ---------------------------
    @Override
    public MovimientoStockResponseDto registrar(MovimientoStockDto dto) {

        Repuesto rep = repuestoRepo.findById(dto.getIdRepuesto())
                .orElseThrow(() -> new IllegalArgumentException("Repuesto no encontrado"));

        int nuevoStock = rep.getStock();

        switch (dto.getTipo()) {
            case ENTRADA -> nuevoStock += dto.getCantidad();
            case SALIDA -> {
                nuevoStock -= dto.getCantidad();
                if (nuevoStock < 0)
                    throw new IllegalArgumentException("Stock insuficiente");
            }
            case AJUSTE -> nuevoStock = dto.getCantidad();
        }

        rep.setStock(nuevoStock);
        repuestoRepo.save(rep);

        MovimientoStock mov = MovimientoStock.builder()
                .repuesto(rep)
                .tipo(dto.getTipo())
                .cantidad(dto.getCantidad())
                .observacion(dto.getObservacion())
                .fecha(Instant.now())
                .build();

        MovimientoStock saved = repo.save(mov);

        return mapToResponse(saved);
    }

    // ---------------------------
    //          LISTAR
    // ---------------------------
    @Override
    public List<MovimientoStockResponseDto> listar() {
        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}