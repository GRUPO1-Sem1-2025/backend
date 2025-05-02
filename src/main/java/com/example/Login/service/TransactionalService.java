package com.example.Login.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.repository.AsientoPorViajeRepository;
import com.example.Login.repository.CompraPasajeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TransactionalService {
	@Autowired
	private CompraPasajeRepository compraPasajeRepository;
	@Autowired
	AsientoPorViajeRepository asientoPorViajeRepository;
	
	@Transactional
	public void eliminarCompra(Long compraId) {
	    Optional<CompraPasaje> compraOpt = compraPasajeRepository.findById(compraId);

	    if (compraOpt.isPresent()) {
	        CompraPasaje compra = compraOpt.get();

	        // Paso 1: Marcar los asientos como no reservados
	        for (AsientoPorViaje asiento : compra.getAsientos()) {
	            asiento.setReservado(false); // liberar el asiento
	            // Si usás repositorio para AsientoPorViaje, guardalo explícitamente
	            asientoPorViajeRepository.save(asiento);
	        }

	        // Paso 2: Limpiar la relación ManyToMany
	        compra.getAsientos().clear();
	        compraPasajeRepository.save(compra); // Esto actualiza la tabla intermedia

	        // Paso 3: Eliminar la entidad principal
	        compraPasajeRepository.delete(compra);
	    } else {
	        throw new EntityNotFoundException("Compra no encontrada con ID: " + compraId);
	    }
	}

}
