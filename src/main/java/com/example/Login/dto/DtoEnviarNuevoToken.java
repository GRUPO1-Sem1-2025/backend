package com.example.Login.dto;

public class DtoEnviarNuevoToken {
	private String idUsuario;
	private String titulo;
	private String msj;
	public DtoEnviarNuevoToken() {
		
	}
	public DtoEnviarNuevoToken(String idUsuario, String titulo, String msj) {
		super();
		this.idUsuario = idUsuario;
		this.titulo = titulo;
		this.msj = msj;
	}
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMsj() {
		return msj;
	}
	public void setMsj(String msj) {
		this.msj = msj;
	}
	
	
	
	

}
