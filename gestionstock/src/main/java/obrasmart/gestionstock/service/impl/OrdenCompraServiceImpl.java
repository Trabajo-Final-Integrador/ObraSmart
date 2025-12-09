package obrasmart.gestionstock.service.impl;



import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.OrdenCompraDto;
import obrasmart.gestionstock.dto.OrdenCompraListadoDTO;
import obrasmart.gestionstock.entity.ordencompra.EstadoOrdenCompra;
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
                new OrdenCompraDto.Item(i.getRepuesto()
                        .getId(),
                        i.getRepuesto().getNombre(),
                        i.getCantidad(),
                        i.getPrecioUnitario())
        ).toList();

        double total = items.stream()
                .mapToDouble(it -> it.getCantidad() * it.getPrecioUnitario())
                .sum();

        return OrdenCompraDto.builder()
                .id(oc.getId())
                .idProveedor(oc.getProveedor().getId())
                .proveedorNombre(oc.getProveedor().getRazonSocial())
                .estado(oc.getEstado())
                .items(items)
                .totalItems(items.size())
                .total(total)
                .build();
    }

    @Override
    public OrdenCompraDto crear(OrdenCompraDto dto) {
        var prov = provRepo.findById(dto.getIdProveedor()).orElseThrow();

        var oc = new OrdenCompra();
        oc.setProveedor(prov);

        // ⭐ GENERAR CODIGO PROFESIONAL OC-000123
        long next = repo.count() + 1;
        oc.setCodigoOrden(String.format("OC-%06d", next));

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
    public List<OrdenCompraListadoDTO> listar() {
        return repo.findAll()
                .stream()
                .map(this::mapListado)
                .toList();
    }

    private OrdenCompraListadoDTO mapListado(OrdenCompra oc) {

        double total = oc.getItems()
                .stream()
                .mapToDouble(i -> i.getCantidad() * i.getPrecioUnitario())
                .sum();

        return OrdenCompraListadoDTO.builder()
                .id(oc.getId())
                .idProveedor(oc.getProveedor().getId())
                .proveedorNombre(oc.getProveedor().getRazonSocial())
                .estado(oc.getEstado().name())
                .totalItems(oc.getItems().size())
                .total(total)
                .fecha(oc.getFecha())
                .build();
    }
    @Override
    public OrdenCompraDto aprobar(Long id) {
        var oc = repo.findById(id).orElseThrow();

        if(oc.getEstado() != EstadoOrdenCompra.PENDIENTE){
            throw new RuntimeException("Solo se pueden aprobar órdenes PENDIENTE");
        }

        oc.setEstado(EstadoOrdenCompra.APROBADA);

        return map(repo.save(oc));
    }


    @Override
    public OrdenCompraDto recibir(Long id) {
        var oc = repo.findById(id).orElseThrow();
        oc.setEstado(EstadoOrdenCompra.RECIBIDA);
        return map(repo.save(oc));
    }

    @Override
    public OrdenCompraDto cancelar(Long id) {
        var oc = repo.findById(id).orElseThrow();
        oc.setEstado(EstadoOrdenCompra.CANCELADA);
        return map(repo.save(oc));
    }

    @Override
    public OrdenCompraDto obtener(Long id) {
        return map(repo.findById(id).orElseThrow());
    }



}