package com.example.Login.dto;

public class DtoTotalPorMes {
	private String mes;
	private String anio;
	private Double total;
	
	public DtoTotalPorMes() {
	}
	public DtoTotalPorMes(String mes,String anio, Double total) {
		super();
		this.mes = mes;
		this.anio = anio;
		this.total = total;
	}
	
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
}
