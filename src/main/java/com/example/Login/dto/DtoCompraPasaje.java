package com.example.Login.dto;

import java.util.List;

public class DtoCompraPasaje {
	private int usuarioId;
    private int viajeId;
    private List<Integer> numerosDeAsiento;
    
    public DtoCompraPasaje() {
    	
    }
    
	public DtoCompraPasaje(int usuarioId, int viajeId, List<Integer> numerosDeAsiento) {
		super();
		this.usuarioId = usuarioId;
		this.viajeId = viajeId;
		this.numerosDeAsiento = numerosDeAsiento;
	}

	public int getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
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
