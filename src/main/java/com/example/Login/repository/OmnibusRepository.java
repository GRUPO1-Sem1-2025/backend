package com.example.Login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.Login.model.Omnibus;


@Repository public interface OmnibusRepository extends JpaRepository<Omnibus,
Integer>{ 
	Optional<Omnibus> findByMatricula(String matricula);
	
	@Query("SELECT MAX(o.id) FROM Omnibus o")
	Integer findUltimoId();  // En un repositorio que extiende JpaRepository
	
	@Query("SELECT o FROM Omnibus o WHERE LOWER(o.matricula) = LOWER(:matricula)")
	Optional<Omnibus> findByMatriculaIgnoreCase(@Param("matricula") String matricula);
}
