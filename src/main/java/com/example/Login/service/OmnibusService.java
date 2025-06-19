package com.example.Login.service;

import com.example.Login.dto.DtoBus;
import com.example.Login.dto.DtoCargarLocalidad;
import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.EstadoViaje;
import com.example.Login.model.Asiento;
import com.example.Login.model.Localidad;
import com.example.Login.model.Omnibus;
import com.example.Login.model.OmnibusAsiento;
import com.example.Login.model.Viaje;
import com.example.Login.repository.AsientoRepository;
import com.example.Login.repository.LocalidadRepository;
import com.example.Login.repository.OmnibusAsientoRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import com.example.Login.model.Usuario;
import com.example.Login.repository.OmnibusRepository;
import com.example.Login.repository.ViajeRepository;
//import com.example.Login.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OmnibusService {

	@Autowired
	private OmnibusRepository omnibusRepository;

	@Autowired
	private AsientoRepository asientoRepository;

	@Autowired
	private OmnibusRepository omnibusrepository;

	@Autowired
	private ViajeRepository viajeRepository;

	@Autowired
	private LocalidadRepository localidadRepository;

	@Autowired
	private OmnibusAsientoRepository omnibusasientoRepository;

//	// Inyección de dependencias
//	public OmnibusService(OmnibusRepository omnibusRepository) {
//		this.omnibusasientoRepository = null;
//		this.omnibusRepository = omnibusRepository;
//	}

	public OmnibusService(OmnibusRepository omnibusRepository, ViajeRepository viajeRepository,
			LocalidadRepository localidadRepository, OmnibusAsientoRepository omnibusAsientoRepository) {
		this.omnibusRepository = omnibusRepository;
		this.viajeRepository = viajeRepository;
		this.localidadRepository = localidadRepository;
		this.omnibusasientoRepository = omnibusAsientoRepository;
	}

	public long busesTotales() {
		return omnibusRepository.count();
	}

	// Guardar bus
	public Omnibus crearOmnibus(Omnibus bus) {
		return omnibusRepository.save(bus);
	}

	// public Omnibus agregarAsientoABus(Omnibus bus,Asiento asiento) {
	public void asignarAsientoAOmnibus(Omnibus omnibus, Asiento asiento, boolean estado) {
		OmnibusAsiento relacion = new OmnibusAsiento();
		relacion.setOmnibus(omnibus);
		relacion.setAsiento(asiento);
//		relacion.setEstado(estado);
		omnibus.getOmnibusAsientos().add(relacion);
		asiento.getOmnibusAsientos().add(relacion);
		// OmnibusRepository.save(omnibus);
		omnibusasientoRepository.save(relacion);
	}

	public List<DtoBus> obtenerOmnibusActivos() {
		List<DtoBus> omnibusActivos = new ArrayList<>();
		List<Omnibus> omnibusTotales = omnibusRepository.findAll();
		System.out.println("Cantidad totales: " + omnibusTotales.size());

		for (Omnibus omnibus : omnibusTotales) {
			// if (omnibus.isActivo()) {
			DtoBus bus = new DtoBus();
			bus.setMatricula(omnibus.getMatricula());
			bus.setId(omnibus.getId());
			bus.setActivo(omnibus.isActivo());
			bus.setCant_asientos(omnibus.getCant_asientos());
			bus.setMarca(omnibus.getMarca());
			bus.setLocalidad_actual(omnibus.getLocalidad());
			omnibusActivos.add(bus);
			// }
		}
		return omnibusActivos;
	}

	public int asignarLocalidadAOmnibus(Omnibus omnibus, String nombreLocalidad) {
		int salida;
		try {
			Optional<Localidad> loc = localidadRepository.findByNombre(nombreLocalidad);

			if (loc.isPresent()) {
				if (loc.get().isActivo()) {
					omnibus.setLocalidad(loc.get().getNombre());
					omnibusRepository.save(omnibus);
					return salida = 0;
				} else {
					System.out.println("No está permitido hacer viajes a " + loc.get().getNombre());
					return salida = 1;
				}
			} else {
				System.out.println("No existe una localidad llamada " + nombreLocalidad + " en el sistema");
				return salida = 2;
			}

		} catch (Exception e) {
			System.out.println("Error al buscar la localidad: " + e.getMessage());
			e.printStackTrace();
			return salida = 3;
		}
	}

	public int cambiarEstadoBus(int busId) {
		Optional<Omnibus> Obus = omnibusRepository.findById(busId);
		int cantidadOmnibusAsignadosAViaje = viajeRepository.contarViajesAsignadoABus(busId);
		int cantidadViajesActivosParaBus = viajeRepository.contarViajesActivosAsignadoABus(busId);

		if (cantidadOmnibusAsignadosAViaje == 0 || cantidadViajesActivosParaBus == 0) {

			if (Obus.isPresent()) {
				System.out.println("Encontre el bus");
				Omnibus bus = Obus.get();
				// verificar si existen viajes activos para ese bus
				if (bus.isActivo()) {
					System.out.println("Estado del bus: " + bus.isActivo());
					bus.setActivo(false);
					omnibusRepository.save(bus);
					System.out.println("Estado del bus: " + bus.isActivo());
					return 1;
				} else {
					System.out.println("Estado del bus: " + bus.isActivo());
					bus.setActivo(true);
					omnibusRepository.save(bus);
					System.out.println("Estado del bus: " + bus.isActivo());
					return 2;
				}
			}
			return 3;
		} else {
			return 4;
		}
	}

	public DtoBus obtenerOmnibusPorMatricula(String matricula) {
		DtoBus bus = new DtoBus();
		Omnibus Bus = new Omnibus();
		try {
			Optional<Omnibus> Obus = omnibusRepository.findByMatricula(matricula);
			if (Obus.isPresent()) {
				Bus = Obus.get();
				bus.setActivo(Bus.isActivo());
				bus.setCant_asientos(Bus.getAsientos().size());
				bus.setId(Bus.getId());
				bus.setMarca(Bus.getMarca());
				bus.setMatricula(matricula);
				return bus;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public int obtenerCantidadDeBus() {
		return omnibusRepository.findAll().size();
	}

	public DtoBus obtenerOmnibusPorId(int idBus) {
		DtoBus bus = new DtoBus();
		Omnibus Bus = new Omnibus();
		try {
			Optional<Omnibus> Obus = omnibusRepository.findById(idBus);
			if (Obus.isPresent()) {
				Bus = Obus.get();
				bus.setActivo(Bus.isActivo());
				bus.setCant_asientos(Bus.getAsientos().size());
				bus.setId(Bus.getId());
				bus.setMarca(Bus.getMarca());
				bus.setMatricula(Bus.getMatricula());
				bus.setLocalidad_actual(Bus.getLocalidad());
				return bus;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public String crearBusMasivosConLocalidad(MultipartFile file) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
	    List<Map<String, String>> dataList = new ArrayList<>();

	    String line;
	    int asientosDisponibles = (int) asientoRepository.count();

	    while ((line = reader.readLine()) != null) {
	        Map<String, String> response = new HashMap<>();
	        try {
	            String[] values = line.split(":");
	            if (values.length != 4) {
	                response.put("error", "Formato incorrecto: se esperaban 4 valores separados por ':'");
	                response.put("linea", line);
	                dataList.add(response);
	                continue;
	            }

	            String marca = values[0].trim();
	            String cantAsientosStr = values[1].trim();
	            String matriculaRaw = values[2].trim();
	            String localidadNombre = values[3].trim();

	            if (matriculaRaw.isEmpty()) {
	                response.put("error", "Matrícula vacía");
	                response.put("linea", line);
	                dataList.add(response);
	                continue;
	            }

	            int cantAsientos;
	            try {
	                cantAsientos = Integer.parseInt(cantAsientosStr);
	            } catch (NumberFormatException e) {
	                response.put("error", "Cantidad de asientos no es un número válido");
	                response.put("linea", line);
	                dataList.add(response);
	                continue;
	            }

	            if (cantAsientos > asientosDisponibles) {
	                response.put("error", "No hay suficientes asientos disponibles. Requeridos: " + cantAsientos + ", disponibles: " + asientosDisponibles);
	                response.put("linea", line);
	                dataList.add(response);
	                continue;
	            }

	            String matricula = matriculaRaw.toLowerCase();

	            Optional<Omnibus> encontrado = omnibusRepository.findByMatriculaIgnoreCase(matricula);
	            if (encontrado.isPresent()) {
	                response.put("error", "Ya existe un ómnibus con matrícula: " + matriculaRaw);	                
	                dataList.add(response);
	                continue;
	            }

	            // Crear nuevo objeto Omnibus
	            Omnibus busCreado = new Omnibus();
	            busCreado.setMarca(marca);
	            busCreado.setCant_asientos(cantAsientos);
	            busCreado.setMatricula(matricula);
	            busCreado.setActivo(true);

	            // Verificamos si la localidad existe y está activa ANTES de asignar
	            Optional<Localidad> Olocalidad = localidadRepository.findByNombre(localidadNombre);
	            if (!Olocalidad.isPresent()) {
	                response.put("mensaje", "Ómnibus creado, pero no se asignó localidad porque no existe: " + localidadNombre);
	            } else if (!Olocalidad.get().isActivo()) {
	                response.put("mensaje", "Ómnibus creado, pero no se asignó localidad porque está inactiva: " + localidadNombre);
	            } else {
	                // Solo si es válida, se asigna
	                busCreado.setLocalidadActual(localidadNombre);
	            }

	            // Persistir el ómnibus (una vez preparado con o sin localidad)
	            crearOmnibus(busCreado);

	            // Si la localidad era válida, asignarla en la relación
	            if (Olocalidad.isPresent() && Olocalidad.get().isActivo()) {
	                asignarLocalidadAOmnibus(busCreado, localidadNombre);
	            }

	            // Asignar asientos disponibles
	            for (int i = 1; i <= cantAsientos; i++) {
	                Optional<Asiento> asientoOpt = asientoRepository.findByNro(i);
	                if (asientoOpt.isPresent()) {
	                    asignarAsientoAOmnibus(busCreado, asientoOpt.get(), true);
	                } else {
	                    System.out.println("No existe asiento nro: " + i);
	                }
	            }

	            //response.put("exito", "Ómnibus creado con matrícula: " + matriculaRaw);

	        } catch (Exception e) {
	            response.put("error", "Error procesando línea: " + e.getMessage());
	            response.put("linea", line);
	        }
	        dataList.add(response);
	    }

	    ObjectMapper objectMapper = new ObjectMapper();
	    return objectMapper.writeValueAsString(dataList);
	}
	
}