package com.example.Login.controller;

import com.example.Login.model.Asiento;
import com.example.Login.model.Omnibus;
import com.example.Login.service.AsientoService;
import com.example.Login.service.OmnibusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Login.model.Usuario;
import com.example.Login.service.AsientoService;
import com.example.Login.repository.AsientoRepository;
import com.example.Login.repository.OmnibusRepository;
import com.example.Login.service.OmnibusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/buses")
@Tag(name = "Buses", description = "API para gestionar buses")
public class OmnibusController {

    //private final controller.UsuarioController usuarioController;

	@Autowired
	private AsientoRepository asientoRepository;

	@Autowired
	private AsientoService asientoService;

	@Autowired
	private OmnibusService omnibusService;
	

	public OmnibusController(OmnibusService omnibusService, AsientoRepository asientoRepository){
		this.omnibusService = omnibusService;
		this.asientoRepository = asientoRepository;
		//this.usuarioController = usuarioController;
	}
	
	@PostMapping
	@Operation(summary = "Crear un bus", description = "Agrega un bus")
	public ResponseEntity<Map<String, String>> crearOmnibus(@RequestBody Omnibus bus) {
		
		Map<String, String> response = new HashMap<>();
		long totalAsientos = bus.getCant_asientos();
		long asientosDisponibles = asientoRepository.count();
		boolean estadoAsiento = true;
		int i;

		if (totalAsientos < asientosDisponibles) {
			omnibusService.crearOmnibus(bus);
			for (i = 1; i <= totalAsientos; i++) {
				System.out.println("valor de i " + i);

				try {
					Optional<Asiento> a = asientoRepository.findByNro(i);
					if (a != null && a.isPresent()) {
						Asiento asiento = a.get();
						System.out.println("Asiento encontrado con nro: " + i + " (id=" + a.get().getId() + ")");
						omnibusService.asignarAsientoAOmnibus(bus, asiento, estadoAsiento);
						System.out.println("Asiento agregado correctamente");
						response.put("mensaje", "Asiento agregado correctamente");
					} else {
						System.out.println("Asiento no encontrado");
						response.put("mensaje", "Asiento no encontrado");
					}
				} catch (Exception e) {
					System.out.println("Error en iteración i=" + i + ": " + e.getMessage());
					e.printStackTrace();
				}
			}
			response.put("mensaje", "Bus registrado exitosamente");
			return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 - Creado
		} else {
			response.put("mensaje", "El Bus no se puede crear con esa candidad de asientos, debe de tener menos de "
					+ asientosDisponibles + " asientos");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
		}
	}

	@GetMapping ("/asientoslibres")
	@Operation(summary = "Mostrar asientos libres", description = "mostrar asientos libres")	
	public ResponseEntity<Map<String,String>> mostrarAsientosLibres(@RequestParam int id){
		Map<String, String> response = new HashMap<>();
		List<Integer> asientosLibres = omnibusService.mostrarAsientosLibres(id);
		
		response.put("mensaje", "El omnibus tiene los asientos " + asientosLibres + " libres");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/cambiarestadoasiento")
	@Operation(summary = "Cambiar estado de asiento", description = "cambiar estado de asiento")
	public ResponseEntity<Map<String, String>> cambiarEstadoAsiento(@RequestParam int bus_id,
			@RequestParam int nro_asiento) {
		Map<String, String> response = new HashMap<>();

		boolean modificado = omnibusService.cambiarEstadoAsiento(bus_id, nro_asiento);

		if (modificado) {
			response.put("mensaje", "Se le cambió el estado al asiento nro " + nro_asiento);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("mensaje",
					"No se pudo cambiar el estado del asiento. Verifique si el bus está activo y si el asiento existe.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	


}
