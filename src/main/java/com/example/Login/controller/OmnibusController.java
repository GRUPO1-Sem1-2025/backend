package com.example.Login.controller;

import com.example.Login.model.Asiento;
import com.example.Login.model.Omnibus;
import com.example.Login.service.AsientoService;
import com.example.Login.service.OmnibusService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Login.model.Usuario;
import com.example.Login.service.AsientoService;
import com.example.Login.repository.AsientoRepository;
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
    public ResponseEntity<Map<String,String>> crearOmnibus(@RequestBody Omnibus bus) {
		omnibusService.crearOmnibus(bus);
		Map<String, String> response = new HashMap<>();
		int totalAsientos = bus.getCant_asientos();
		System.out.println("cantidad de asientos " + totalAsientos );
		boolean estadoAsiento = true;
		int i;
		
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
	}	

}
