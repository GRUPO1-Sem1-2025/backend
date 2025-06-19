package com.example.Login.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comentario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private Integer idUsuario;
	private Integer calificacion;
	private String comentario;
	@ManyToOne
	@JoinColumn(name = "viaje_id")
	private Viaje viaje;

	public Comentario() {

	}
	
	public Comentario(int id, Integer idUsuario, Integer calificacion, String comentario, Viaje viaje) {
		super();
		this.id = id;
		this.idUsuario = idUsuario;
		this.calificacion = calificacion;
		this.comentario = comentario;
		this.viaje = viaje;
	}


//	public Comentario(Integer idUsuario, String comentario, Viaje viaje) {
//		super();
//		this.idUsuario = idUsuario;
//		this.comentario = comentario;
//		this.viaje = viaje;
//	}


	public Viaje getViaje() {
		return viaje;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}



	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Integer getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}

}
