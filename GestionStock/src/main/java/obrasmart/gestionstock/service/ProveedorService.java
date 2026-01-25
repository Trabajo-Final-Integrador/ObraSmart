package obrasmart.gestionstock.service;



import obrasmart.gestionstock.dto.ProveedorCreateDTO;
import obrasmart.gestionstock.dto.ProveedorDto;
import obrasmart.gestionstock.dto.ProveedorListadoDTO;
import obrasmart.gestionstock.dto.ProveedorUpdateDTO;

import java.util.List;

public interface ProveedorService {
    ProveedorDto crear(ProveedorCreateDTO dto);

    ProveedorDto actualizar(Long id, ProveedorUpdateDTO dto);

    ProveedorDto buscar(Long id);

    List<ProveedorListadoDTO> listarLite();


    List<ProveedorDto> getByEstado(String estado);

    List<ProveedorDto> search(String search);

    List<ProveedorDto> searchByEstado(String estado, String search);

    void eliminar(Long id);

    void cambiarEstado(Long id, String nuevoEstado);



    boolean existsByCuit(String cuit);
}