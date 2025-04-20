package com.example.Login.dto;

import java.util.List;

public class DtoCompraPasaje {
	private Integer usuarioId;
	private Integer vendedorId;
    private int viajeId;
    private List<Integer> numerosDeAsiento;
    
    public DtoCompraPasaje() {
    	
    }

	public DtoCompraPasaje(Integer usuarioId, Integer vendedorId, int viajeId, List<Integer> numerosDeAsiento) {
		super();
		this.usuarioId = usuarioId;
		this.vendedorId = vendedorId;
		this.viajeId = viajeId;
		this.numerosDeAsiento = numerosDeAsiento;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
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

}
