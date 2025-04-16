package com.example.Login.controller;

import com.example.Login.model.Omnibus;
import com.example.Login.model.Viaje;
import com.example.Login.repository.OmnibusRepository;
import com.example.Login.repository.ViajeRepository;
import com.example.Login.service.OmnibusService;
import com.example.Login.service.ViajeService;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/viajes")
@Tag(name = "Viajes", description = "API para gestionar viajes")
public class ViajeController {

	@Autowired
	private ViajeService viajeService;

	@Autowired
	private OmnibusService omnibusService;

	@Autowired
	private OmnibusRepository omnibusRepository;

	@Autowired
	private ViajeRepository viajeRepository;

	@PostMapping("/crearViaje")
	@Operation(summary = "Crear un viaje", description = "Agrega un nuevo viaje")
	public ResponseEntity<Map<String, String>> crearViaje(@RequestBody Viaje viaje) {

		Map<String, String> response = new HashMap<>();

		if (viajeService.crearViaje(viaje)) {
			// 🔹 Prepara la respuesta exitosa
			response.put("mensaje", "Viaje registrado exitosamente");
			return ResponseEntity.status(HttpStatus.CREATED).body(response); // ✅ 201 - Creado
		} else {
			response.put("mensaje",
					"La localidad de origen/destino eran iguales, o una de ellas no se encontraba en el sistemae");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response); // ✅ 201 - Creado
		}

	}@PostMapping("/agregarBusAViaje")
	@Operation(summary = "Asignar bus a viaje", description = "Agrega un bus a un viaje")
	public ResponseEntity<Map<String, String>> asignarOmnibusAViaje(@RequestParam int id_viaje, @RequestParam int id_bus) {
	    Map<String, String> response = new HashMap<>();

	    Optional<Omnibus> bus = omnibusRepository.findById(id_bus);
	    Optional<Viaje> viaje = viajeRepository.findById(id_viaje);

	    if (bus.isPresent() && viaje.isPresent()) {
	        int resultado = viajeService.asignarOmnibusAViaje(bus.get(), viaje.get());

	        switch (resultado) {
	            case 1:
	                response.put("mensaje", "Se asignó de forma correcta el ómnibus al viaje.");
	                return ResponseEntity.status(HttpStatus.OK).body(response);
	            case 4:
	                response.put("mensaje", "No se puede asignar el viaje ya que el bus no se encuentra en la localidad origen del viaje.");
	                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
	            case 2:
	                response.put("mensaje", "No se encontró el ómnibus.");
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	            case 3:
	                response.put("mensaje", "Ocurrió un error inesperado al asignar el ómnibus al viaje.");
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	            default:
	                response.put("mensaje", "Resultado desconocido.");
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    } else {
	        response.put("mensaje", "No se encontró el viaje o el ómnibus con los IDs proporcionados.");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	}
	
	

//	@PostMapping("/agregarBusAViaje")
//	@Operation(summary = "Asignar bus a viaje", description = "Agrega un bus a un viaje")
//	public ResponseEntity<Map<String, String>> asignarOmnibusAViaje(@RequestParam int id_viaje, int id_bus) {
//		int resultado;
//		Map<String, String> response = new HashMap<>();
//
//		Optional<Omnibus> bus = omnibusRepository.findById(id_bus);
//		Optional<Viaje> viaje = viajeRepository.findById(id_viaje);
//		resultado = viajeService.asignarOmnibusAViaje(bus.get(), viaje.get());
//
//		switch (resultado) {
//		case 4:
//			response.put("mensaje",
//					"No se puede asignar el viaje ya que el bus no se encuentra en la localida" + "origen del viaje");
//			return ResponseEntity.status(HttpStatus.OK).body(response);
//		case 1:
//			response.put("mensaje", "Se asigno de forma correcta el omnibus al viaje");
//			return ResponseEntity.status(HttpStatus.OK).body(response);
//
//		// case 3:
//		}
//
//		response.put("mensaje", "Error desconocido");
//		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
//	}
}
