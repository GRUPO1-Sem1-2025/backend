package com.example.Login.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Login.model.Localidad;
import com.example.Login.model.Usuario;
import com.example.Login.service.LocalidadService;
import com.example.Login.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/localidades")
@Tag(name = "Localidades", description = "API para gestionar localidades")

public class LocalidadController {
	
	@Autowired
    private LocalidadService localidadService;

    //private LocalidadService localidadService;@Autowired
    private LocalidadService fileConversionService;
    
    
    @PostMapping("/agregarlocalidad")
    @Operation(summary = "Crear un usuario", description = "Agrega un nuevo usuario")
    public ResponseEntity<Map<String,String>> crearLocalidad(@RequestBody Localidad localidad) {
    	
    	Optional<Localidad> loc = localidadService.buscarPorNombre(localidad.getNombre());
    	Map<String, String> response = new HashMap<>();
    	
    	if (loc.isPresent()) {
    		response.put("mensaje", "Ya existe una Localidad con el nombre  " + localidad.getNombre());
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    	}
    	
    	localidadService.crearLocalidad(localidad);
    	// 🔹 Prepara la respuesta exitosa
        response.put("mensaje", "Localidad registrada exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // ✅ 201 - Creado
    }
    
    @PostMapping("/crearlocalidadmasivos")
    public ResponseEntity<String> crearLocalidadMasivos(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo procesado esta vacio");
        }

        try {
            // Llamamos al servicio para convertir el archivo a JSON
            String json = fileConversionService.convertCsvToJson(file);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo: " + e.getMessage());
        }
    }
    
    
    }