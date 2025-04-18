package com.example.Login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Login.model.Localidad;
import com.example.Login.model.Usuario;

@Repository public interface LocalidadRepository extends JpaRepository<Localidad,
Integer>{ 
	  
	 Optional<Localidad> findByNombre(String nombre);
	  
}
