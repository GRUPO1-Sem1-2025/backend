package com.example.Login.model;

import com.example.Login.dto.EstadoViaje;
import com.example.Login.model.Localidad;
import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

//@Entity
//@Table(name = "viajes")
//public class Viaje {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private int id;
//	private float precio;
//	private Date fechaInicio;
//	private Date fechaFin;
//	private LocalTime horaInicio;
//	private LocalTime horaFin;
//	
//	@ManyToOne
//	@JoinColumn(name = "localidad_origen_id") // nombre real de la FK en la tabla viajes
//	private Localidad localidadOrigen;
//
//	@ManyToOne
//	@JoinColumn(name = "localidad_destino_id") // nombre real de la FK en la tabla viajes
//	private Localidad localidadDestino;

@Entity
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Omnibus omnibus;

    // Otros campos: origen, destino, fecha, etc.

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL)
    private List<AsientoPorViaje> asientosPorViaje;
    
	private float precio;
	private Date fechaInicio;
	private Date fechaFin;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private EstadoViaje estadoViaje;
	
	@ManyToOne
	@JoinColumn(name = "localidad_origen_id") // nombre real de la FK en la tabla viajes
	private Localidad localidadOrigen;

	@ManyToOne
	@JoinColumn(name = "localidad_destino_id") // nombre real de la FK en la tabla viajes
	private Localidad localidadDestino;
	
	//agregado para calificar viaje
	@Column(nullable = true)
	private Integer calificacion = 0;
	
	@OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comentario> comentarios = new ArrayList<>();

	
	//Constructores
	
	public Viaje() {
		
	}

//	public Viaje(int id, Omnibus omnibus, List<AsientoPorViaje> asientosPorViaje, float precio, Date fechaInicio,
//			Date fechaFin, LocalTime horaInicio, LocalTime horaFin, EstadoViaje estadoViaje, Localidad localidadOrigen,
//			Localidad localidadDestino) {
//		this.id = id;
//		this.omnibus = omnibus;
//		this.asientosPorViaje = asientosPorViaje;
//		this.precio = precio;
//		this.fechaInicio = fechaInicio;
//		this.fechaFin = fechaFin;
//		this.horaInicio = horaInicio;
//		this.horaFin = horaFin;
//		this.estadoViaje = estadoViaje;
//		this.localidadOrigen = localidadOrigen;
//		this.localidadDestino = localidadDestino;
//	}
	
	

//	public Viaje(int id, Omnibus omnibus, List<AsientoPorViaje> asientosPorViaje, float precio, Date fechaInicio,
//			Date fechaFin, LocalTime horaInicio, LocalTime horaFin, EstadoViaje estadoViaje, Localidad localidadOrigen,
//			Localidad localidadDestino, Integer calificacion, List<DtoComentarios> comentarios) {
//		super();
//		this.id = id;
//		this.omnibus = omnibus;
//		this.asientosPorViaje = asientosPorViaje;
//		this.precio = precio;
//		this.fechaInicio = fechaInicio;
//		this.fechaFin = fechaFin;
//		this.horaInicio = horaInicio;
//		this.horaFin = horaFin;
//		this.estadoViaje = estadoViaje;
//		this.localidadOrigen = localidadOrigen;
//		this.localidadDestino = localidadDestino;
//		this.calificacion = calificacion;
//		this.comentarios = comentarios;
//	}

	public Viaje(int id, Omnibus omnibus, List<AsientoPorViaje> asientosPorViaje, float precio, Date fechaInicio,
			Date fechaFin, LocalTime horaInicio, LocalTime horaFin, EstadoViaje estadoViaje, Localidad localidadOrigen,
			Localidad localidadDestino, Integer calificacion, List<Comentario> comentarios) {
		super();
		this.id = id;
		this.omnibus = omnibus;
		this.asientosPorViaje = asientosPorViaje;
		this.precio = precio;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.estadoViaje = estadoViaje;
		this.localidadOrigen = localidadOrigen;
		this.localidadDestino = localidadDestino;
		this.calificacion = calificacion;
		this.comentarios = comentarios;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Omnibus getOmnibus() {
		return omnibus;
	}

	public void setOmnibus(Omnibus omnibus) {
		this.omnibus = omnibus;
	}

	public List<AsientoPorViaje> getAsientosPorViaje() {
		return asientosPorViaje;
	}

	public void setAsientosPorViaje(List<AsientoPorViaje> asientosPorViaje) {
		this.asientosPorViaje = asientosPorViaje;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public Localidad getLocalidadOrigen() {
		return localidadOrigen;
	}

	public void setLocalidadOrigen(Localidad localidadOrigen) {
		this.localidadOrigen = localidadOrigen;
	}

	public Localidad getLocalidadDestino() {
		return localidadDestino;
	}

	public void setLocalidadDestino(Localidad localidadDestino) {
		this.localidadDestino = localidadDestino;
	}

	public EstadoViaje getEstadoViaje() {
		return estadoViaje;
	}

	public void setEstadoViaje(EstadoViaje estadoViaje) {
		this.estadoViaje = estadoViaje;
	}

	public Integer getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	
	
	
}