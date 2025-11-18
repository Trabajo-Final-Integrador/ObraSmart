package obrasmart.gestionstock.service;


import obrasmart.gestionstock.dto.MovimientoStockDto;

import java.util.List;

public interface MovimientoService {
    MovimientoStockDto registrar(MovimientoStockDto dto);
    List<MovimientoStockDto> listar();
}