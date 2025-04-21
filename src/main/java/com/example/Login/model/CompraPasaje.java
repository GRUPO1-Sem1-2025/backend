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
    @JoinColumn(name = "cliente_id", nullable = true)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = true)
    private Usuario vendedor;    
    
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
    
    private int cat_pasajes;
    
    private float total;
    
    private String tipo_venta;
    
    private String estadoCompra;
    
    public CompraPasaje() {
    	
    }

	public CompraPasaje(Long id,String estado, Usuario usuario, Usuario vendedor, Viaje viaje, List<AsientoPorViaje> asientos,
			LocalDateTime fechaHoraCompra, int cat_pasajes, float total, String tipo_venta) {
		this.id = id;
		this.usuario = usuario;
		this.vendedor = vendedor;
		this.viaje = viaje;
		this.asientos = asientos;
		this.fechaHoraCompra = fechaHoraCompra;
		this.cat_pasajes = cat_pasajes;
		this.total = total;
		this.estadoCompra = estado;
		this.tipo_venta = tipo_venta;
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

	public Usuario getVendedor() {
		return vendedor;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
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

	public int getCat_pasajes() {
		return cat_pasajes;
	}

	public void setCat_pasajes(int cat_pasajes) {
		this.cat_pasajes = cat_pasajes;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getTipo_venta() {
		return tipo_venta;
	}

	public void setTipo_venta(String tipo_venta) {
		this.tipo_venta = tipo_venta;
	}

	public String getEstadoCompra() {
		return estadoCompra;
	}

	public void setEstadoCompra(String estadoCompra) {
		this.estadoCompra = estadoCompra;
	}

	
}