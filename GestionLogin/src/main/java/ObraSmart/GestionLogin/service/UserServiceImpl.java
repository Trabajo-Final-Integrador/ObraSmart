package ObraSmart.GestionLogin.service;

import ObraSmart.GestionLogin.dto.CreateUserRequest;
import ObraSmart.GestionLogin.dto.UpdateUserRequest;
import ObraSmart.GestionLogin.dto.UserDTO;
import ObraSmart.GestionLogin.entity.Status;
import ObraSmart.GestionLogin.entity.User;
import ObraSmart.GestionLogin.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    @Override
    public UserDTO create(CreateUserRequest r){
        var user = User.builder()
                .username(r.getUsername())
                .email(r.getEmail())
                .password(encoder.encode(r.getPassword()))
                .firstname(r.getFirstname())
                .lastname(r.getLastname())
                .role(r.getRole())
                .status(r.getStatus())
                .build();
        user = repo.save(user);
        return toDTO(user);
    }

    @Override
    public UserDTO update(Long id, UpdateUserRequest r){
        var u = repo.findById(id).orElseThrow();
        u.setEmail(r.getEmail());
        u.setRole(r.getRole());
        u.setStatus(r.getStatus());
        u = repo.save(u);
        return toDTO(u);
    }

    @Override
    public void deleteLogical(Long id){
        var u = repo.findById(id).orElseThrow();
        u.setStatus(Status.INACTIVO);
    }

    @Transactional
    @Override
    public UserDTO get(Long id){
        return toDTO(repo.findById(id).orElseThrow());
    }

    @Override
    public List<UserDTO> list(){
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    private UserDTO toDTO(User u){
        return new UserDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getStatus());
    }
}
