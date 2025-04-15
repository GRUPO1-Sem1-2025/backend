package com.example.Login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Login.model.Asiento;
import com.example.Login.model.Viaje;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {
   // Optional<Viaje> findByNombre(String nombre);

}
