package com.example.Login.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.Login.repository.ViajeRepository;

import io.jsonwebtoken.lang.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.EstadoViaje;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.Localidad;
import com.example.Login.model.Omnibus;
import com.example.Login.model.OmnibusAsiento;
import com.example.Login.model.OmnibusAsientoViaje;
import com.example.Login.repository.LocalidadRepository;
import com.example.Login.repository.OmnibusRepository;
import com.example.Login.model.Viaje;

@Service
public class ViajeService {

	@Autowired
	private LocalidadRepository localidadRepository;

	@Autowired
	private ViajeRepository viajeRepository;

	@Autowired
	private OmnibusRepository omnibusRepository;

//	public int crearViaje_viejo(DtoViaje dtoViaje) {
//		Viaje nuevoViaje = new Viaje();
//		System.out.println("idOrigen: " + dtoViaje.getIdLocalidadOrigen());
//		System.out.println("idOrigen: " + dtoViaje.getIdLocalidadOrigen());
//		Optional<Localidad> locOri = localidadRepository.findById(dtoViaje.getIdLocalidadOrigen());// getIdLocalidadOrigen().getId());
//		Optional<Localidad> locDest = localidadRepository.findById(dtoViaje.getIdLocalidadDestino());// .getId());
//
//		if (locOri.isPresent() && locDest.isPresent()) {
//			if (locOri.get().getId() == locDest.get().getId()) {
//				System.out.println("La ciudad de origen y destino no pueden ser las mismas");
//				return 1;
//			}
//			if (!locOri.get().isActivo() || !locDest.get().isActivo()) {
//				System.out.println("estado origen: " + locOri.get().isActivo());
//				System.out.println("estado destino: " + locDest.get().isActivo());
//				System.out.println("Una de las ciudades no se encuentra disponible");
//				return 2;
//			}
//
//			Localidad localidadOrigen = locOri.get();
//			Localidad localidadDestino = locDest.get();
//
//			nuevoViaje.setFechaFin(dtoViaje.getFechaFin());
//			nuevoViaje.setFechaInicio(dtoViaje.getFechaInicio());
//			nuevoViaje.setHoraInicio(dtoViaje.getHoraInicio());
//			nuevoViaje.setHoraFin(dtoViaje.getHoraFin());
//			nuevoViaje.setLocalidadOrigen(localidadOrigen);
//			nuevoViaje.setLocalidadDestino(localidadDestino);
//			nuevoViaje.setPrecio(dtoViaje.getPrecio());
//			nuevoViaje.setOmnibus(null);
//			nuevoViaje.setAsientosPorViaje(null);
//			nuevoViaje.setEstadoViaje(EstadoViaje.NUEVO);
//
//			viajeRepository.save(nuevoViaje);
//			return 3;
//		}
//		System.out.println("Una de las ciudads ingresadas no existe en el sistema");
//		return 4;
//	}
	
	public int crearViaje(DtoViaje dtoViaje) {
	    Viaje nuevoViaje = new Viaje();
	    
	    Optional<Localidad> locOri = localidadRepository.findById(dtoViaje.getIdLocalidadOrigen());
	    Optional<Localidad> locDest = localidadRepository.findById(dtoViaje.getIdLocalidadDestino());

	    if (locOri.isPresent() && locDest.isPresent()) {
	        if (locOri.get().getId() == locDest.get().getId()) {
	            System.out.println("La ciudad de origen y destino no pueden ser las mismas");
	            return 1;
	        }

	        if (!locOri.get().isActivo() || !locDest.get().isActivo()) {
	            System.out.println("Una de las ciudades no se encuentra disponible");
	            return 2;
	        }

	        nuevoViaje.setFechaFin(dtoViaje.getFechaFin());
	        nuevoViaje.setFechaInicio(dtoViaje.getFechaInicio());
	        nuevoViaje.setHoraInicio(dtoViaje.getHoraInicio());
	        nuevoViaje.setHoraFin(dtoViaje.getHoraFin());
	        nuevoViaje.setLocalidadOrigen(locOri.get());
	        nuevoViaje.setLocalidadDestino(locDest.get());
	        nuevoViaje.setPrecio(dtoViaje.getPrecio());
	        nuevoViaje.setEstadoViaje(EstadoViaje.NUEVO);

	        // No se asigna aún el omnibus ni los asientos
	        nuevoViaje.setOmnibus(null);
	        nuevoViaje.setAsientosPorViaje(new ArrayList<>());

	        viajeRepository.save(nuevoViaje);
	        return 3;
	    }

	    System.out.println("Una de las ciudades ingresadas no existe");
	    return 4;
	}
	
	

	public int asignarOmnibusAViaje_vieja(Omnibus omnibus, Viaje viaje) {
		int resultado = 0;
		Localidad localidadOrigen = viaje.getLocalidadOrigen();
		Optional<Localidad> loc = localidadRepository.findByNombre(localidadOrigen.getNombre());
		OmnibusAsientoViaje oav = new OmnibusAsientoViaje();

		try {
			Optional<Omnibus> busOpt = omnibusRepository.findById(omnibus.getId());

			if (busOpt.isPresent()) {
				Omnibus bus = busOpt.get();
				System.out.println(" Omnibus localidad: " + bus.getLocalidad());
				System.out.println(" Localidad localidad: " + loc.get().getNombre());

				if (bus.getLocalidad() != null && loc.isPresent() && bus.getLocalidad().equals(loc.get().getNombre())) {

					viaje.setOmnibus(bus);
					// Crear relación asiento/viaje
					for (OmnibusAsiento asiento : bus.getAsientos()) {
						AsientoPorViaje apv = new AsientoPorViaje();
						apv.setOmnibusAsiento(asiento);
						apv.setViaje(viaje);
						apv.setReservado(false); // al inicio, todos libres
						viaje.getAsientosPorViaje().add(apv);
					}

					viajeRepository.save(viaje);

					resultado = 1; // éxito
				} else {
					System.out.println(
							"No se puede asignar el viaje porque el bus no se encuentra en la localidad origen del viaje.");
					resultado = 4;
				}
			} else {
				System.out.println("No se encontró el ómnibus.");
				resultado = 2;
			}

		} // fin del try
		catch (Exception e) {
			e.printStackTrace();
			resultado = 3;
		}

		return resultado;
	}

	public int asignarOmnibusAViaje(Omnibus omnibus, Viaje viaje) {
		int resultado = 0;
		Localidad localidadOrigen = viaje.getLocalidadOrigen();
		Optional<Localidad> loc = localidadRepository.findByNombre(localidadOrigen.getNombre());

		try {
			Optional<Omnibus> busOpt = omnibusRepository.findById(omnibus.getId());

			if (busOpt.isPresent()) {
				Omnibus bus = busOpt.get();
				System.out.println(" Omnibus localidad: " + bus.getLocalidad());
				System.out.println(" Localidad localidad: " + loc.get().getNombre());

				if (bus.getLocalidad() != null && loc.isPresent() && bus.getLocalidad().equals(loc.get().getNombre())) {

					// Asignar el ómnibus al viaje
					viaje.setOmnibus(bus);

					// Crear relación asiento/viaje (modelo AsientoPorViaje, si estás usando ambos
					// modelos)
					for (OmnibusAsiento asiento : bus.getAsientos()) {
						AsientoPorViaje apv = new AsientoPorViaje();
						apv.setOmnibusAsiento(asiento);
						apv.setViaje(viaje);
						apv.setReservado(false); // al inicio, todos libres
						viaje.getAsientosPorViaje().add(apv);
					}

					viajeRepository.save(viaje);
					resultado = 1; // éxito

				} else {
					System.out.println(
							"No se puede asignar el viaje porque el bus no se encuentra en la localidad origen del viaje.");
					resultado = 4;
				}
			} else {
				System.out.println("No se encontró el ómnibus.");
				resultado = 2;
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultado = 3;
		}

		return resultado;
	}
}
