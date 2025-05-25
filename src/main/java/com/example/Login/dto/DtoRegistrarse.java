package com.example.Login.dto;

import java.time.LocalDate;

public class DtoRegistrarse {
	private String nombre;
	private String apellido;
	private String ci;
	private String email;
	private String password;
	private LocalDate fecha_nac;
	private categoriaUsuario categoria;
	
	public DtoRegistrarse() {
		
	}
	
	
	public DtoRegistrarse(String nombre, String apellido, String ci, String email, String password, LocalDate fecha_nac,
			categoriaUsuario categoria) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.ci = ci;
		this.email = email;
		this.password = password;
		this.fecha_nac = fecha_nac;
		this.categoria = categoria;
	}


	public LocalDate getFecha_nac() {
		return fecha_nac;
	}


	public void setFecha_nac(LocalDate fecha_nac) {
		this.fecha_nac = fecha_nac;
	}


	public String getCi() {
		return ci;
	}



	public void setCi(String ci) {
		this.ci = ci;
	}



	public categoriaUsuario getCategoria() {
		return categoria;
	}



	public void setCategoria(categoriaUsuario categoria) {
		this.categoria = categoria;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	

}
