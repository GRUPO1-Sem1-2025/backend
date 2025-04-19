package com.example.Login.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @JoinColumn(name = "asiento_por_viaje_id")
    private AsientoPorViaje asientoPorViaje;

    @Column(name = "fecha_hora_compra")
    private LocalDateTime fechaHoraCompra;
    
    //private float precio;
    
    public CompraPasaje() {
    	
    }

	public CompraPasaje(Long id, Usuario usuario, AsientoPorViaje asientoPorViaje, LocalDateTime fechaHoraCompra,
			float precio) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.asientoPorViaje = asientoPorViaje;
		this.fechaHoraCompra = fechaHoraCompra;
		//this.precio = precio;
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

	public AsientoPorViaje getAsientoPorViaje() {
		return asientoPorViaje;
	}

	public void setAsientoPorViaje(AsientoPorViaje asientoPorViaje) {
		this.asientoPorViaje = asientoPorViaje;
	}

	public LocalDateTime getFechaHoraCompra() {
		return fechaHoraCompra;
	}

	public void setFechaHoraCompra(LocalDateTime fechaHoraCompra) {
		this.fechaHoraCompra = fechaHoraCompra;
	}

//	public float getPrecio() {
//		return precio;
//	}
//
//	public void setPrecio(float precio) {
//		this.precio = precio;
//	}
	
	

    // Otros atributos opcionales
    // private BigDecimal precio;
    // private String metodoPago;

    // Getters y setters
    
    
}