package com.example.Login.dto;

public class DtoCantidadPorRol {
	private Integer rol;
    private Long cantidad;
    public DtoCantidadPorRol() {
    	
    }
	public DtoCantidadPorRol(Integer rol, Long cantidad) {
		super();
		this.rol = rol;
		this.cantidad = cantidad;
	}
	public Integer getRol() {
		return rol;
	}
	public void setRol(Integer rol) {
		this.rol = rol;
	}
	public Long getCantidad() {
		return cantidad;
	}
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}
    
    
	
}
