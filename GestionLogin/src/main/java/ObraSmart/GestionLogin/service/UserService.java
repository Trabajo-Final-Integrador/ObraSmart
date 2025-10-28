package ObraSmart.GestionLogin.service;

import ObraSmart.GestionLogin.dto.CreateUserRequest;
import ObraSmart.GestionLogin.dto.UpdateUserRequest;
import ObraSmart.GestionLogin.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO create(CreateUserRequest req);
    UserDTO update(Long id, UpdateUserRequest req);
    void deleteLogical(Long id); // baja lógica => status INACTIVO
    UserDTO get(Long id);
    List<UserDTO> list();
}
