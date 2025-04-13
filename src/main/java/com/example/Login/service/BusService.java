package com.example.Login.service;

import com.example.Login.model.Asiento;
import com.example.Login.model.Omnibus;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.example.Login.model.Usuario;
import com.example.Login.repository.BusRepository;
//import com.example.Login.repository.UsuarioRepository;

@Service
public class BusService {

	@Autowired
	private final BusRepository busRepository;

	// Inyección de dependencias
	public BusService(BusRepository busRepository) {
		this.busRepository = busRepository;
	}
	
	public long busesTotales() {
		return busRepository.count();
	}
	
	// Guardar bus
	public Omnibus crearOmnibus(Omnibus bus) {
		return busRepository.save(bus);
	}
	
	public Omnibus agregarAsientoABus(Omnibus bus,Asiento asiento) {
		List<Asiento> listaAsientos = bus.getListaAsientos();
		listaAsientos.add(asiento);		
		return bus;
	}
	

}