
package com.example.SPP.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

// Pastikan model User memiliki field dan method ini:

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(unique = true)
    private String username;

    private String password;

    private String nama;

    private String role;

    // Constructors
    public User() {}

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}