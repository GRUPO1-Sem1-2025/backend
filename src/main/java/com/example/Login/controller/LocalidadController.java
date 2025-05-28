package com.example.Login.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Login.dto.DtoDestinoMasVistos;
import com.example.Login.dto.DtoLocalidad;
import com.example.Login.model.Localidad;
import com.example.Login.service.LocalidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/localidades")
@Tag(name = "Localidades", description = "API para gestionar localidades")

public class LocalidadController {
	
	@Autowired
    private LocalidadService localidadService;

    //private LocalidadService localidadService;
	@Autowired
    private LocalidadService fileConversionService;
    
    
    @PostMapping("/agregarlocalidad")
    @Operation(summary = "Crear una localidad", description = "Agrega una nueva localidad")
    public ResponseEntity<Map<String,String>> crearLocalidad(@RequestBody DtoLocalidad dtoLocalidad) {
    	
    	Optional<Localidad> loc = localidadService.buscarPorNombre(dtoLocalidad.getNombre());
    	Map<String, String> response = new HashMap<>();
    	
    	if (loc.isPresent()) {
    		response.put("mensaje", "Ya existe una Localidad con el nombre  " + dtoLocalidad.getNombre());
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    	}
    	
    	localidadService.crearLocalidad(dtoLocalidad);
    	// 🔹 Prepara la respuesta exitosa
        response.put("mensaje", "Localidad registrada exitosamente");
        return ResponseEntity.status(HttpStatus.OK).body(response); // ✅ 201 - Creado
    }
    
    @PostMapping("/crearLocalidadMasivas")
    public ResponseEntity<String> crearLocalidadMasivos(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo procesado esta vacio");
        }

        try {
            // Llamamos al servicio para convertir el archivo a JSON
            String json = fileConversionService.crearLocalidadesMasivas(file);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo: " + e.getMessage());
        }
    }
    
    @GetMapping("/obtenerLocalidadesActivas")
    @Operation(summary = "Obtener localidades activas", description = "Retorna las localidades activas")    
    public List<Localidad> obtenerLocalidadesActivas () {
        return localidadService.obtenerLocalidadesActivas();
        }
    
    @GetMapping("/localidadesMasVisitadas")
    public List<DtoDestinoMasVistos> obtenerTop10DestinosConNombre(){
    	return localidadService.obtenerTop10DestinosConNombre();
    }
    
    
    }
