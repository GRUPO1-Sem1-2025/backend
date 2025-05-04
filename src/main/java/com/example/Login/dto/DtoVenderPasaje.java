package com.example.Login.dto;

import java.util.List;

public class DtoVenderPasaje {
	private String emailCliente;
	private Integer vendedorId;
    private int viajeId;
    private List<Integer> numerosDeAsiento;
    EstadoCompra estadoCompra;
    
    public DtoVenderPasaje() {
    	
    }
    
	public DtoVenderPasaje(String emailCliente, Integer vendedorId, int viajeId, List<Integer> numerosDeAsiento,
			EstadoCompra estadoCompra) {
		super();
		this.emailCliente = emailCliente;
		this.vendedorId = vendedorId;
		this.viajeId = viajeId;
		this.numerosDeAsiento = numerosDeAsiento;
		this.estadoCompra = estadoCompra;
	}
	public String getEmailCliente() {
		return emailCliente;
	}
	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}
	public Integer getVendedorId() {
		return vendedorId;
	}
	public void setVendedorId(Integer vendedorId) {
		this.vendedorId = vendedorId;
	}
	public int getViajeId() {
		return viajeId;
	}
	public void setViajeId(int viajeId) {
		this.viajeId = viajeId;
	}
	public List<Integer> getNumerosDeAsiento() {
		return numerosDeAsiento;
	}
	public void setNumerosDeAsiento(List<Integer> numerosDeAsiento) {
		this.numerosDeAsiento = numerosDeAsiento;
	}
	public EstadoCompra getEstadoCompra() {
		return estadoCompra;
	}
	public void setEstadoCompra(EstadoCompra estadoCompra) {
		this.estadoCompra = estadoCompra;
	}
    
    

}


