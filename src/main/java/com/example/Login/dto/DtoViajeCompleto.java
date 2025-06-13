package com.example.Login.dto;

import java.sql.Date;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DtoViajeCompleto {
	private float precio;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Montevideo")
	private Date fechaInicio;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Montevideo")	
	private Date fechaFin;
	
	private int id;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private String idLocalidadOrigen;
	private String idLocalidadDestino;
	private Integer idOmnibus;
	private int asientosOcupados;
	private int calificacion;
	private EstadoViaje estadoViaje;
	
	public DtoViajeCompleto() {
		
	}
	public DtoViajeCompleto(float precio, Date fechaInicio, Date fechaFin, int id, LocalTime horaInicio,
			LocalTime horaFin, String idLocalidadOrigen, String idLocalidadDestino, Integer idOmnibus, int asientosOcupados, int calificacion, EstadoViaje estadoViaje) {
		super();
		this.precio = precio;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.id = id;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.idLocalidadOrigen = idLocalidadOrigen;
		this.idLocalidadDestino = idLocalidadDestino;
		this.idOmnibus = idOmnibus;
		this.asientosOcupados = asientosOcupados;
		this.calificacion = calificacion;
		this.estadoViaje = estadoViaje;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getIdLocalidadOrigen() {
		return idLocalidadOrigen;
	}
	public void setIdLocalidadOrigen(String idLocalidadOrigen) {
		this.idLocalidadOrigen = idLocalidadOrigen;
	}
	public String getIdLocalidadDestino() {
		return idLocalidadDestino;
	}
	public void setIdLocalidadDestino(String idLocalidadDestino) {
		this.idLocalidadDestino = idLocalidadDestino;
	}
	public Integer getIdOmnibus() {
		return idOmnibus;
	}
	public void setIdOmnibus(Integer idOmnibus) {
		this.idOmnibus = idOmnibus;
	}
	public int getAsientosOcupados() {
		return asientosOcupados;
	}
	public void setAsientosOcupados(int asientosOcupados) {
		this.asientosOcupados = asientosOcupados;
	}
	public int getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}
	public EstadoViaje getEstadoViaje() {
		return estadoViaje;
	}
	public void setEstadoViaje(EstadoViaje estadoViaje) {
		this.estadoViaje = estadoViaje;
	}
	
	
	
}
