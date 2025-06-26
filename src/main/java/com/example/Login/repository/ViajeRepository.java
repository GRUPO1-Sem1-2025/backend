package com.example.Login.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.DtoViajeDestinoFecha;
import com.example.Login.model.Asiento;
import com.example.Login.model.Viaje;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {
	@Query("SELECT COUNT(v) FROM Viaje v WHERE v.omnibus.id = :omnibusId")
    int contarViajesAsignadoABus(@Param("omnibusId") int omnibusId);
	
	@Query("SELECT COUNT(v) FROM Viaje v WHERE v.omnibus.id = :omnibusId and v.estadoViaje <=1")
    int contarViajesActivosAsignadoABus(@Param("omnibusId") int omnibusId);
	
//	@Query("SELECT new com.example.Login.dto.DtoViaje(v.id, v.fechaInicio, v.fechaFin, v.localidadDestino.nombre) " +
//		       "FROM Viaje v " +
//		       "WHERE v.fechaInicio >= :fechaInicio " +
//		       "AND v.fechaFin < :fechaFin " +
//		       "AND v.localidadOrigen.id = :origenId " +
//		       "AND v.localidadDestino.id = :destinoId " +
//		       "AND v.estadoViaje < 2 " +
//		       "AND v.omnibus IS NOT NULL")
//		List<DtoViaje> buscarViajesFiltrados(
//		    @Param("fechaInicio") Date date,
//		    @Param("fechaFin") Date date2,
//		    @Param("origenId") int i,
//		    @Param("destinoId") int j
//		);
	
//	@Query("SELECT new com.example.Login.dto.DtoViaje(v.fechaInicio, v.fechaFin, v.localidadOrigen.id, v.localidadDestino.id) " +
//		       "FROM Viaje v " +
//		       "WHERE v.fechaInicio >= :fechaInicio " +
//		       "AND v.fechaFin < :fechaFin " +
//		       "AND v.localidadOrigen.id = :origenId " +
//		       "AND v.localidadDestino.id = :destinoId " +
//		       "AND v.estadoViaje < 2 " +
//		       "AND v.omnibus IS NOT NULL")
//		List<DtoViaje> buscarViajesFiltrados(
//		    @Param("fechaInicio") Date fechaInicio,
//		    @Param("fechaFin") Date fechaFin,
//		    @Param("origenId") int origenId,
//		    @Param("destinoId") int destinoId
//		);
	
		@Query("SELECT new com.example.Login.dto.DtoViaje(v.precio, v.fechaInicio, v.fechaFin, v.horaInicio, v.horaFin, v.localidadOrigen.id, v.localidadDestino.id, v.omnibus.id, v.id) " +
			       "FROM Viaje v " +
			       "WHERE v.fechaInicio >= :fechaInicio " +
			       "AND v.fechaFin <= :fechaFin " +
			       "AND v.localidadOrigen.id = :origenId " +
			       "AND v.localidadDestino.id = :destinoId " +
			       "AND v.estadoViaje < 2 " +
			       "AND v.omnibus IS NOT NULL")
		List<DtoViaje> buscarViajesFiltrados(
		    @Param("fechaInicio") Date fechaInicio,
		    @Param("fechaFin") Date fechaFin,
		    @Param("origenId") int origenId,
		    @Param("destinoId") int destinoId
		);
		
		@Query(value = """
			    SELECT * FROM viaje 
			    WHERE (fecha_inicio + hora_inicio) BETWEEN NOW() AND NOW() + INTERVAL '60 minutes'
			""", nativeQuery = true)
			List<Viaje> findViajesConInicioEnLosProximos60Minutos();
		
		@Query(value = """
			    SELECT * FROM viaje 
			    WHERE (fecha_inicio + hora_inicio) BETWEEN NOW() AND NOW() + CAST(?1 AS INTERVAL)
			    """, nativeQuery = true)
			List<Viaje> findViajesConInicioEnLosProximosMinutos(String intervalo);
		
		List<Viaje> findByOmnibus_Id(int idOmnibus);
		
		@Query(value = "SELECT id FROM viaje ORDER BY calificacion DESC LIMIT 5", nativeQuery = true)
	    List<Integer> findTop5IdsByCalificacion();
		
		@Query("SELECT MAX(v.id) FROM Viaje v")
		Integer findUltimoId();  // En un repositorio que extiende JpaRepository
		
		@Query("SELECT v FROM Viaje v WHERE v.omnibus.id = :omnibusId")
	    List<Viaje> findByOmnibusId(@Param("omnibusId") int omnibusId);
}
