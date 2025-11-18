package obrasmart.gestionstock.service.impl;



import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.OrdenCompraDto;
import obrasmart.gestionstock.entity.ordencompra.OrdenCompra;
import obrasmart.gestionstock.entity.ordencompra.OrdenCompraItem;
import obrasmart.gestionstock.repository.OrdenCompraRepository;
import obrasmart.gestionstock.repository.ProveedorRepository;
import obrasmart.gestionstock.repository.RepuestoRepository;
import obrasmart.gestionstock.service.OrdenCompraService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository repo;
    private final ProveedorRepository provRepo;
    private final RepuestoRepository repRepo;

    private OrdenCompraDto map(OrdenCompra oc){
        var items = oc.getItems().stream().map(i ->
                new OrdenCompraDto.Item(i.getRepuesto().getId(), i.getCantidad(), i.getPrecioUnitario())
        ).toList();

        return OrdenCompraDto.builder()
                .id(oc.getId())
                .idProveedor(oc.getProveedor().getId())
                .estado(oc.getEstado())
                .items(items)
                .build();
    }

    @Override
    public OrdenCompraDto crear(OrdenCompraDto dto) {
        var prov = provRepo.findById(dto.getIdProveedor()).orElseThrow();

        var oc = new OrdenCompra();
        oc.setProveedor(prov);

        var lista = new java.util.ArrayList<OrdenCompraItem>();
        for (var it : dto.getItems()) {
            var rep = repRepo.findById(it.getIdRepuesto()).orElseThrow();
            var item = new OrdenCompraItem();
            item.setOrden(oc);
            item.setRepuesto(rep);
            item.setCantidad(it.getCantidad());
            item.setPrecioUnitario(it.getPrecioUnitario());
            lista.add(item);
        }
        oc.setItems(lista);

        return map(repo.save(oc));
    }

    @Override
    public List<OrdenCompraDto> listar() {
        return repo.findAll().stream().map(this::map).toList();
    }
}