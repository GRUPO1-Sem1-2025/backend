package com.example.Login.dto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Array;

import com.example.Login.model.Comentario;

public class DtoCalificacion {
	private Integer calificacion;
	private List<Comentario> comentarios = new ArrayList<>();
	public DtoCalificacion() {
		
	}
	public DtoCalificacion(Integer calificacion, List<Comentario> comentarios) {
		super();
		this.calificacion = calificacion;
		this.comentarios = comentarios;
	}
	public Integer getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}
	public List<Comentario> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	
	

}
