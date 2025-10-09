package com.ObraSmart.GestionDeLogin.Dto;

import com.ObraSmart.GestionDeLogin.Entity.Role;

public class RegisterUserDto {

    private String username;
    private String password;
    private String firstname;
    private String lastName;
    private String email;
    private Role role;

    public RegisterUserDto() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
