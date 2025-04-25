package com.example.Login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Login.model.Asiento;
import com.example.Login.model.AsientoPorViaje;

@Repository
public interface AsientoPorViajeRepository extends JpaRepository<AsientoPorViaje, Integer> {
    //Optional<Asiento> findByNro(int nro);
	@Query("SELECT apv " +
		       "FROM AsientoPorViaje apv " +
		       "JOIN apv.omnibusAsiento oa " +
		       "JOIN oa.asiento a " +
		       "WHERE apv.viaje.id = :viajeId AND a.nro = :nroAsiento")
		Optional<AsientoPorViaje> findByViajeIdAndNroAsiento(@Param("viajeId") int viajeId,
		                                                     @Param("nroAsiento") int nroAsiento);
	
	@Query(value = """
	        SELECT COUNT(a.nro)
	        FROM asiento_por_viaje apv
	        JOIN bus_asiento ba ON apv.omnibus_asiento_id = ba.id
	        JOIN asientos a ON ba.asiento_id = a.id
	        WHERE apv.viaje_id = :viajeId
	          AND apv.reservado = false
	        """, nativeQuery = true)
	    int contarAsientosDisponiblesPorViaje(@Param("viajeId") int viajeId);
}
