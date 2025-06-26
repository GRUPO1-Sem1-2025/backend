package com.example.Login.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Login.model.CerrarViaje;
import com.example.Login.service.CerrarViajeService;

@RestController
@RequestMapping("/cerrarviaje")
public class CerrarViajeController {
	
	@Autowired
	private CerrarViajeService cerrarViaje;
	
	@PostMapping("/setearTiempoDeCierre")
	public ResponseEntity<Map<String, Object>> setearTiempoDeCierre(@RequestParam String tiempo) {
		Map<String, Object> response = new HashMap<>();
		int idCerrarViaje=1;

		int respuesta = cerrarViaje.setearTiempoDeCierre(idCerrarViaje,tiempo);//ategoriaService.cambiarDescuento(nombreCategoria, descuento);

		if (respuesta == 1) {
			response.put("mensaje", "El tiempo de cierre de viajes se seteó de forma correcta");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("error", "No se pudo setear el tiempo del cierre de viajes");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	

}
