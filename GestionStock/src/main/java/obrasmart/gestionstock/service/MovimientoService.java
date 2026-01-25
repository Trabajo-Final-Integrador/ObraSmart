package obrasmart.gestionstock.service;


import obrasmart.gestionstock.dto.MovimientoStockDto;
import obrasmart.gestionstock.dto.MovimientoStockResponseDto;

import java.util.List;

public interface MovimientoService {
    MovimientoStockResponseDto registrar(MovimientoStockDto dto);
    List<MovimientoStockResponseDto> listar();
}