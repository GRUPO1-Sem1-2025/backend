package com.example.Login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Login.model.OmnibusAsiento;

@Repository public interface OmnibusAsientoRepository extends JpaRepository<OmnibusAsiento,
Integer>{
	
}
