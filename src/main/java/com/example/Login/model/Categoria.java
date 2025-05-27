package com.example.Login.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // o AUTO, según tu configuración
	private Long id;

	private String nombreCategoria;
	private int descuento;

	public Categoria() {

	}

	public Categoria(String nombreCategoria, int descuento) {
		super();
		this.nombreCategoria = nombreCategoria;
		this.descuento = descuento;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public int getDescuento() {
		return descuento;
	}

	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}
}
