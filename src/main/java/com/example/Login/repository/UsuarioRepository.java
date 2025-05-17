package com.example.Login.repository;

import com.example.Login.dto.DtoNewUsuariosPorMes;
import com.example.Login.dto.DtoUsuariosPorRolQuery;
import com.example.Login.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


  @Repository public interface UsuarioRepository extends JpaRepository<Usuario,
  Integer>{ 
	  
	  Optional<Usuario> findByEmail(String email);
	  
	  @Query("SELECT MAX(u.cod_empleado) FROM Usuario u")
	    Integer findMaxCodEmpleado();
	  
		@Query(
		        value = "SELECT TO_CHAR(fecha_creacion, 'MM') AS mes, COUNT(*) AS total " +
		                "FROM usuarios " +
		                "GROUP BY mes " +
		                "ORDER BY mes",
		        nativeQuery = true
		    )
		    List<DtoNewUsuariosPorMes> contarUsuariosPorMes();
		
		@Query(
				value = "select rol ,count(*)"
						+ " from usuarios u "
						+ "group by u.rol",
						nativeQuery = true)
		List<DtoUsuariosPorRolQuery> usuariosPorRol();
	  
  }
  
  
 