package com.example.Login.controller;

import com.example.Login.model.Asiento;
import com.example.Login.service.AsientoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Login.service.UsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("asientos")
@Tag(name = "Asientos", description = "API para gestionar asientos")
public class AsientoController {
	
	private final AsientoService asientoService;

    public AsientoController(AsientoService asientoService) {
        this.asientoService = asientoService;
    }
    
    //@Autowired
    //private AsientoService fileConversionService;
    
	@PostMapping("/crearAsientosMasivos")
	public ResponseEntity<String> cargarAsientosMasivos(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo procesado esta vacio");
		}

		try {
			// Llamamos al servicio para convertir el archivo a JSON
			//String json = fileConversionService.convertCsvToJson(file);
			String json = asientoService.convertCsvToJson(file);
			return ResponseEntity.ok(json);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al procesar el archivo: " + e.getMessage());
		}
	}

}
