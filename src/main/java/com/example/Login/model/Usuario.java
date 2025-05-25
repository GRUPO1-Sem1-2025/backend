package com.example.Login.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import com.example.Login.dto.categoriaUsuario;

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
private categoriaUsuario categoria;
private String ci;
private LocalDate fechaCreacion;
//hasta aca
private String password;
private int codigo;
private boolean activo;
private int rol; //100 USUAIO FINAL 200 VENDEDOR 300 ADMIN

@Column(name = "contraseniaValida", nullable = true)
private Boolean contraseniaValida;

@Column(name = "cod_empleado", nullable = true)
private Integer cod_empleado;

@OneToMany(mappedBy = "usuario")
private List<CompraPasaje> compraPasajes;

@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Token> tokens;



public Usuario() {
	
}

public Usuario(int id, String nombre, String apellido, Date fechaNac, String email, categoriaUsuario categoria, String ci,
		LocalDate fechaCreacion, String password, boolean activo, int rol, int cod_empleado, Boolean contrasenia) {
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
	this.codigo = codigo;
	this.activo = activo;
	this.rol = rol;
	this.cod_empleado= cod_empleado;
	this.contraseniaValida = contrasenia;
	this.tokens = new ArrayList<>();
}

//Getters y Setters


public String getApellido() {
	return apellido;
}
public Boolean isContraseniaValida() {
	return contraseniaValida;
}

public void setContraseniaValida(Boolean contraseniaValida) {
	this.contraseniaValida = contraseniaValida;
}

public List<CompraPasaje> getCompraPasajes() {
	return compraPasajes;
}

public void setCompraPasajes(List<CompraPasaje> compraPasajes) {
	this.compraPasajes = compraPasajes;
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
public categoriaUsuario getCategoria() {
	return categoria;
}
public void setCategoria(categoriaUsuario categoria) {
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

public int getCodigo() {
	return codigo;
}

public void setCodigo(int codigo) {
	this.codigo = codigo;
}

public Integer getCod_empleado() {
	return cod_empleado;
}

public void setCod_empleado(Integer cod_empleado) {
	this.cod_empleado = cod_empleado;
}

public List<Token> getTokens() {
	return tokens;
}

public void setTokens(List<Token> tokens) {
	this.tokens = tokens;
}

}
