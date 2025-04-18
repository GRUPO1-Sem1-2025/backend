package com.example.Login.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Login.model.OmnibusAsiento;

@Repository public interface OmnibusAsientoRepository extends JpaRepository<OmnibusAsiento,
Integer>{
	//Optional<OmnibusAsiento> findFirstByOmnibusId(int busId);
	@Query("SELECT oa FROM OmnibusAsiento oa WHERE oa.omnibus.id = :busId")
    //Optional<OmnibusAsiento> buscarUnoPorBus(@Param("busId") int idBus);
	List<OmnibusAsiento> buscarPorBus(@Param("busId") int idBus);
	
}
