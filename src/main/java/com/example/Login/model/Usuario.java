package com.example.Login.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String nombre;
private String email;
private String password;
private boolean activo;

// Getters y Setters
public Usuario() {
	
}
public Usuario(String nombre, String email, String password, boolean activo) {
	this.nombre = nombre;
	this.email = email;
	this.password = password;
	this.activo = activo;
}

public int getId() { return id; }
public void setId(int id) { this.id = id; }

public String getNombre() { return nombre; }
public void setNombre(String nombre) { this.nombre = nombre; }

public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }

public String getPassword() { return password; }
public void setPassword(String password) { this.password = password; }

public boolean getActivo() { return activo; }
public void setActivo(boolean activo) { this.activo = activo; }

}
