package com.example.Login.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "asiento_por_viaje")
public class AsientoPorViaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viaje_id")
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "omnibus_asiento_id")
    private OmnibusAsiento omnibusAsiento;

    @Column(name = "reservado")
    private boolean reservado;
    
    @ManyToMany(mappedBy = "asientos")
    private List<CompraPasaje> compraPasajes;
    
//    @OneToMany(mappedBy = "asientoPorViaje")
//    private List<CompraPasaje> compraPasajes;
//    
    public AsientoPorViaje() {
    	
    }

public AsientoPorViaje(Long id, Viaje viaje, OmnibusAsiento omnibusAsiento, boolean reservado,
		List<CompraPasaje> compraPasajes) {
	super();
	this.id = id;
	this.viaje = viaje;
	this.omnibusAsiento = omnibusAsiento;
	this.reservado = reservado;
	this.compraPasajes = compraPasajes;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Viaje getViaje() {
	return viaje;
}

public void setViaje(Viaje viaje) {
	this.viaje = viaje;
}

public OmnibusAsiento getOmnibusAsiento() {
	return omnibusAsiento;
}

public void setOmnibusAsiento(OmnibusAsiento omnibusAsiento) {
	this.omnibusAsiento = omnibusAsiento;
}

public boolean isReservado() {
	return reservado;
}

public void setReservado(boolean reservado) {
	this.reservado = reservado;
}

public List<CompraPasaje> getCompraPasajes() {
	return compraPasajes;
}

public void setCompraPasajes(List<CompraPasaje> compraPasajes) {
	this.compraPasajes = compraPasajes;
}
    
    
    
}
