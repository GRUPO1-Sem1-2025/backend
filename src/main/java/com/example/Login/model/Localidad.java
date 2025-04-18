package com.example.Login.model;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "localidades")
public class Localidad {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String nombre;
private String departamento;
private boolean activo;

// Contructores

public Localidad() {
	
}

public Localidad(int id, String nombre, String departamento, boolean activo) {
	super();
	this.id = id;
	this.nombre = nombre;
	this.departamento = departamento;
	this.activo = activo;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getNombre() {
	return nombre;
}

public void setNombre(String nombre) {
	this.nombre = nombre;
}

public String getDepartamento() {
	return departamento;
}

public void setDepartamento(String departamento) {
	this.departamento = departamento;
}

public boolean isActivo() {
	return activo;
}

public void setActivo(boolean activo) {
	this.activo = activo;
}



}