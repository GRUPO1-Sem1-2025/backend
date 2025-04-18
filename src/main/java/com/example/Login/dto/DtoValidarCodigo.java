package com.example.Login.dto;

public class DtoValidarCodigo {
	private String email;
	private int codigo;
	
	public DtoValidarCodigo() {
		
	}
	
	public DtoValidarCodigo(String email, int codigo) {
		super();
		this.email = email;
		this.codigo = codigo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	

}
