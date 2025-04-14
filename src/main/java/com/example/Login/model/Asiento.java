package com.example.Login.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "asientos")
public class Asiento {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    //private boolean estado;

	    @Column(unique = true)
	    private int nro;

	    @OneToMany(mappedBy = "asiento", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<OmnibusAsiento> omnibusAsientos = new ArrayList<>();


   	// constructor
	public Asiento() {	
	}
	
	public Asiento(int id, int nro, List<OmnibusAsiento> omnibusAsientos) {
		super();
		this.id = id;
		this.nro = nro;
		this.omnibusAsientos = omnibusAsientos;
	}

	// set and get

	public int getId() {
		return id;
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

	public List<OmnibusAsiento> getOmnibusAsientos() {
		return omnibusAsientos;
	}

	public void setOmnibusAsientos(List<OmnibusAsiento> omnibusAsientos) {
		this.omnibusAsientos = omnibusAsientos;
	}

	
}
