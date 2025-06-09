package com.example.Login.dto;

public class DtoViajeIdDestino {
	private int id;
	private String origenDestino;
	public DtoViajeIdDestino() {
		
	}
	public DtoViajeIdDestino(int id, String origenDestino) {
		super();
		this.id = id;
		this.origenDestino = origenDestino;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrigenDestino() {
		return origenDestino;
	}
	public void setOrigenDestino(String origenDestino) {
		this.origenDestino = origenDestino;
	}
	
}
