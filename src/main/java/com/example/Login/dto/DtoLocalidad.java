package com.example.Login.dto;

public class DtoLocalidad {
	private boolean activo;
	private String departamento;
	private String nombre;
	
	public DtoLocalidad() {
		
	}
	
	public DtoLocalidad(boolean activo, String departamento, String nombre) {
		super();
		this.activo = activo;
		this.departamento = departamento;
		this.nombre = nombre;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
