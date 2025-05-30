package com.example.Login.dto;

public class DtoCalificarViaje {
	private int calificacion;
	private String comentario;
	private int idViaje;
	
	public DtoCalificarViaje() {}
	
	public DtoCalificarViaje(int calificacion, String comentario, int idViaje) {
		super();
		this.calificacion = calificacion;
		this.comentario = comentario;
		this.idViaje = idViaje;
	}

	public int getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public int getIdViaje() {
		return idViaje;
	}

	public void setIdViaje(int idViaje) {
		this.idViaje = idViaje;
	}
	
	

}
