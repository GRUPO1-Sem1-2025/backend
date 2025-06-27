package com.example.Login.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import com.example.Login.dto.DtoDestinoMasVistos;
import com.example.Login.model.Localidad;
import com.example.Login.model.Usuario;

@Repository public interface LocalidadRepository extends JpaRepository<Localidad,
Integer>{ 
	  
	 Optional<Localidad> findByNombre(String nombre);
	 
//	 @Query(value = """
//			    SELECT 
//			       (v.localidad_destino_id) AS id,
//			        l.nombre AS nombre,
//			        COUNT(*)::BIGINT AS cantidad
//			    FROM viaje v
//			    JOIN compras c ON c.viaje_id = v.id
//			    JOIN localidades l ON v.localidad_destino_id = l.id
//			    WHERE c.estado_compra = 0
//			    GROUP BY v.localidad_destino_id, l.nombre
//			    ORDER BY cantidad DESC
//			    LIMIT 10
//			""", nativeQuery = true)
	 
	 
//	 @Query(value = "SELECT v.localidad_destino_id AS id, l.nombre AS nombre, COUNT(*) AS cantidad " +
//             "FROM viaje v " +
//             "JOIN compras c ON c.viaje_id = v.id " +
//             "JOIN localidades l ON v.localidad_destino_id = l.id " +
//             "WHERE c.estado_compra = 0 " +
//             "GROUP BY v.localidad_destino_id, l.nombre " +
//             "ORDER BY cantidad DESC " +
//             "LIMIT 10", nativeQuery = true)
	 
	 @Query(value = """
			    SELECT 
			       v.localidad_destino_id AS id,
			       l.nombre AS nombre,
			       COUNT(*) AS cantidad
			    FROM viaje v
			    JOIN compras c ON c.viaje_id = v.id
			    JOIN localidades l ON v.localidad_destino_id = l.id
			    WHERE c.estado_compra = 0
			    GROUP BY v.localidad_destino_id, l.nombre
			    ORDER BY cantidad DESC
			    LIMIT 10
			""", nativeQuery = true)
		    List<DtoDestinoMasVistos> findTop10DestinosConNombre();	
}