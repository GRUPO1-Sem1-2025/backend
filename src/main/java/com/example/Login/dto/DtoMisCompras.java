package com.example.Login.dto;

import java.util.List;

public class DtoMisCompras {
	private int viajeId;
	private List<Integer> numerosDeAsiento;
	EstadoCompra estadoCompra;
	private int compraId;
	private int descuento;

	public DtoMisCompras() {

	}

	public DtoMisCompras(int viajeId, List<Integer> numerosDeAsiento, EstadoCompra estadoCompra, int compraId,
			int descuento) {
		super();
		this.viajeId = viajeId;
		this.numerosDeAsiento = numerosDeAsiento;
		this.estadoCompra = estadoCompra;
		this.compraId = compraId;
		this.descuento = descuento;
	}

	public int getDescuento() {
		return descuento;
	}

	public void setDescuento(int descuento) {
		this.descuento = descuento;
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

	public int getCompraId() {
		return compraId;
	}

	public void setCompraId(int compraId) {
		this.compraId = compraId;
	}

}
