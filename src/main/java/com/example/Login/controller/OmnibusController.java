package com.example.Login.controller;

import com.example.Login.dto.DtoBus;
import com.example.Login.dto.DtoCargarLocalidad;
import com.example.Login.model.Asiento;
import com.example.Login.model.Omnibus;
import com.example.Login.service.OmnibusService;

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

import com.example.Login.repository.AsientoRepository;
import com.example.Login.repository.OmnibusRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/buses")
@Tag(name = "Buses", description = "API para gestionar buses")
public class OmnibusController {

	// private final controller.UsuarioController usuarioController;

	@Autowired
	private AsientoRepository asientoRepository;

	@Autowired
	private OmnibusService omnibusService;

	@Autowired
	private OmnibusRepository omnibusrepository;

	public OmnibusController(OmnibusService omnibusService, AsientoRepository asientoRepository) {
		this.omnibusService = omnibusService;
		this.asientoRepository = asientoRepository;
		// this.usuarioController = usuarioController;
	}

	@PostMapping("/crearOmnibus")
	@Operation(summary = "Crear un bus", description = "Agrega un bus")
	public ResponseEntity<Map<String, String>> crearOmnibus(@RequestBody DtoBus dtoBus) {
		Omnibus bus = new Omnibus();
		bus.setCant_asientos(dtoBus.getCant_asientos());
		bus.setMarca(dtoBus.getMarca());
		bus.setActivo(true);// dtoBus.isActivo());
		Map<String, String> response = new HashMap<>();

		try {
			Optional<Omnibus> Obus = omnibusrepository.findByMatricula(dtoBus.getMatricula());
			if (!Obus.isPresent()) {
				bus.setMatricula(dtoBus.getMatricula());
			} else {
				response.put("mensaje", "Ya existe un omnibus ingresado con esa matricula");
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
			}
		} catch (Exception e) {

		}

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
			return ResponseEntity.status(HttpStatus.OK).body(response); // 201 - Creado
		} else {
			response.put("mensaje", "El Bus no se puede crear con esa candidad de asientos, debe de tener menos de "
					+ asientosDisponibles + " asientos");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@PostMapping("/crearOmnibusConLocalidad")
	@Operation(summary = "Crear un bus", description = "Agrega un bus")
	public ResponseEntity<Map<String, String>> crearOmnibusConLocalidad(@RequestBody DtoBus dtoBus) {
		Omnibus bus = new Omnibus();
		bus.setCant_asientos(dtoBus.getCant_asientos());
		bus.setMarca(dtoBus.getMarca());
		bus.setActivo(true);// dtoBus.isActivo());
		Map<String, String> response = new HashMap<>();

		try {
			Optional<Omnibus> Obus = omnibusrepository.findByMatricula(dtoBus.getMatricula());
			if (!Obus.isPresent()) {
				bus.setMatricula(dtoBus.getMatricula());
			} else {
				response.put("mensaje", "Ya existe un omnibus ingresado con esa matricula");
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
			}
		} catch (Exception e) {

		}

		long totalAsientos = bus.getCant_asientos();
		long asientosDisponibles = asientoRepository.count();
		boolean estadoAsiento = true;
		int i;

		if (totalAsientos < asientosDisponibles) {
			omnibusService.crearOmnibus(bus);

			// Agregar Localidad
			DtoCargarLocalidad cargarLocalidad = new DtoCargarLocalidad();
			cargarLocalidad.setNombreLocalidad(dtoBus.getLocalidad_actual());
			System.out.println("localidad_actual: " + dtoBus.getLocalidad_actual());
			cargarLocalidad.setId_bus(omnibusrepository.findUltimoId());
			asignarLocalidadAOmnibus(cargarLocalidad);
			// fin Agregar Localidad

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
			return ResponseEntity.status(HttpStatus.OK).body(response); // 201 - Creado
		} else {
			response.put("mensaje", "El Bus no se puede crear con esa candidad de asientos, debe de tener menos de "
					+ asientosDisponibles + " asientos");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@GetMapping("/obtenerOmnibusActivos")
	@Operation(summary = "Obtener omnibus activos", description = "Retorna los omnibus activos")
	public List<DtoBus> obtenerOmnibusActivos() {
		return omnibusService.obtenerOmnibusActivos();
	}

	@PostMapping("/asignarLocalidad")
	@Operation(summary = "Asigna localidad a un omnibus", description = "Agrega una localidad a un bus")
	public ResponseEntity<Map<String, String>> asignarLocalidadAOmnibus(
			@RequestBody DtoCargarLocalidad cargarLocalidad) {
		int retornoServicio;
		Map<String, String> response = new HashMap<>();
		Optional<Omnibus> omnibus = omnibusrepository.findById(cargarLocalidad.getId_bus());
		System.out.println("idBus: " + cargarLocalidad.getId_bus());

		if (omnibus.isPresent()) {
			retornoServicio = omnibusService.asignarLocalidadAOmnibus(omnibus.get(),
					cargarLocalidad.getNombreLocalidad());
			switch (retornoServicio) {
			case 0:
				response.put("mensaje", "Se modifico la localidad en la cual se encuentra el omnibus actualmente");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			case 1:
				response.put("mensaje", "No esta permitido hacer viajes a " + cargarLocalidad.getNombreLocalidad());
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
			case 2:
				response.put("mensaje",
						"No existe una localidad llamada " + cargarLocalidad.getNombreLocalidad() + " en el sistema");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		} else {
			response.put("mensaje", "No existe un omnibus que contenga ese ID");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		response.put("mensaje", "Error desconocido");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@PostMapping("/cambiarEstadoBus")
	@Operation(summary = "Se cambia el estado de un omnibus para darlo de baja", description = "se marca el bus como en desuso")
	public ResponseEntity<Map<String, String>> cambiarEstadoBus(int busId) {
		Map<String, String> response = new HashMap<>();
		int resultado = omnibusService.cambiarEstadoBus(busId);

		switch (resultado) {
		case 1:
			response.put("mensaje", "Se deshabilito el Omnibus");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		case 2:
			response.put("mensaje", "Se habilito el Omnibus");
			return ResponseEntity.status(HttpStatus.OK).body(response);

		case 3:
			response.put("mensaje", "No existe un omnibus con ese id");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		case 4:
			response.put("mensaje",
					"El omnibus esta asignado a algun viaje activo, por lo tanto no puede ser deshabilitado");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		response.put("mensaje", "Error desconocido");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@GetMapping("/obtenerOmnibusPorMatricula")
	public DtoBus obtenerOmnibusPorMatricula(@RequestParam String matricula) {
		DtoBus bus = new DtoBus();
		bus = omnibusService.obtenerOmnibusPorMatricula(matricula);
		return bus;
	}

	@GetMapping("/obtenerCantidadDeOmnibus")
	public int obtenerCantidadDeOmnibus() {
		return omnibusService.obtenerCantidadDeBus();
	}

	@GetMapping("/obtenerOmnibusPorId")
	public DtoBus obtenerOmnibusPorId(@RequestParam int idBus) {
		DtoBus bus = new DtoBus();
		bus = omnibusService.obtenerOmnibusPorId(idBus);
		return bus;
	}
	
	@PostMapping("/crearBusMasivosConLocalidad")
	public ResponseEntity<String> crearBusMasivosConLocalidad(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo procesado esta vacio");
		}

		try {
			// Llamamos al servicio para convertir el archivo a JSON
			String json = omnibusService.crearBusMasivosConLocalidad(file);
			return ResponseEntity.ok(json);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al procesar el archivo: " + e.getMessage());
		}
	}

}
