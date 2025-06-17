package com.example.Login.controller;

import com.example.Login.dto.DtoCalificacion;
import com.example.Login.dto.DtoCalificarViaje;
import com.example.Login.dto.DtoCompraViaje;
import com.example.Login.dto.DtoTipoDeCompra;
import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.DtoViajeCompleto;
import com.example.Login.dto.DtoViajeDestinoFecha;
import com.example.Login.dto.DtoViajeIdDestino;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.Omnibus;
import com.example.Login.model.Viaje;
import com.example.Login.repository.OmnibusRepository;
import com.example.Login.repository.ViajeRepository;
import com.example.Login.service.OmnibusService;
import com.example.Login.service.UsuarioService;
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
import org.springframework.web.multipart.MultipartFile;

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
	
	@Autowired
	private ViajeService fileConversionService;

	@PostMapping("/crearViaje")
	@Operation(summary = "Crear un viaje", description = "Agrega un nuevo viaje")
	public ResponseEntity<Map<String, String>> crearViaje(@RequestBody DtoViaje dtoViaje) {

		int respuesta = viajeService.crearViaje(dtoViaje);
		Map<String, String> response = new HashMap<>();

		switch (respuesta) {
		case 1:
			response.put("mensaje", "La ciudad de origen y destino no pueden ser las mismas");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		case 2:
			response.put("mensaje", "Una de las ciudades no se encuentra disponible");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		case 3:
			response.put("mensaje", "Viaje registrado exitosamente");
			return ResponseEntity.status(HttpStatus.OK).body(response); // ✅ 201 - Creado
		case 4:
			response.put("mensaje", "Una de las ciudads ingresadas no existe en el sistema");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response); //
		}
		response.put("mensaje", "Error Desconcido");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); //
	}
	
	@PostMapping("/crearViajeConBus")
	@Operation(summary = "Crear un viaje", description = "Agrega un nuevo viaje")
	public ResponseEntity<Map<String, String>> crearViajeConBus(@RequestBody DtoViaje dtoViaje) {

		int respuesta = viajeService.crearViajeConBus(dtoViaje);
		Map<String, String> response = new HashMap<>();

		switch (respuesta) {
		case 1:
			response.put("mensaje", "Se creo el viaje de forma exitosa y se le asigno"+
			" el omnibus de forma correcta");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		case 2:
			response.put("error", "Viaje creado de forma exitosa pero no se le puede"+
		" asigar el bus porque el mismo no existe");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		case 3:
			response.put("error", "La ciudad de origen y destino no pueden ser las mismas");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		case 6:
			response.put("mensaje", "Viaje creado de forma exitosa, pero no se le puede"+
		" asigar el bus porque el mismo ya esta asignado a un viaje en proceso");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		case 4:
			response.put("mensaje", "Viaje creado de forma exitosa, pero no se puede asignar el viaje"+
		" porque el bus no se encuentra en la localidad origen del viaje");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
		case 5:
			response.put("mensaje", "Viaje creado de forma exitosa, pero no se le puede asigar el bus,"+
		" porque el mismo esta inactivo");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response); //
		
		case 7:
			response.put("mensaje", "Viaje creado de forma exitosa, pero no se puede asigar el"+
		" bus, porque no dispone de los asientos libres necesarios");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		case 8:
			response.put("error", "Una de las ciudades no se encuentra disponible");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		case 9:
			response.put("error", "Una de las ciudades ingresadas no existe");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		response.put("mensaje", "Error Desconcido");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); //
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
			case 5:
				response.put("mensaje", "No se puede asignar el omnibus, dado que el mismo se encuentra inactivo");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			case 6:
				response.put("mensaje", "No se le puede asigar el bus, porque el mismo ya esta asignado a un viaje en proceso");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			case 7:
				response.put("mensaje", "No se puede asigar el bus, porque no dispone de los asientos libres necesarios");
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

		DtoViaje dtoVDF = new DtoViaje();
		dtoVDF.setFechaFin(fechaFin);
		dtoVDF.setFechaInicio(fechaInicio);
		dtoVDF.setIdLocalidadOrigen(locOrigen);
		dtoVDF.setIdLocalidadDestino(locDestino);
		List<DtoViajeDestinoFecha> Dtovdf = new ArrayList<>();
		Dtovdf = viajeService.obtenerViajesPorFechaYDestino(dtoVDF);
		return ResponseEntity.status(HttpStatus.OK).body(Dtovdf);
	}

	@GetMapping("/obtenerAsientosDisponibles")
	public List<Integer> asientosDisponibles(@RequestParam int idViaje) {
		return viajeService.asientosDisponibles(idViaje);
	}

	@GetMapping("/cancelarViaje")
	public ResponseEntity<Map<String, String>> cancelarViaje(@RequestParam Long idViaje) {
		Map<String, String> response = new HashMap<>();

		if (viajeService.cancelarViaje(idViaje)) {
			response.put("mensaje", "El viaje fue cancelado de forma correcta.");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
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

	@GetMapping("/cantidadDeViajesCreados")
	public int cantidadDeViajesCreados() {
		return viajeService.cantidadDeViajesCreados();
	}

	@PostMapping("/calificarViaje")
	public ResponseEntity<Map<String, String>> calificarViaje(@RequestBody DtoCalificarViaje dtoCalificar) {
		
		Map<String, String> response = new HashMap<>();
		int resultado = viajeService.calificarViaje(dtoCalificar);

		if (resultado == 1) {
			response.put("mensaje", "Viaje calificado de forma correcta");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else if(resultado == 2) {
			response.put("error", "La calificación debe de estar entre 1 y 5");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		else {
			response.put("error", "Viaje no calificado");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@GetMapping("/verCalificacionComentario")
	public DtoCalificacion verCalificacionYComentariosDeViaje(@RequestParam int idViaje) {
		System.out.println("entre al controlador de verCalificacionComentario");
		return viajeService.verCalificacionYComentariosDeViaje(idViaje);
	}
	
	@GetMapping("/obtenerViajes")
	public List<DtoViajeCompleto> obtenerViajes() {
		List<DtoViajeCompleto> respuesta = new ArrayList<>();
		respuesta = viajeService.obtenerViajes();
		return respuesta;
	}
	
	@GetMapping("/obtenerViajesIdDestino")
	public List<DtoViajeIdDestino> obtenerViajesIdDestino() {
		List<DtoViajeIdDestino> respuesta = new ArrayList<>();
		respuesta = viajeService.obtenerViajesIdDestino();
		return respuesta;
	}
	
	@GetMapping("/obtenerCompraViaje")
	public DtoCompraViaje obtenerCompraViaje(@RequestParam int idViaje, @RequestParam int idCompra, @RequestParam int idUsuario){
		return viajeService.obtenerCompraViaje(idViaje, idCompra, idUsuario);
	}
	
	@GetMapping("/obtenerViajeId")
	public DtoViajeCompleto obtenerViajeId(@RequestParam int idViaje) {
		DtoViajeCompleto resultado = new DtoViajeCompleto();		
		resultado = viajeService.obtenerViajeId(idViaje);
		return resultado;
	}
	
	@GetMapping("/obtenerViajePorDestino")
	public List<DtoViajeCompleto> obtenerViajePorDestino(@RequestParam String destino) {
		List<DtoViajeCompleto> resultado = new ArrayList<>();		
		resultado = viajeService.obtenerViajePorDestino(destino);
		return resultado;
	}
	
	@GetMapping("/obtenerViajePorOrigen")
	public List<DtoViajeCompleto> obtenerViajePorOrigen(@RequestParam String origen) {
		List<DtoViajeCompleto> resultado = new ArrayList<>();		
		resultado = viajeService.obtenerViajePorOrigen(origen);
		return resultado;
	}
	
	@GetMapping("/obtenerViajeMejorCalificados")
	public List<DtoViajeCompleto> obtenerViajeMejorCalificados() {
		List<DtoViajeCompleto> resultado = new ArrayList<>();		
		resultado = viajeService.obtenerViajeMejorCalificados();
		return resultado;
	}
	
	@PostMapping("/crearViajesMasivos")
	public ResponseEntity<String> crearViajesMasivos(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo procesado esta vacio");
		}

		try {
			// Llamamos al servicio para convertir el archivo a JSON
			String json = fileConversionService.crearViajesMasivos(file);
			return ResponseEntity.ok(json);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al procesar el archivo: " + e.getMessage());
		}
	}

}
