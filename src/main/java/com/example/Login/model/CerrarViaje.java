package com.example.Login.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tiempo_cerrar_viaje")
public class CerrarViaje {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // o AUTO, según tu configuración
	private Long id;
	private String tiempo = "0";
	public CerrarViaje() {
		
	}
	public CerrarViaje(Long id, String tiempo) {
		super();
		this.id = id;
		this.tiempo = tiempo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

}
