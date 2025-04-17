package com.example.Login.dto;

import java.sql.Date;
import java.time.LocalTime;

public class DtoViaje {
	private float precio;
	private Date fechaInicio;
	private Date fechaFin;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private int idLocalidadOrigen;
	private int idLocalidadDestino;
	
	public DtoViaje() {
		
	}
	
	public DtoViaje(float precio, Date fechaInicio, Date fechaFin, LocalTime horaInicio, LocalTime horaFin,
			int idLocalidadOrigen, int idLocalidadDestino) {
		super();
		this.precio = precio;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.idLocalidadOrigen = idLocalidadOrigen;
		this.idLocalidadDestino = idLocalidadDestino;
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

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public int getIdLocalidadOrigen() {
		return idLocalidadOrigen;
	}

	public void setIdLocalidadOrigen(int idLocalidadOrigen) {
		this.idLocalidadOrigen = idLocalidadOrigen;
	}

	public int getIdLocalidadDestino() {
		return idLocalidadDestino;
	}

	public void setIdLocalidadDestino(int idLocalidadDestino) {
		this.idLocalidadDestino = idLocalidadDestino;
	}

	
	
	
}
