package com.example.Login.dto;

import java.sql.Date;
import java.time.LocalTime;

public class DtoViajeDestinoFecha {
	private int BusId;
	private int cantAsientosDisponibles;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private float precioPasaje;
	

	public DtoViajeDestinoFecha() {

	}	
	
	
	public DtoViajeDestinoFecha(int busId, int cantAsientosDisponibles, LocalTime horaInicio, LocalTime horaFin,
			float precioPasaje) {
		super();
		BusId = busId;
		this.cantAsientosDisponibles = cantAsientosDisponibles;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.precioPasaje = precioPasaje;
	}


	public int getBusId() {
		return BusId;
	}


	public void setBusId(int busId) {
		BusId = busId;
	}


	public int getCantAsientosDisponibles() {
		return cantAsientosDisponibles;
	}


	public void setCantAsientosDisponibles(int cantAsientosDisponibles) {
		this.cantAsientosDisponibles = cantAsientosDisponibles;
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


	public float getPrecioPasaje() {
		return precioPasaje;
	}


	public void setPrecioPasaje(float precioPasaje) {
		this.precioPasaje = precioPasaje;
	}


	
}
