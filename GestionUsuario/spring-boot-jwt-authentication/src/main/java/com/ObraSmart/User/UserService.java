package com.ObraSmart.User;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository; 

    @Transactional
    public UserResponse updateUser(UserRequest userRequest) {
       
        User user = User.builder()
        .id(userRequest.id)
        .firstname(userRequest.getFirstname())
        .lastname(userRequest.lastname)
        .status(Status.ACTIVE)
        .role(Role.TECNICO)
        .build();
        
        userRepository.updateUser(user.id, user.firstname, user.lastname, user.firstname, user.status.toString());

        return new UserResponse("El usuario se registró satisfactoriamente");
    }

    public UserDTO getUser(Integer id) {
        User user= userRepository.findById(id).orElse(null);
       
        if (user!=null)
        {
            UserDTO userDTO = UserDTO.builder()
            .id(user.id)
            .username(user.username)
            .firstname(user.firstname)
            .lastname(user.lastname)
            .Role(user.role.toString())
            .Status(user.status.toString())
            .build();
            return userDTO;
        }
        return null;
    }
}
