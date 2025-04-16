package com.example.Login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Login.model.OmnibusAsiento;
import com.example.Login.model.OmnibusAsientoViaje;

@Repository 
public interface OmnibusAsientoViajeRepository extends JpaRepository<OmnibusAsientoViaje,
Integer>{


}
