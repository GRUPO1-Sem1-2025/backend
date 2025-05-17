package com.example.Login.dto;

public class DtoNewUsuariosPorMes {
	private String mes;
	private long cantidad;
	
	public DtoNewUsuariosPorMes() {
		
	}
	public DtoNewUsuariosPorMes(String mes, long cantidad) {
		super();
		this.mes = mes;
		this.cantidad = cantidad;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public long getCantidad() {
		return cantidad;
	}
	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
