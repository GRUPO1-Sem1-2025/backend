package com.example.Login.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
//	// Inyección de dependencias
//    public UsuarioService(UsuarioRepository usuarioRepository){
//    	this.usuarioRepository = usuarioRepository;
//    }
}