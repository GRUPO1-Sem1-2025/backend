package com.example.Login.dto;

public class DtoBus {
	private int id;
	private String marca;
	private String matricula;
	private int cant_asientos;
	private boolean activo;
	private String localidad_actual;
	
	public DtoBus() {
		
	}
	
	public DtoBus(int id, String marca, String matricula, int cant_asientos, boolean activo, String localidad_actual) {
		super();
		this.id = id;
		this.matricula = matricula;
		this.marca = marca;
		this.cant_asientos = cant_asientos;
		this.activo = activo;
		this.localidad_actual = localidad_actual;
	}

	
	
	public String getLocalidad_actual() {
		return localidad_actual;
	}

	public void setLocalidad_actual(String localidad_actual) {
		this.localidad_actual = localidad_actual;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	
	
}
