package com.example.Login.dto;

import com.example.Login.model.Comentario;

public class DtoCalificarViaje {
	private int calificacion;
	private Comentario comentario;
	private int idViaje;
	
	public DtoCalificarViaje() {}
	
	public DtoCalificarViaje(int calificacion, Comentario comentario, int idViaje) {
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

	public Comentario getComentario() {
		return comentario;
	}

	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}

	public int getIdViaje() {
		return idViaje;
	}

	public void setIdViaje(int idViaje) {
		this.idViaje = idViaje;
	}
	
	

}
