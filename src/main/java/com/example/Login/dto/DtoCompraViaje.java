package com.example.Login.dto;

import java.sql.Date;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DtoCompraViaje {

	// datos del viaje
	private String localidadDestinoDepartamento;
	private String localidadDestinoLocalidad;
	private String localidadOrigenDepartamento;
	private String localidadOrigenLocalidad;
	private LocalTime horaInicio;
	private LocalTime horaFin;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Montevideo")
	private Date fechaInicio;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Montevideo")
	private Date fechaFin;

	// datos de la compra
	private int cantidadAsientos;
	private float precio;
	private int descuento;
	private int idOmnibus;

	public DtoCompraViaje() {

	}

	public DtoCompraViaje(Date fechaInicio, Date fechaFin, LocalTime horaInicio, LocalTime horaFin,
			String localidadDestinoNombre, String localidadDestinoLocalidad, String localidadOrigenNombre,
			String localidadOrigenLocalidad, int cantidadAsientos, float precio, int descuento, int idOmnibus) {
		super();
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.localidadDestinoDepartamento = localidadDestinoNombre;
		this.localidadDestinoLocalidad = localidadDestinoLocalidad;
		this.localidadOrigenDepartamento = localidadOrigenNombre;
		this.localidadOrigenLocalidad = localidadOrigenLocalidad;
		this.cantidadAsientos = cantidadAsientos;
		this.precio = precio;
		this.descuento = descuento;
		this.idOmnibus = idOmnibus;
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

	public String getLocalidadDestinoNombre() {
		return localidadDestinoDepartamento;
	}

	public void setLocalidadDestinoNombre(String localidadDestinoNombre) {
		this.localidadDestinoDepartamento = localidadDestinoNombre;
	}

	public String getLocalidadDestinoLocalidad() {
		return localidadDestinoLocalidad;
	}

	public void setLocalidadDestinoLocalidad(String localidadDestinoLocalidad) {
		this.localidadDestinoLocalidad = localidadDestinoLocalidad;
	}

	public String getLocalidadOrigenNombre() {
		return localidadOrigenDepartamento;
	}

	public void setLocalidadOrigenNombre(String localidadOrigenNombre) {
		this.localidadOrigenDepartamento = localidadOrigenNombre;
	}

	public String getLocalidadOrigenLocalidad() {
		return localidadOrigenLocalidad;
	}

	public void setLocalidadOrigenLocalidad(String localidadOrigenLocalidad) {
		this.localidadOrigenLocalidad = localidadOrigenLocalidad;
	}

	public int getCantidadAsientos() {
		return cantidadAsientos;
	}

	public void setCantidadAsientos(int cantidadAsientos) {
		this.cantidadAsientos = cantidadAsientos;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public int getDescuento() {
		return descuento;
	}

	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}

	public int getIdOmnibus() {
		return idOmnibus;
	}

	public void setIdOmnibus(int idOmnibus) {
		this.idOmnibus = idOmnibus;
	}

}
