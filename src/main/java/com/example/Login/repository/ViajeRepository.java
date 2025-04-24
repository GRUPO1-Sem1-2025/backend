package com.example.Login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Login.model.Asiento;
import com.example.Login.model.Viaje;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {
	@Query("SELECT COUNT(v) FROM Viaje v WHERE v.omnibus.id = :omnibusId")
    int contarViajesAsignadoABus(@Param("omnibusId") int omnibusId);

}
