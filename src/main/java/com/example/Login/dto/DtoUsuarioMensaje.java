package com.example.Login.dto;

public class DtoUsuarioMensaje {
	private int resultado;
	private DtoUsuario dtoUsuario;
	
	public DtoUsuarioMensaje() {
		
	}
	public DtoUsuarioMensaje(int resultado, DtoUsuario dtoUsuario) {
		super();
		this.resultado = resultado;
		this.dtoUsuario = dtoUsuario;
	}
	public int getResultado() {
		return resultado;
	}
	public void setResultado(int resultado) {
		this.resultado = resultado;
	}
	public DtoUsuario getDtoUsuario() {
		return dtoUsuario;
	}
	public void setDtoUsuario(DtoUsuario dtoUsuario) {
		this.dtoUsuario = dtoUsuario;
	}
	
	
}
