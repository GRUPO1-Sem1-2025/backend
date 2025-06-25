package com.example.Login.dto;

import java.util.ArrayList;
import java.util.List;

public class DtoRespuestaCompraPasaje {
	private List<Integer> asientosOcupados;
	private EstadoCompra estado;
	private int idCompra;
	private int asientosComprados;
	private int descuento;

	public DtoRespuestaCompraPasaje() {
		this.asientosOcupados = new ArrayList<>();
	}

	public DtoRespuestaCompraPasaje(List<Integer> asientosOcupados, EstadoCompra estado, int icCompra,
			int asientosComprados, int descuento) {
		super();
		this.asientosOcupados = asientosOcupados;
		this.estado = estado;
		this.idCompra = idCompra;
		this.asientosComprados = asientosComprados;
		this.descuento = descuento;
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

	public int getAsientosComprados() {
		return asientosComprados;
	}

	public void setAsientosComprados(int asientosComprados) {
		this.asientosComprados = asientosComprados;
	}

	public int getDescuento() {
		return descuento;
	}

	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}

}
