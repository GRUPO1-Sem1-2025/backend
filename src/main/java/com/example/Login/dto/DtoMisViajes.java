package com.example.Login.dto;

import java.sql.Date;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DtoMisViajes {
	private float precio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Montevideo")
	private Date fechaInicio;
	private int idLocalidadDestino;
	private int idOmnibus;
	
	public DtoMisViajes() {
		
	}
	public DtoMisViajes(float precio, Date fechaInicio, int idLocalidadDestino, int idOmnibus) {
		super();
		this.precio = precio;
		this.fechaInicio = fechaInicio;
		this.idLocalidadDestino = idLocalidadDestino;
		this.idOmnibus = idOmnibus;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	
	}
	public int getIdLocalidadDestino() {
		return idLocalidadDestino;
	}
	public void setIdLocalidadDestino(int idLocalidadDestino) {
		this.idLocalidadDestino = idLocalidadDestino;
	}
	public int getIdOmnibus() {
		return idOmnibus;
	}
	public void setIdOmnibus(int idOmnibus) {
		this.idOmnibus = idOmnibus;
	}	
	
	

}
