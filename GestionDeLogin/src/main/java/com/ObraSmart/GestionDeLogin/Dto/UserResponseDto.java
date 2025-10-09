package com.ObraSmart.GestionDeLogin.Dto;

import com.ObraSmart.GestionDeLogin.Entity.Role;
import com.ObraSmart.GestionDeLogin.Entity.UserStatus;

public class UserResponseDto {

    private Long id;
    private String username;
    private String firstname;
    private String lastName;
    private String email;
    private Role role;
    private UserStatus status;

    public UserResponseDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
}
