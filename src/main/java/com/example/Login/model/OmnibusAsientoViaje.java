package com.example.Login.model;

import com.example.Login.dto.EstadoViaje;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bus_asiento_viaje")
public class OmnibusAsientoViaje {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "omnibus_asiento_id")
    private OmnibusAsiento omnibusAsiento;

    @ManyToOne
    @JoinColumn(name = "viaje_id")
    private Viaje viaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_viaje")
    private EstadoViaje estadoViaje;
    
    // Constructor
    public OmnibusAsientoViaje() {
    	
    }

    public OmnibusAsientoViaje(int id, OmnibusAsiento omnibusAsiento, Viaje viaje, EstadoViaje estadoViaje) {
		super();
		this.id = id;
		this.omnibusAsiento = omnibusAsiento;
		this.viaje = viaje;
		this.estadoViaje = estadoViaje;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OmnibusAsiento getOmnibusAsiento() {
		return omnibusAsiento;
	}

	public void setOmnibusAsiento(OmnibusAsiento omnibusAsiento) {
		this.omnibusAsiento = omnibusAsiento;
	}

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}

	public EstadoViaje getEstadoViaje() {
		return estadoViaje;
	}

	public void setEstadoViaje(EstadoViaje estadoViaje) {
		this.estadoViaje = estadoViaje;
	}    
    
}

