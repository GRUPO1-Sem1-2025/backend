package com.example.Login.dto;

public class DtoCantidadPorRol {
	private String rol;
    private Long cantidad;
    public DtoCantidadPorRol() {
    	
    }
	public DtoCantidadPorRol(String rol, Long cantidad) {
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
	public Long getCantidad() {
		return cantidad;
	}
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}
    
    
	
}
