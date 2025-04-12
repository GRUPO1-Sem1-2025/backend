package com.example.Login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Login.model.Asiento;


//@Repository
//public interface AsientoRepository extends JpaRepository<Asiento,
//Integer>{ 
//	Optional<Asiento> findByNro(int nro);
//}

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Integer> {
    Optional<Asiento> findByNro(int nro);
}