package com.example.Login.dto;

public class DtoUsuariosPorRol {
	private String rol;
	private long cantidad;
	public DtoUsuariosPorRol() {
	
	}
	public DtoUsuariosPorRol(String rol, long cantidad) {
		super();
		this.rol = rol;
		this.cantidad = cantidad;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public long getCantidad() {
		return cantidad;
	}
	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
