package com.example.Login.dto;
import java.util.ArrayList;
import java.util.List;

public class DtoRespuestaVentaPasaje {
	private List<Integer> asientosOcupados;
	private List<Integer> asientosInexistentes;
	private EstadoCompra estado;
	private int idCompra;
	private int asientosComprados;
	private int descuento;
	private int codigoSalida; 

	public DtoRespuestaVentaPasaje() {
		this.asientosOcupados = new ArrayList<>();
	}

	public DtoRespuestaVentaPasaje(List<Integer> asientosOcupados,List<Integer> asientosInexistentes, EstadoCompra estado, int icCompra,
			int asientosComprados, int descuento, int codigoSalida) {
		super();
		this.asientosOcupados = asientosOcupados;
		this.estado = estado;
		this.idCompra = idCompra;
		this.asientosComprados = asientosComprados;
		this.descuento = descuento;
		this.asientosInexistentes = asientosInexistentes;
		this.codigoSalida = codigoSalida;
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

	public List<Integer> getAsientosInexistentes() {
		return asientosInexistentes;
	}

	public void setAsientosInexistentes(List<Integer> asientosInexistentes) {
		this.asientosInexistentes = asientosInexistentes;
	}

	public int getCodigoSalida() {
		return codigoSalida;
	}

	public void setCodigoSalida(int codigoSalida) {
		this.codigoSalida = codigoSalida;
	}
	

}
