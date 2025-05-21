package com.example.Login.dto;

public class DtoUsuarioToken {
	private int id_usuario;
	private String token;
	public DtoUsuarioToken() {
		
	}
	public DtoUsuarioToken(int id_usuario, String token) {
		super();
		this.id_usuario = id_usuario;
		this.token = token;
	}
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	
	

}
