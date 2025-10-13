package com.ObraSmart.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    int id;
    String username;
    String firstname;
    String email;//agregue este
    String lastname;
    String role;
    String status;
}
