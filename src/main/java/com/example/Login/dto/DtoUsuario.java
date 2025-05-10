package com.example.Login.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.example.Login.model.CompraPasaje;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

public class DtoUsuario {
	private int id;
	private String nombre;
	private String apellido;
	private Date fechaNac;
	private String email;
	private String categoria;
	private String ci;
	private boolean activo;
	private int rol; //100 USUAIO FINAL 200 VENDEDOR 300 ADMIN
	private Integer cod_empleado;
	
	public DtoUsuario() {
		
	}
	public DtoUsuario(int id, String nombre, String apellido, Date fechaNac, String email, String categoria, String ci,
			boolean activo, int rol, Integer cod_empleado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNac = fechaNac;
		this.email = email;
		this.categoria = categoria;
		this.ci = ci;
		this.activo = activo;
		this.rol = rol;
		this.cod_empleado = cod_empleado;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getFechaNac() {
		return fechaNac;
	}
	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public int getRol() {
		return rol;
	}
	public void setRol(int rol) {
		this.rol = rol;
	}
	public Integer getCod_empleado() {
		return cod_empleado;
	}
	public void setCod_empleado(Integer cod_empleado) {
		this.cod_empleado = cod_empleado;
	}
	
	
	
	
	


}
