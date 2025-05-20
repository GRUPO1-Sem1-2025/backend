package com.example.Login.controller;

import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.DtoViajeDestinoFecha;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.Omnibus;
import com.example.Login.model.Viaje;
import com.example.Login.repository.OmnibusRepository;
import com.example.Login.repository.ViajeRepository;
import com.example.Login.service.OmnibusService;
import com.example.Login.service.ViajeService;

import java.sql.Date;
import java.util.ArrayList;
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
	public ResponseEntity<Map<String, String>> crearViaje(@RequestBody DtoViaje dtoViaje) {

		int respuesta = viajeService.crearViaje(dtoViaje);
		Map<String, String> response = new HashMap<>();

		switch (respuesta) {
		case 1:
			response.put("mensaje", "La ciudad de origen y destino no pueden ser las mismas");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
		case 2:
			response.put("mensaje", "Una de las ciudades no se encuentra disponible");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
		case 3:
			response.put("mensaje", "Viaje registrado exitosamente");
			return ResponseEntity.status(HttpStatus.CREATED).body(response); // ✅ 201 - Creado
		case 4:
			response.put("mensaje", "Una de las ciudads ingresadas no existe en el sistema");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response); //
		}
		response.put("mensaje", "Error Desconcido");
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response); //
	}

	@PostMapping("/agregarBusAViaje")
	@Operation(summary = "Asignar bus a viaje", description = "Agrega un bus a un viaje")
	public ResponseEntity<Map<String, String>> asignarOmnibusAViaje(@RequestParam int id_viaje,
			@RequestParam int id_bus) {
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
				response.put("mensaje",
						"No se puede asignar el viaje ya que el bus no se encuentra en la localidad origen del viaje.");
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

	@GetMapping("/obtenerViajesPorFechaYDestino")
	@Operation(summary = "obtenerViajesPorFechaYDestino", description = "obtenerViajesPorFechaYDestino")
	public ResponseEntity<List<DtoViajeDestinoFecha>> obtenerViajesPorFechaYDestino(@RequestParam int locOrigen,
			@RequestParam int locDestino, @RequestParam Date fechaInicio, @RequestParam Date fechaFin) {
		System.out.println("Cdestino: " + locOrigen);// dtoVDF.getIdLocalidadDestino());
		System.out.println("Corigen: " + locDestino);// dtoVDF.getIdLocalidadOrigen());
		System.out.println("Cinicio: " + fechaInicio);// dtoVDF.getFechaInicio());// IdLocalidadOrigen());
		System.out.println("Cfin: " + fechaFin);// dtoVDF.getFechaFin());// IdLocalidadOrigen());
		DtoViaje dtoVDF = new DtoViaje();
		dtoVDF.setFechaFin(fechaFin);
		dtoVDF.setFechaInicio(fechaInicio);
		dtoVDF.setIdLocalidadOrigen(locOrigen);
		dtoVDF.setIdLocalidadDestino(locDestino);
		List<DtoViajeDestinoFecha> Dtovdf = new ArrayList<>();
		Dtovdf = viajeService.obtenerViajesPorFechaYDestino(dtoVDF);
		// response.put("mensaje", "No se encontró el viaje o el ómnibus con los IDs
		// proporcionados.");
		return ResponseEntity.status(HttpStatus.OK).body(Dtovdf);
	}

	@GetMapping("/obtenerAsientosDisponibles")
	public List<Integer> asientosDisponibles(@RequestParam int idViaje) {
		return viajeService.asientosDisponibles(idViaje);
	}

	@GetMapping("/cancelarViaje")
	public ResponseEntity<Map<String, String>> cancelarViaje(@RequestParam Long idViaje) {
		Map<String, String> response = new HashMap<>();
		
		if(viajeService.cancelarViaje(idViaje)) {
			response.put("mensaje",
					"El viaje fue cancelado de forma correcta.");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else {
			response.put("mensaje", "No se puede cancelar el viaje porque no existe o ya esta cancelado.");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
		}
	}
	
	@GetMapping("/obtenerViajesPorBus")
	public List<DtoViaje> obtenerViajesPorBus(@RequestParam int idBus) {
		List<DtoViaje> respuesta = new ArrayList<>();
		respuesta = viajeService.obtenerViajesPorBus(idBus);
		return respuesta;
	}
	
	

}
