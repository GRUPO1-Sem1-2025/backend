package com.example.Login.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.model.CerrarViaje;
import com.example.Login.repository.CerrarViajeRepository;

@Service
public class CerrarViajeService {
	
	@Autowired
	private CerrarViajeRepository cerrarViajeRepository;

	public int setearTiempoDeCierre(int idCerrarViaje, String tiempo) {
		CerrarViaje cerrarviaje = new CerrarViaje();
		try {
		Optional<CerrarViaje> OcerrarViaje = cerrarViajeRepository.findAllById(idCerrarViaje);
		cerrarviaje = OcerrarViaje.get();
		cerrarviaje.setTiempo(tiempo);
		cerrarViajeRepository.save(cerrarviaje);
		return 1;
		}catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	

}
