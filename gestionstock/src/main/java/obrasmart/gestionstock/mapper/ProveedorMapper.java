package obrasmart.gestionstock.mapper;


import obrasmart.gestionstock.dto.ProveedorCreateDTO;
import obrasmart.gestionstock.dto.ProveedorDto;
import obrasmart.gestionstock.dto.ProveedorUpdateDTO;
import obrasmart.gestionstock.entity.proveedores.EstadoProveedor;
import obrasmart.gestionstock.entity.proveedores.Proveedor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ProveedorMapper {

    public ProveedorDto toDTO(Proveedor entity) {
        if (entity == null) return null;

        return ProveedorDto.builder()
                .id(entity.getId())
                .razonSocial(entity.getRazonSocial())
                .nombreComercial(entity.getNombreComercial())
                .cuit(entity.getCuit())
                .condicionIVA(entity.getCondicionIVA() )
                .estado(entity.getEstado())
                .telefono(entity.getTelefono())
                .email(entity.getEmail())

                .horarioAtencion(entity.getHorarioAtencion())
                .direccion(entity.getDireccion())
                .ciudad(entity.getCiudad())
                .provincia(entity.getProvincia())
                .codigoPostal(entity.getCodigoPostal())
                .especialidad(entity.getEspecialidad())
                .marcas(entity.getMarcas())
                .tipoProveedor(entity.getTipoProveedor())
                .tiempoEntrega(entity.getTiempoEntrega())
                .pedidoMinimo(entity.getPedidoMinimo())
                .condicionesPago(entity.getCondicionesPago())
                .descuentoVolumen(entity.getDescuentoVolumen())
                .tieneStock(entity.getTieneStock())
                .haceEnvios(entity.getHaceEnvios())
                .zonaCobertura(entity.getZonaCobertura())
                .aceptaDevoluciones(entity.getAceptaDevoluciones())
                .tieneCatalogo(entity.getTieneCatalogo())
                .urlCatalogo(entity.getUrlCatalogo())
                .codigoCliente(entity.getCodigoCliente())
                .fechaUltimaActualizacionPrecios(entity.getFechaUltimaActualizacionPrecios())
                .banco(entity.getBanco())
                .tipoCuenta(entity.getTipoCuenta())
                .cbu(entity.getCbu())
                .observaciones(entity.getObservaciones())
                .ultimaCompra(entity.getUltimaCompra())
                .totalComprado(entity.getTotalComprado())
                .plazoRespuestaPromedio(entity.getPlazoRespuestaPromedio())
                .fechaAlta(entity.getFechaAlta())
                .fechaModificacion(entity.getFechaModificacion())
                .build();
    }

    public Proveedor toEntity(ProveedorCreateDTO dto) {
        if (dto == null) return null;

        return Proveedor.builder()
                .razonSocial(dto.getRazonSocial())
                .nombreComercial(dto.getNombreComercial())
                .cuit(dto.getCuit())
                .condicionIVA(dto.getCondicionIVA())
                .estado(EstadoProveedor.ACTIVO)
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .personaContacto(dto.getPersonaContacto())
                .horarioAtencion(dto.getHorarioAtencion())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .provincia(dto.getProvincia())
                .codigoPostal(dto.getCodigoPostal())
                .especialidad(dto.getEspecialidad())
                .marcas(dto.getMarcas() != null ? dto.getMarcas() : new ArrayList<>())
                .tipoProveedor(dto.getTipoProveedor())
                .tiempoEntrega(dto.getTiempoEntrega())
                .pedidoMinimo(dto.getPedidoMinimo())
                .condicionesPago(dto.getCondicionesPago())
                .descuentoVolumen(dto.getDescuentoVolumen())
                .tieneStock(dto.getTieneStock() != null ? dto.getTieneStock() : false)
                .haceEnvios(dto.getHaceEnvios() != null ? dto.getHaceEnvios() : false)
                .zonaCobertura(dto.getZonaCobertura())
                .aceptaDevoluciones(dto.getAceptaDevoluciones() != null ? dto.getAceptaDevoluciones() : false)
                .tieneCatalogo(dto.getTieneCatalogo() != null ? dto.getTieneCatalogo() : false)
                .urlCatalogo(dto.getUrlCatalogo())
                .codigoCliente(dto.getCodigoCliente())
                .banco(dto.getBanco())
                .tipoCuenta(dto.getTipoCuenta())
                .cbu(dto.getCbu())
                .observaciones(dto.getObservaciones())
                .build();
    }

    public void updateEntityFromDTO(ProveedorUpdateDTO dto, Proveedor entity) {

        if (dto == null || entity == null) return;

        if (dto.getRazonSocial() != null) entity.setRazonSocial(dto.getRazonSocial());
        if (dto.getNombreComercial() != null) entity.setNombreComercial(dto.getNombreComercial());
        if (dto.getCuit() != null) entity.setCuit(dto.getCuit());
        if (dto.getCondicionIVA() != null) entity.setCondicionIVA(dto.getCondicionIVA());
        if (dto.getTelefono() != null) entity.setTelefono(dto.getTelefono());

        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getPersonaContacto() != null) entity.setPersonaContacto(dto.getPersonaContacto());
        if (dto.getHorarioAtencion() != null) entity.setHorarioAtencion(dto.getHorarioAtencion());
        if (dto.getDireccion() != null) entity.setDireccion(dto.getDireccion());
        if (dto.getCiudad() != null) entity.setCiudad(dto.getCiudad());
        if (dto.getProvincia() != null) entity.setProvincia(dto.getProvincia());
        if (dto.getCodigoPostal() != null) entity.setCodigoPostal(dto.getCodigoPostal());
        if (dto.getEspecialidad() != null) entity.setEspecialidad(dto.getEspecialidad());

        // Solo actualizar marcas si trae contenido, evitar pisar con lista vacía
        if (dto.getMarcas() != null && !dto.getMarcas().isEmpty()) {
            entity.setMarcas(dto.getMarcas());
        }

        // asignar TipoProveedor correctamente
        if (dto.getTipoProveedor() != null) entity.setTipoProveedor(dto.getTipoProveedor());

        if (dto.getTiempoEntrega() != null) entity.setTiempoEntrega(dto.getTiempoEntrega());
        if (dto.getPedidoMinimo() != null) entity.setPedidoMinimo(dto.getPedidoMinimo());
        if (dto.getCondicionesPago() != null) entity.setCondicionesPago(dto.getCondicionesPago());
        if (dto.getDescuentoVolumen() != null) entity.setDescuentoVolumen(dto.getDescuentoVolumen());

        if (dto.getTieneStock() != null) entity.setTieneStock(dto.getTieneStock());
        if (dto.getHaceEnvios() != null) entity.setHaceEnvios(dto.getHaceEnvios());

        if (dto.getZonaCobertura() != null) entity.setZonaCobertura(dto.getZonaCobertura());
        if (dto.getAceptaDevoluciones() != null) entity.setAceptaDevoluciones(dto.getAceptaDevoluciones());
        if (dto.getTieneCatalogo() != null) entity.setTieneCatalogo(dto.getTieneCatalogo());
        if (dto.getUrlCatalogo() != null) entity.setUrlCatalogo(dto.getUrlCatalogo());

        if (dto.getCodigoCliente() != null) entity.setCodigoCliente(dto.getCodigoCliente());
        if (dto.getBanco() != null) entity.setBanco(dto.getBanco());
        if (dto.getTipoCuenta() != null) entity.setTipoCuenta(dto.getTipoCuenta());
        if (dto.getCbu() != null) entity.setCbu(dto.getCbu());
        if (dto.getObservaciones() != null) entity.setObservaciones(dto.getObservaciones());

        if (dto.getEstado() != null) {
            entity.setEstado(EstadoProveedor.valueOf(dto.getEstado().toUpperCase()));
        }
    }
}