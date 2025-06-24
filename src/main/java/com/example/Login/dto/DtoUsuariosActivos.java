package com.example.Login.dto;

public class DtoUsuariosActivos {
	private int activos;
	private int inactivos;
	
	public DtoUsuariosActivos() {
		
	}
	
	public DtoUsuariosActivos(int activos, int inactivos) {
		super();
		this.activos = activos;
		this.inactivos = inactivos;
	}
	public int getActivos() {
		return activos;
	}
	public void setActivos(int activos) {
		this.activos = activos;
	}
	public int getInactivos() {
		return inactivos;
	}
	public void setInactivos(int inactivos) {
		this.inactivos = inactivos;
	}
	
	
}
