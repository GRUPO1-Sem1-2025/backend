package com.example.Login.dto;

import java.util.ArrayList;
import java.util.List;

public class DtoRespuestaCompraPasaje {
	private List<Integer> asientosOcupados;
    private EstadoCompra estado;
    
    public DtoRespuestaCompraPasaje () {
    	this.asientosOcupados = new ArrayList<>();    	
    }
	public DtoRespuestaCompraPasaje(List<Integer> asientosOcupados, EstadoCompra estado) {
		super();
		this.asientosOcupados = asientosOcupados;
		this.estado = estado;
	}
	public List<Integer> getAsientosOcupados() {
		return asientosOcupados;
	}
	public void setAsientosOcupados(List<Integer> asientosOcupados) {
		this.asientosOcupados = asientosOcupados;
	}
	public EstadoCompra getEstado() {
		return estado;
	}
	public void setEstado(EstadoCompra estado) {
		this.estado = estado;
	}
}
