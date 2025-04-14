package com.example.Login.service;

import com.example.Login.model.Asiento;
import com.example.Login.model.Omnibus;
import com.example.Login.model.OmnibusAsiento;
import com.example.Login.repository.OmnibusAsientoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	
	public List<Integer> mostrarAsientosLibres(int bus_id) {
		Optional<Omnibus> omnibus = omnibusRepository.findById(bus_id);
		Omnibus bus = omnibus.get();
		System.out.println("Encontre el bus");
		
		List<Integer> asientosLibres = new ArrayList<>();	
		List<OmnibusAsiento> listaOmnibusAsiento = bus.getOmnibusAsientos();
	
		
		for (OmnibusAsiento oa : listaOmnibusAsiento) {
			boolean estado = oa.isEstado();
			System.out.println("Estado del asiento: " + estado);
			
			if (estado == true) {
				System.out.println("El asiento " + oa.getAsiento().getNro() + " esta libre");
				asientosLibres.add(oa.getAsiento().getNro());
			}
			else {
				System.out.println("El asiento " + oa.getAsiento().getNro() + " esta ocupado");
			}
		}
		return asientosLibres;
	}
}