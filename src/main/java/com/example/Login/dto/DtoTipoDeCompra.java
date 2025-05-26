package com.example.Login.dto;

public class DtoTipoDeCompra {
	private String tipoVenta;
	private Long cantidad;
	
	public DtoTipoDeCompra() {
		
	}
	public DtoTipoDeCompra(String tipoVenta, Long cantidad) {
		super();
		this.tipoVenta = tipoVenta;
		this.cantidad = cantidad;
	}
	public String getTipoCompra() {
		return tipoVenta;
	}
	public void setTipoCompra(String tipoVenta) {
		this.tipoVenta = tipoVenta;
	}
	public Long getCantidad() {
		return cantidad;
	}
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}
	
	
	
	

}
