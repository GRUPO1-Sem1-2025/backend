package com.example.Login.controller;
import com.example.Login.model.Viaje;
import com.example.Login.service.ViajeService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/viajes")
@Tag(name = "Viajes", description = "API para gestionar viajes")
public class ViajeController {
	
	@Autowired
    private ViajeService viajeService;
	
	@PostMapping("/crearViaje")
    @Operation(summary = "Crear un viaje", description = "Agrega un nuevo viaje")
    public ResponseEntity<Map<String,String>> crearViaje(@RequestBody Viaje viaje) {
		
		Map<String, String> response = new HashMap<>();
		
		if(viajeService.crearViaje(viaje)) {
    	// 🔹 Prepara la respuesta exitosa
        response.put("mensaje", "Viaje registrado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // ✅ 201 - Creado
		}else {
			response.put("mensaje", "La localidad de origen/destino eran iguales, o una de ellas no se encontraba en el sistemae");
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response); // ✅ 201 - Creado
		}
		
	}

}
