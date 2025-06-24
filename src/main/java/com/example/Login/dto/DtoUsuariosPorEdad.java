package com.example.Login.dto;

public class DtoUsuariosPorEdad {
	private String rango;
	private Integer cantidad;
	
	public DtoUsuariosPorEdad() {}
	
	public DtoUsuariosPorEdad(String rango, Integer cantidad) {
		super();
		this.rango = rango;
		this.cantidad = cantidad;
	}

	public String getRango() {
		return rango;
	}

	public void setRango(String rango) {
		this.rango = rango;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
