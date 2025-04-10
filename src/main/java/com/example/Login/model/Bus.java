package com.example.Login.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "buses")
public class Bus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String marca;
	private boolean activo;
	@ManyToMany
	   @JoinTable(
		        name = "bus_asiento",
		        joinColumns = {@JoinColumn(name = "bus_id")},
		        inverseJoinColumns = {@JoinColumn(name = "asiento_id")}
	)
	private List<Asiento> listaAsientos = new ArrayList<>();
	private int cant_asientos;
	
	// constructor
	public Bus() {
		// TODO Auto-generated constructor stub
	}
	
	public Bus(int id, String marca, boolean activo, List<Asiento> listaAsientos, int cant_asientos) {
		super();
		this.id = id;
		this.marca = marca;
		this.activo = activo;
		this.listaAsientos = listaAsientos;
		this.cant_asientos = cant_asientos;
	}
	
	// set and get
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public List<Asiento> getListaAsientos() {
		return listaAsientos;
	}

	public void setListaAsientos(List<Asiento> listaAsientos) {
		this.listaAsientos = listaAsientos;
	}

	public int getCant_asientos() {
		return cant_asientos;
	}

	public void setCant_asientos(int cant_asientos) {
		this.cant_asientos = cant_asientos;
	}

	

	

}
