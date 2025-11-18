package obrasmart.gestionstock.service;


import obrasmart.gestionstock.dto.OrdenCompraDto;

import java.util.List;

public interface OrdenCompraService {
    OrdenCompraDto crear(OrdenCompraDto dto);
    List<OrdenCompraDto> listar();
}