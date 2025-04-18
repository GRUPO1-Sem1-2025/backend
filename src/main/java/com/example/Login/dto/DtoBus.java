package com.example.Login.dto;

public class DtoBus {
	private String marca;
	private int cant_asientos;
	private boolean activo;
	
	public DtoBus() {
		
	}
	
	public DtoBus(String marca, int cant_asientos, boolean activo) {
		super();
		this.marca = marca;
		this.cant_asientos = cant_asientos;
		this.activo = activo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public int getCant_asientos() {
		return cant_asientos;
	}

	public void setCant_asientos(int cant_asientos) {
		this.cant_asientos = cant_asientos;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	
	
}
