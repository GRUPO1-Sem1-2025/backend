package com.example.Login.dto;

import java.util.ArrayList;
import java.util.List;

public class DtoRespuestaCompraPasaje {
	private List<Integer> asientosOcupados;
    private EstadoCompra estado;
    private int idCompra;
    
    public DtoRespuestaCompraPasaje () {
    	this.asientosOcupados = new ArrayList<>();    	
    }
	public DtoRespuestaCompraPasaje(List<Integer> asientosOcupados, EstadoCompra estado, int icCompra) {
		super();
		this.asientosOcupados = asientosOcupados;
		this.estado = estado;
		this.idCompra = idCompra;
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
	public int getIdCompra() {
		return idCompra;
	}
	public void setIdCompra(int idCompra) {
		this.idCompra = idCompra;
	}
	
}
