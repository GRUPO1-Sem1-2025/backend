package com.example.Login.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "compras")
public class CompraPasaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "viaje_id")
    private Viaje viaje;
    
    @ManyToMany
    @JoinTable(
        name = "compra_asiento_por_viaje",
        joinColumns = @JoinColumn(name = "compra_id"),
        inverseJoinColumns = @JoinColumn(name = "asiento_por_viaje_id")
    )
    private List<AsientoPorViaje> asientos = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "asiento_por_viaje_id")
//    private AsientoPorViaje asientoPorViaje;

    @Column(name = "fecha_hora_compra")
    private LocalDateTime fechaHoraCompra;
    
    //private float precio;
    
    public CompraPasaje() {
    	
    }

	public CompraPasaje(Long id, Usuario usuario, Viaje viaje, List<AsientoPorViaje> asientos,
			LocalDateTime fechaHoraCompra) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.viaje = viaje;
		this.asientos = asientos;
		this.fechaHoraCompra = fechaHoraCompra;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}

	public List<AsientoPorViaje> getAsientos() {
		return asientos;
	}

	public void setAsientos(List<AsientoPorViaje> asientos) {
		this.asientos = asientos;
	}

	public LocalDateTime getFechaHoraCompra() {
		return fechaHoraCompra;
	}

	public void setFechaHoraCompra(LocalDateTime fechaHoraCompra) {
		this.fechaHoraCompra = fechaHoraCompra;
	}
    
    
    
}