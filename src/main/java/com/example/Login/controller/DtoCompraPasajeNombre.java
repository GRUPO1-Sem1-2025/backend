package com.example.Login.controller;

import java.util.List;

import com.example.Login.dto.EstadoCompra;

public class DtoCompraPasajeNombre {
	private String nombreUsuario;
	private String nombreVendedor;
    private String origenDestino;
    private List<Integer> numerosDeAsiento;
    EstadoCompra estadoCompra;
    private String fechaHora;
    private Long idCompra;
    
    public DtoCompraPasajeNombre() {
    	
    }
	public DtoCompraPasajeNombre(String nombreUsuario, String nombreVendedor, String origenDestino,
			List<Integer> numerosDeAsiento, EstadoCompra estadoCompra, String fechaHora, Long idCompra) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.nombreVendedor = nombreVendedor;
		this.origenDestino = origenDestino;
		this.numerosDeAsiento = numerosDeAsiento;
		this.estadoCompra = estadoCompra;
		this.fechaHora = fechaHora;
		this.idCompra = idCompra;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getNombreVendedor() {
		return nombreVendedor;
	}
	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}
	public String getOrigenDestino() {
		return origenDestino;
	}
	public void setOrigenDestino(String origenDestino) {
		this.origenDestino = origenDestino;
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
	public String getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	public Long getIdCompra() {
		return idCompra;
	}
	public void setIdCompra(Long idCompra) {
		this.idCompra = idCompra;
	}
	
    
}
