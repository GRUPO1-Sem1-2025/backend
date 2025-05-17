package com.example.Login.dto;

public class DtoUsuariosPorRolQuery {
	private int rol;
	private long cantidad;
	
	public DtoUsuariosPorRolQuery() {
		
	}
	public DtoUsuariosPorRolQuery(int rol, long cantidad) {
		super();
		this.rol = rol;
		this.cantidad = cantidad;
	}
	public int getRol() {
		return rol;
	}
	public void setRol(int rol) {
		this.rol = rol;
	}
	public long getCantidad() {
		return cantidad;
	}
	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
