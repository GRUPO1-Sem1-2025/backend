package com.example.Login.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.List;

@Entity
//@Table(name = "buses")
@Table(name = "omnibuses")

public class Omnibus {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String marca;
    private boolean activo;

    @OneToMany(mappedBy = "omnibus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OmnibusAsiento> omnibusAsientos = new ArrayList<>();

    private int cant_asientos;
    
    private String localidadActual;
	
	// constructor
	public Omnibus() {
		// TODO Auto-generated constructor stub
	}
	
	public Omnibus(int id, String marca, boolean activo, List<OmnibusAsiento> omnibusAsientos, int cant_asientos, String localidad) {
		super();
		this.id = id;
		this.marca = marca;
		this.activo = activo;
		this.omnibusAsientos = omnibusAsientos;
		this.cant_asientos = cant_asientos;
		this.localidadActual = localidad;
	}

	// set and get
	
	public List<OmnibusAsiento> getAsientos() {
	    return omnibusAsientos;
	}
	
	public String getLocalidad() {
		return localidadActual;
	}

	public void setLocalidad(String localidad) {
		this.localidadActual = localidad;
	}
	
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

	public int getCant_asientos() {
		return cant_asientos;
	}

	public void setCant_asientos(int cant_asientos) {
		this.cant_asientos = cant_asientos;
	}

	public List<OmnibusAsiento> getOmnibusAsientos() {
		return omnibusAsientos;
	}

	public void setOmnibusAsientos(List<OmnibusAsiento> omnibusAsientos) {
		this.omnibusAsientos = omnibusAsientos;
	}

	

	

}
