package com.example.Login.dto;

public class DtoViajesMasCaros {
	 private Long id;
	    private Double precio;

	    // Constructor que acepta los tipos de la entidad original
	    public DtoViajesMasCaros(Integer id, Float precio) {
	        this.id = id != null ? id.longValue() : null;
	        this.precio = precio != null ? precio.doubleValue() : null;
	    }

	    // Getters
	    public Long getId() {
	        return id;
	    }

	    public Double getPrecio() {
	        return precio;
	    }
    

}
