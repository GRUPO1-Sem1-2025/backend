package com.example.Login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Login.model.Omnibus;


@Repository public interface OmnibusRepository extends JpaRepository<Omnibus,
Integer>{ 
	Optional<Omnibus> findByMatricula(String matricula);
}
