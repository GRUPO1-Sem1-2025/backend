package com.example.Login.service;

import java.util.Optional;
import com.example.Login.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.model.Localidad;
import com.example.Login.repository.LocalidadRepository;
import com.example.Login.model.Viaje;

@Service
public class ViajeService {

	@Autowired
	private LocalidadRepository localidadRepository;

	@Autowired
	private ViajeRepository viajeRepository;

	public boolean crearViaje(Viaje viaje) {
	    Viaje nuevoViaje = new Viaje();
	    Optional<Localidad> locOri = localidadRepository.findById(viaje.getLocalidadOrigen().getId());
	    Optional<Localidad> locDest = localidadRepository.findById(viaje.getLocalidadDestino().getId());

	    if (locOri.isPresent() && locDest.isPresent()) {
	    	if (locOri.get().getId() == locDest.get().getId()) {
	            System.out.println("La ciudad de origen y destino no pueden ser las mismas");
	            return false;
	        }
	    	if (!locOri.get().isActivo() || !locDest.get().isActivo()) {
	    		System.out.println("Una de las ciudades no se encuentra disponible");
	    		return false;
	    	}

	        Localidad localidadOrigen = locOri.get();
	        Localidad localidadDestino = locDest.get();

	        nuevoViaje.setFechaFin(viaje.getFechaFin());
	        nuevoViaje.setFechaInicio(viaje.getFechaInicio());
	        nuevoViaje.setHoraInicio(viaje.getHoraInicio());
	        nuevoViaje.setHoraFin(viaje.getHoraFin());
	        nuevoViaje.setLocalidadOrigen(localidadOrigen);
	        nuevoViaje.setLocalidadDestino(localidadDestino);

	        viajeRepository.save(nuevoViaje);
	        return true;
	    }
	    System.out.println("Una de las ciudads ingresadas no existe en el sistema");
	    return false;
	}


}
