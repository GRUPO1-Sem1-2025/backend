package com.example.Login.repository;

import com.example.Login.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;


  @Repository public interface UsuarioRepository extends JpaRepository<Usuario,
  Integer>{ 
	  
	  Optional<Usuario> findByEmail(String email);
	  
	  @Query("SELECT MAX(u.cod_empleado) FROM Usuario u")
	    Integer findMaxCodEmpleado();
	  
  }
  
  
 