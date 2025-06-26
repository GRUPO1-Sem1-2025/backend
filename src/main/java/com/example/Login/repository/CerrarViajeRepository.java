package com.example.Login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Login.model.Categoria;
import com.example.Login.model.CerrarViaje;

@Repository
public interface CerrarViajeRepository extends JpaRepository<CerrarViaje, Integer> {

	Optional<CerrarViaje> findAllById(int id);


}
