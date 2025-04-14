package com.example.Login.service;

import com.example.Login.model.Asiento;
import com.example.Login.model.Omnibus;
import com.example.Login.model.OmnibusAsiento;
import com.example.Login.repository.OmnibusAsientoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.example.Login.model.Usuario;
import com.example.Login.repository.OmnibusRepository;
//import com.example.Login.repository.UsuarioRepository;
import com.example.Login.repository.UsuarioRepository;

@Service
public class OmnibusService {
	
	@Autowired
	private OmnibusRepository omnibusRepository;

	@Autowired
	private OmnibusAsientoRepository omnibusasientoRepository;
	
	// Inyección de dependencias
	public OmnibusService(OmnibusRepository omnibusRepository) {
		this.omnibusasientoRepository = null;
		this.omnibusRepository = omnibusRepository;
	}
	
	public long busesTotales() {
		return omnibusRepository.count();
	}
	
	// Guardar bus
	public Omnibus crearOmnibus(Omnibus bus) {
		return omnibusRepository.save(bus);
	}
	
	//public Omnibus agregarAsientoABus(Omnibus bus,Asiento asiento) {
	public void asignarAsientoAOmnibus(Omnibus omnibus, Asiento asiento, boolean estado) {
		    OmnibusAsiento relacion = new OmnibusAsiento();
		    relacion.setOmnibus(omnibus);
		    relacion.setAsiento(asiento);
		    relacion.setEstado(estado);
		    omnibus.getOmnibusAsientos().add(relacion);
		    asiento.getOmnibusAsientos().add(relacion);
		    //OmnibusRepository.save(omnibus);
		    omnibusasientoRepository.save(relacion);
	}
	

}