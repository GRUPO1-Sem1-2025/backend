package com.example.Login.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "asientos")
public class Asiento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private boolean estado;
	private int nro;
	@ManyToMany(mappedBy = "listaAsientos")
    private List<Bus> buses;// = null;

   	// constructor
	public Asiento() {	
	}
	
	public Asiento(int id, boolean estado, int nro, List<Bus> buses) {
		super();
		this.id = id;
		this.estado = estado;
		this.nro = nro;
		this.buses = buses;
	}
	
	// set and get

	public int getId() {
		return id;
	}

	public List<Bus> getBuses() {
		return buses;
	}

	public void setBuses(List<Bus> buses) {
		this.buses = buses;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNro() {
		return nro;
	}

	public void setNro(int nro) {
		this.nro = nro;
	}

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
