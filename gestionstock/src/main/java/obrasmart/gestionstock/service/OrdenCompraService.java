package obrasmart.gestionstock.service;


import obrasmart.gestionstock.dto.OrdenCompraDto;
import obrasmart.gestionstock.dto.OrdenCompraListadoDTO;
import obrasmart.gestionstock.entity.ordencompra.OrdenCompra;

import java.util.List;

public interface OrdenCompraService {
    OrdenCompraDto crear(OrdenCompraDto dto);
    List<OrdenCompraListadoDTO> listar();


    OrdenCompraDto aprobar(Long id);

    OrdenCompraDto recibir(Long id);

    OrdenCompraDto cancelar(Long id);

    OrdenCompraDto obtener(Long id);
}