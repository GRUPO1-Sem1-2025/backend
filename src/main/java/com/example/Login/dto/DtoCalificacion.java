package com.example.Login.dto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Array;

public class DtoCalificacion {
	private Integer calificacion;
	private List<String> comentarios = new ArrayList<>();
	public DtoCalificacion() {
		
	}
	public DtoCalificacion(Integer calificacion, List<String> comentarios) {
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
	public List<String> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<String> comentarios) {
		this.comentarios = comentarios;
	}
	
	

}
