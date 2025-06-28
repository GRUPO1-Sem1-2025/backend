package com.example.Login.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import com.example.Login.dto.DtoDepartamentoLocalidad;
import com.example.Login.dto.DtoDestinoMasVistos;
import com.example.Login.dto.DtoLocalidadOrigenDestino;
import com.example.Login.model.Localidad;
import com.example.Login.model.Usuario;

@Repository public interface LocalidadRepository extends JpaRepository<Localidad,
Integer>{ 
	  
	 Optional<Localidad> findByNombre(String nombre);
	 
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
	 
	 @Query(value = """
			    SELECT l.departamento AS nombreDepartamento, COUNT(l.nombre) AS cantidadLocalidades
			    FROM localidades l
			    GROUP BY l.departamento
			    ORDER BY cantidadLocalidades DESC
			    """, nativeQuery = true)
			List<DtoDepartamentoLocalidad> obtenerDepartamentosConCantidad();
	 
	 @Query(value = """
		        SELECT 
		          l.nombre AS nombreLocalidad,
		          COUNT(vo.id) AS cantidad_origen,
		          COUNT(vd.id) AS cantidad_destino
		        FROM localidades l
		        LEFT JOIN viaje vo ON vo.localidad_origen_id = l.id
		        LEFT JOIN viaje vd ON vd.localidad_destino_id = l.id
		        GROUP BY l.id, l.nombre
		        HAVING COUNT(vo.id) != 0 AND COUNT(vd.id) != 0
		        ORDER BY l.nombre
		        LIMIT 10
		        """, nativeQuery = true)
		    List<DtoLocalidadOrigenDestino> obtenerLocalidadesConViajes();	 
}