package com.example.Login.dto;

import java.sql.Date;

public class DtoCrearCuenta {
	private String nombre;
	private String apellido;
	private String rol;
	private String categoria;
	private String email;
	private String ci;
	private Date fechaNac;
	
	public DtoCrearCuenta() {
		
	}
	public DtoCrearCuenta(String nombre, String apellido, String email,String rol, String categoria, String ci, Date fechaNac) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.rol = rol;
		this.categoria = categoria;
		this.ci = ci;
		this.fechaNac = fechaNac;
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
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
	public Date getFechaNac() {
		return fechaNac;
	}
	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	
}
