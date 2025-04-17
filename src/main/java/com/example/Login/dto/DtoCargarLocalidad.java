package com.example.Login.dto;

public class DtoCargarLocalidad {
	private int id_bus;
	private String nombreLocalidad;
	
	public DtoCargarLocalidad() {
		
	}
	public DtoCargarLocalidad(int id_bus, String nombreLocalidad) {
		super();
		this.id_bus = id_bus;
		this.nombreLocalidad = nombreLocalidad;
	}
	
	public int getId_bus() {
		return id_bus;
	}
	public void setId_bus(int id_bus) {
		this.id_bus = id_bus;
	}
	public String getNombreLocalidad() {
		return nombreLocalidad;
	}
	public void setNombreLocalidad(String nombreLocalidad) {
		this.nombreLocalidad = nombreLocalidad;
	}
	
	

}
