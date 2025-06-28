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

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Integer> {

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
			LIMIT 10
			""", nativeQuery = true)
	List<DtoDepartamentoLocalidad> obtenerDepartamentosConCantidad();

	@Query(value = """
					    SELECT
			  l.nombre AS nombreLocalidad,
			  (
			    SELECT COUNT(*)
			    FROM viaje v1
			    WHERE v1.localidad_origen_id = l.id
			  ) AS cantidad_origen,
			  (
			    SELECT COUNT(*)
			    FROM viaje v2
			    WHERE v2.localidad_destino_id = l.id
			  ) AS cantidad_destino
			FROM localidades l
			WHERE
			  (SELECT COUNT(*) FROM viaje v1 WHERE v1.localidad_origen_id = l.id) > 0
			  AND
			  (SELECT COUNT(*) FROM viaje v2 WHERE v2.localidad_destino_id = l.id) > 0
			ORDER BY l.nombre
			LIMIT 10;
					        """, nativeQuery = true)
	List<DtoLocalidadOrigenDestino> obtenerLocalidadesConViajes();
}