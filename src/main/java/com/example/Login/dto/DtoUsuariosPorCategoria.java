package com.example.Login.dto;

public class DtoUsuariosPorCategoria {
	private String categoria=null;
	private int cantidad=0;
	
	public DtoUsuariosPorCategoria() {
		
	}
	public DtoUsuariosPorCategoria(String categoria, int cantidad) {
		super();
		this.categoria = categoria;
		this.cantidad = cantidad;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	
	
}
