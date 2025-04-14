package com.example.Login.model;

import java.sql.Date;
import java.time.LocalDate;

import javax.xml.crypto.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String nombre;
//Agregados por el mer
private String apellido;
private Date fechaNac;
private String email;
private String categoria;
private String ci;
private LocalDate fechaCreacion;
//hasta aca
private String password;
private boolean activo;
private int rol;



public Usuario() {
	
}

public Usuario(int id, String nombre, String apellido, Date fechaNac, String email, String categoria, String ci,
		LocalDate fechaCreacion, String password, boolean activo, int rol) {
	super();
	this.id = id;
	this.nombre = nombre;
	this.apellido = apellido;
	this.fechaNac = fechaNac;
	this.email = email;
	this.categoria = categoria;
	this.ci = ci;
	this.fechaCreacion = fechaCreacion;
	this.password = password;
	this.activo = activo;
	this.rol = rol;
}

//Getters y Setters
public String getApellido() {
	return apellido;
}
public void setApellido(String apellido) {
	this.apellido = apellido;
}
public Date getFechaNac() {
	return fechaNac;
}
public void setFechaNac(Date fechaNacimiento) {
	this.fechaNac = fechaNacimiento;
}
public String getCategoria() {
	return categoria;
}
public void setCategoria(String categoria) {
	this.categoria = categoria;
}
public String getCi() {
	return ci;
}
public void setCi(String ci) {
	this.ci = ci;
}
public LocalDate getFechaCreacion() {
	return fechaCreacion;
}
public void setFechaCreacion(LocalDate fechaCreacion) {
	this.fechaCreacion = fechaCreacion;
}
public int getId() { return id; }
public void setId(int id) { this.id = id; }

public int getRol() { return rol; }
public void setRol(int rol) { this.rol = rol; }

public String getNombre() { return nombre; }
public void setNombre(String nombre) { this.nombre = nombre; }

public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }

public String getPassword() { return password; }
public void setPassword(String password) { this.password = password; }

public boolean getActivo() { return activo; }
public void setActivo(boolean activo) { this.activo = activo; }

}
