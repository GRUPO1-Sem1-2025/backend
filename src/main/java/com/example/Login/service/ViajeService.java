package com.example.Login.service;

import com.example.Login.repository.AsientoPorViajeRepository;
import com.example.Login.repository.CompraPasajeRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToIntFunction;

import com.example.Login.repository.ViajeRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.lang.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Login.dto.DtoCalificacion;
import com.example.Login.dto.DtoCalificarViaje;
import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.DtoCompraViaje;
import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.DtoViajeCompleto;
import com.example.Login.dto.DtoViajeDestinoFecha;
import com.example.Login.dto.DtoViajeIdDestino;
import com.example.Login.dto.EstadoCompra;
import com.example.Login.dto.EstadoViaje;
import com.example.Login.dto.categoriaUsuario;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Localidad;
import com.example.Login.model.Omnibus;
import com.example.Login.model.OmnibusAsiento;
import com.example.Login.model.OmnibusAsientoViaje;
import com.example.Login.model.Token;
import com.example.Login.model.Usuario;
import com.example.Login.repository.LocalidadRepository;
import com.example.Login.repository.OmnibusRepository;
import com.example.Login.repository.TokenRepository;
import com.example.Login.model.Viaje;

@Service
public class ViajeService {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private LocalidadRepository localidadRepository;

	@Autowired
	CompraPasajeRepository compraPasajeRepository;

	@Autowired
	private AsientoPorViajeRepository asientoPorViajeRepository;

	@Autowired
	private ViajeRepository viajeRepository;

	@Autowired
	private OmnibusRepository omnibusRepository;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CompraPasajeService compraPasajeService;

//	ViajeService(UsuarioService usuarioService) {
//		this.usuarioService = usuarioService;
//	}

	public ViajeService(LocalidadRepository localidadRepository, ViajeRepository viajeRepository,
			OmnibusRepository omnibusRepository, UsuarioService usuarioService) {
		this.localidadRepository = localidadRepository;
		this.viajeRepository = viajeRepository;
		this.omnibusRepository = omnibusRepository;
		this.usuarioService = usuarioService;
	}

	public int crearViaje(DtoViaje dtoViaje) {

		Viaje nuevoViaje = new Viaje();

		Optional<Localidad> locOri = localidadRepository.findById(dtoViaje.getIdLocalidadOrigen());
		Optional<Localidad> locDest = localidadRepository.findById(dtoViaje.getIdLocalidadDestino());

		if (locOri.isPresent() && locDest.isPresent()) {
			if (locOri.get().getId() == locDest.get().getId()) {
				System.out.println("La ciudad de origen y destino no pueden ser las mismas");
				return 1;
			}

			if (!locOri.get().isActivo() || !locDest.get().isActivo()) {
				System.out.println("Una de las ciudades no se encuentra disponible");
				return 2;
			}

			nuevoViaje.setFechaFin(dtoViaje.getFechaFin());
			nuevoViaje.setFechaInicio(dtoViaje.getFechaInicio());
			nuevoViaje.setHoraInicio(dtoViaje.getHoraInicio());
			nuevoViaje.setHoraFin(dtoViaje.getHoraFin());
			nuevoViaje.setLocalidadOrigen(locOri.get());
			nuevoViaje.setLocalidadDestino(locDest.get());
			nuevoViaje.setPrecio(dtoViaje.getPrecio());
			nuevoViaje.setEstadoViaje(EstadoViaje.NUEVO);

			// No se asigna aún el omnibus ni los asientos
			nuevoViaje.setOmnibus(null);
			nuevoViaje.setAsientosPorViaje(new ArrayList<>());

			viajeRepository.save(nuevoViaje);
			return 3;
		}

		System.out.println("Una de las ciudades ingresadas no existe");
		return 4;
	}

//	public int crearViajeConBus(DtoViaje dtoViaje) {
//
//		Viaje nuevoViaje = new Viaje();
//
//		Optional<Localidad> locOri = localidadRepository.findById(dtoViaje.getIdLocalidadOrigen());
//		Optional<Localidad> locDest = localidadRepository.findById(dtoViaje.getIdLocalidadDestino());
//
//		if (locOri.isPresent() && locDest.isPresent()) {
//			if (locOri.get().getId() == locDest.get().getId()) {
//				System.out.println("La ciudad de origen y destino no pueden ser las mismas");
//				return 1;
//			}
//
//			if (!locOri.get().isActivo() || !locDest.get().isActivo()) {
//				System.out.println("Una de las ciudades no se encuentra disponible");
//				return 2;
//			}
//
//			nuevoViaje.setFechaFin(dtoViaje.getFechaFin());
//			nuevoViaje.setFechaInicio(dtoViaje.getFechaInicio());
//			nuevoViaje.setHoraInicio(dtoViaje.getHoraInicio());
//			nuevoViaje.setHoraFin(dtoViaje.getHoraFin());
//			nuevoViaje.setLocalidadOrigen(locOri.get());
//			nuevoViaje.setLocalidadDestino(locDest.get());
//			nuevoViaje.setPrecio(dtoViaje.getPrecio());
//			nuevoViaje.setEstadoViaje(EstadoViaje.NUEVO);
//			nuevoViaje.setAsientosPorViaje(new ArrayList<>());
//			viajeRepository.save(nuevoViaje);
//
//			// buscar omnibus
//			Omnibus bus = new Omnibus();
//			try {
//				Optional<Omnibus> Obus = omnibusRepository.findById(dtoViaje.getIdOmnibus());
//				bus = Obus.get();
////				if (!bus.isSePuedeUtilizar()) {
////					System.out.println(
////							"No se le puede asigar el bus, porque el mismo ya esta asignado a un viaje en proceso");
////					return 6;
////				}
//
//				if (omnibusDisponible(bus.getId(), nuevoViaje.getFechaInicio(), nuevoViaje.getHoraInicio()) == false) {
//					System.out.println(
//							"No se le puede asigar el bus, porque el viaje coincide con otro que ya tiene el bus asignado");
//					return 6;
//				}
//
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//
//			// obtener id del ultimo viaje
//			Viaje viaje = new Viaje();
//			int ultimoId = viajeRepository.findUltimoId();
//			System.out.println("UltimoIdIngresado: " + ultimoId);
//
//			try {
//				Optional<Viaje> Oviaje = viajeRepository.findById(ultimoId);
//				viaje = Oviaje.get();
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			try {
//				if (asignarOmnibusAViaje(bus, viaje) == 1) {
//					;
//					return 3;
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			return 5;
//		}
//
//		System.out.println("Una de las ciudades ingresadas no existe");
//		return 4;
//	}

	public int crearViajeConBus(DtoViaje dtoViaje) {
		int resultado = 0;
		Viaje nuevoViaje = new Viaje();

		Optional<Localidad> locOri = localidadRepository.findById(dtoViaje.getIdLocalidadOrigen());
		Optional<Localidad> locDest = localidadRepository.findById(dtoViaje.getIdLocalidadDestino());

		if (locOri.isPresent() && locDest.isPresent()) {
			if (locOri.get().getId() == locDest.get().getId()) {
				System.out.println("***La ciudad de origen y destino no pueden ser las mismas");
				resultado = 3;
				return resultado;
			}

			if (!locOri.get().isActivo() || !locDest.get().isActivo()) {
				System.out.println("***Una de las ciudades no se encuentra disponible");
				resultado = 8;
				return resultado;
			}

			nuevoViaje.setFechaFin(dtoViaje.getFechaFin());
			nuevoViaje.setFechaInicio(dtoViaje.getFechaInicio());
			nuevoViaje.setHoraInicio(dtoViaje.getHoraInicio());
			nuevoViaje.setHoraFin(dtoViaje.getHoraFin());
			nuevoViaje.setLocalidadOrigen(locOri.get());
			nuevoViaje.setLocalidadDestino(locDest.get());
			nuevoViaje.setPrecio(dtoViaje.getPrecio());
			nuevoViaje.setEstadoViaje(EstadoViaje.NUEVO);
			nuevoViaje.setAsientosPorViaje(new ArrayList<>());
			viajeRepository.save(nuevoViaje);

			// buscar omnibus
			Omnibus bus = new Omnibus();
			try {
				Optional<Omnibus> Obus = omnibusRepository.findById(dtoViaje.getIdOmnibus());
				bus = Obus.get();
			} catch (Exception e) {
				// TODO: handle exception
			}

//			if (omnibusDisponible(bus.getId(), nuevoViaje.getFechaInicio(), nuevoViaje.getHoraInicio()) == false) {
//				System.out.println(
//						"***No se le puede asigar el bus, porque el viaje coincide con otro que ya tiene el bus asignado");
//				resultado = 6;
//				return resultado;
//			}

			// obtener id del ultimo viaje
			Viaje viaje = new Viaje();
			int ultimoId = viajeRepository.findUltimoId();
			System.out.println("UltimoIdIngresado: " + ultimoId);

			try {
				Optional<Viaje> Oviaje = viajeRepository.findById(ultimoId);
				viaje = Oviaje.get();
			} catch (Exception e) {
				// TODO: handle exception
			}

			resultado = asignarOmnibusAViaje(bus, viaje);
			System.out.println("***resultado: " + resultado);
			return resultado;
		}
		System.out.println("***Una de las ciudades ingresadas no existe");
		resultado = 9;
		return resultado;
	}

	public int asignarOmnibusAViaje_vieja(Omnibus omnibus, Viaje viaje) {
		int resultado = 0;
		Localidad localidadOrigen = viaje.getLocalidadOrigen();
		Optional<Localidad> loc = localidadRepository.findByNombre(localidadOrigen.getNombre());
		OmnibusAsientoViaje oav = new OmnibusAsientoViaje();

		try {
			Optional<Omnibus> busOpt = omnibusRepository.findById(omnibus.getId());

			if (busOpt.isPresent()) {
				Omnibus bus = busOpt.get();
				System.out.println(" Omnibus localidad: " + bus.getLocalidad());
				System.out.println(" Localidad localidad: " + loc.get().getNombre());

				if (bus.getLocalidad() != null && loc.isPresent() && bus.getLocalidad().equals(loc.get().getNombre())) {

					viaje.setOmnibus(bus);
					// Crear relación asiento/viaje
					for (OmnibusAsiento asiento : bus.getAsientos()) {
						AsientoPorViaje apv = new AsientoPorViaje();
						apv.setOmnibusAsiento(asiento);
						apv.setViaje(viaje);
						apv.setReservado(false); // al inicio, todos libres
						viaje.getAsientosPorViaje().add(apv);
					}

					viajeRepository.save(viaje);

					resultado = 1; // éxito
				} else {
					System.out.println(
							"No se puede asignar el viaje porque el bus no se encuentra en la localidad origen del viaje.");
					resultado = 4;
				}
			} else {
				System.out.println("No se encontró el ómnibus.");
				resultado = 2;
			}

		} // fin del try
		catch (Exception e) {
			e.printStackTrace();
			resultado = 3;
		}

		return resultado;
	}

	public int asignarOmnibusAViaje(Omnibus omnibus, Viaje viaje) {
		int resultado = 0;
		Localidad localidadOrigen = viaje.getLocalidadOrigen();
		Optional<Localidad> loc = localidadRepository.findByNombre(localidadOrigen.getNombre());
		int cantidaAsientosVendidos = cantidadAsientosVendidos((long) (viaje.getId()));
		System.out.println("asientosVendidos " + cantidaAsientosVendidos);
		int asientosDelBus = omnibus.getCant_asientos();
		System.out.println("asientosDelBus " + asientosDelBus);

		try {
			Optional<Omnibus> busOpt = omnibusRepository.findById(omnibus.getId());

			if (busOpt.isPresent()) {

				if (cantidaAsientosVendidos > asientosDelBus) {
					System.out
							.println("No se puede asigar el bus, porque no dispone de los asientos libres necesarios");
					resultado = 7;
					return resultado;
				}

				if (omnibusDisponible(busOpt.get().getId(), viaje.getFechaInicio(), viaje.getHoraInicio()) == false) {
					System.out.println(
							"No se le puede asigar el bus, porque el mismo ya esta asignado a un viaje en proceso");
					resultado = 6;
					return resultado;
				}

//				if(!busOpt.get().isSePuedeUtilizar()) {
//					
//				}

				if (!busOpt.get().isActivo()) {
					System.out.println("No se le puede asigar el bus, porque el mismo esta inactivo");
					resultado = 5;
					return resultado;
				}

				Omnibus bus = busOpt.get();
				System.out.println(" Omnibus localidad: " + bus.getLocalidad());
				System.out.println(" Localidad localidad: " + loc.get().getNombre());

				if (bus.getLocalidad() != null && loc.isPresent() && bus.getLocalidad().equals(loc.get().getNombre())) {

					// Asignar el ómnibus al viaje
					viaje.setOmnibus(bus);

					// Crear relación asiento/viaje (modelo AsientoPorViaje, si estás usando ambos
					// modelos)
					for (OmnibusAsiento asiento : bus.getAsientos()) {
						AsientoPorViaje apv = new AsientoPorViaje();
						apv.setOmnibusAsiento(asiento);
						apv.setViaje(viaje);
						apv.setReservado(false); // al inicio, todos libres
						viaje.getAsientosPorViaje().add(apv);
					}
					bus.setSePuedeUtilizar(false);
					viajeRepository.save(viaje);
					resultado = 1; // éxito

				} else {
					System.out.println(
							"No se puede asignar el viaje porque el bus no se encuentra en la localidad origen del viaje.");
					resultado = 4;
				}
			} else {
				System.out.println("No se encontró el ómnibus.");
				resultado = 2;
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultado = 3;
		}

		return resultado;
	}

	public List<DtoViajeDestinoFecha> obtenerViajesPorFechaYDestino(DtoViaje dtoVDF) {
		List<DtoViaje> lista = new ArrayList<>();
		List<DtoViajeDestinoFecha> listaDto = new ArrayList<>();
		System.out.println("Sdestino: " + dtoVDF.getIdLocalidadDestino());
		System.out.println("Sorigen: " + dtoVDF.getIdLocalidadOrigen());
		System.out.println("Sinicio: " + dtoVDF.getFechaInicio());// IdLocalidadOrigen());
		System.out.println("Sfin: " + dtoVDF.getFechaFin());// IdLocalidadOrigen());
		// lista =
		// viajeRepository.buscarViajesFiltrados(dtoVDF.getFechaInicio(),dtoVDF.getFechaFin(),dtoVDF.getIdLocalidadOrigen(),dtoVDF.getIdLocalidadDestino());
		lista = viajeRepository.buscarViajesFiltrados(dtoVDF.getFechaInicio(), dtoVDF.getFechaFin(),
				dtoVDF.getIdLocalidadOrigen(), dtoVDF.getIdLocalidadDestino());
		System.out.println("Cantidad de objetos: " + lista.size());

		for (DtoViaje dto : lista) {
			DtoViajeDestinoFecha vdf = new DtoViajeDestinoFecha();
			vdf.setBusId(dto.getIdOmnibus());
			vdf.setCantAsientosDisponibles(asientoPorViajeRepository.contarAsientosDisponiblesPorViaje(dto.getId()));
			System.out.println("Cantida de asientos" + vdf.getCantAsientosDisponibles());
			vdf.setHoraFin(dto.getHoraFin());
			vdf.setHoraInicio(dto.getHoraInicio());
			vdf.setPrecioPasaje(dto.getPrecio());
			vdf.setViajeId(dto.getId());
			vdf.setFechaInicio(dto.getFechaInicio());
			vdf.setFechaFin(dto.getFechaFin());
			listaDto.add(vdf);
		}
		return listaDto;
	}

	public List<Integer> asientosDisponibles(int idViaje) {
		List<AsientoPorViaje> resultado = new ArrayList<>();
		List<AsientoPorViaje> salida = new ArrayList<>();
		List<Integer> asientosDisponibles = new ArrayList<>();
		Viaje viaje = new Viaje();
		try {
			Optional<Viaje> Oviaje = viajeRepository.findById(idViaje);
			viaje = Oviaje.get();
			resultado = viaje.getAsientosPorViaje();

			for (AsientoPorViaje apv : resultado) {
				if (!apv.isReservado()) {
					salida.add(apv);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		for (AsientoPorViaje a : salida) {
			int nro = a.getOmnibusAsiento().getAsiento().getNro();
			System.out.println("asiento nro " + nro + " esta disponible");
			asientosDisponibles.add(nro);
		}
		return asientosDisponibles;
	}

	public boolean cancelarViaje(Long idViaje) {
		Viaje viaje = new Viaje();
		try {
			Optional<Viaje> Oviaje = viajeRepository.findById(idViaje.intValue());
			viaje = Oviaje.get();
			if (viaje.getEstadoViaje().equals(EstadoViaje.CANCELADO)) {
				return false;
			}
			viaje.setEstadoViaje(EstadoViaje.CANCELADO);

			try {
				viaje.getOmnibus().setSePuedeUtilizar(true);
				System.out.println("id del bus a habilitar: " + viaje.getOmnibus().getId());
			} catch (Exception e) {
				// TODO: handle exception
			}
			viajeRepository.save(viaje);
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<Long> compraPasajeACancelar = new ArrayList<>();
		try {
			List<CompraPasaje> compraPasaje = compraPasajeRepository.findByViajeId(idViaje);
			System.out.println("Cantidad de compras: " + compraPasaje.size());// Ok

			for (CompraPasaje cp : compraPasaje) {
				compraPasajeACancelar.add(cp.getId());
			}
			for (Long id : compraPasajeACancelar) {
				compraPasajeService.cancelarCompra(id.intValue()); // cancela la compra
				usuarioService.enviarMailCancelarCompra(id.intValue());
				System.out.println("Se canceló la compra nro " + id);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

	@Scheduled(fixedRate = 60000) // cada 60 segundos
	public void cerrarViajes() {
		List<Viaje> viajesACerrar = viajeRepository.findViajesConInicioEnLosProximos60Minutos();
		List<String> tokenAEnviar = new ArrayList<>();
		for (Viaje v : viajesACerrar) {
			if (!v.getEstadoViaje().equals(EstadoViaje.CERRADO)) {
				v.setEstadoViaje(EstadoViaje.CERRADO);

				try {
					Omnibus omnibus = v.getOmnibus();
					System.out.println("id del bus a habilitar al cerrar el viaje: " + omnibus.getId());
					omnibus.setSePuedeUtilizar(true);
					omnibus.setLocalidadActual(v.getLocalidadDestino().getNombre());
					omnibusRepository.save(omnibus);
				} catch (Exception e) {
					// TODO: handle exception
				}

				viajeRepository.save(v);

				// enviar mail a los compradores de pasajes para ese viaje
				List<CompraPasaje> comprapasaje = new ArrayList<>();
				comprapasaje = compraPasajeRepository.findByViajeId((long) v.getId());
				System.out.println("El viaje de id " + v.getId() + " ha sido cerrado");
				for (CompraPasaje cp : comprapasaje) {
					usuarioService.enviarMailAvisandoDeViaje(cp.getId().intValue());

					// enviar pushNotifications
					tokenAEnviar = tokenRepository.findTokensByUsuarioId(cp.getUsuario().getId());
					String titulo = "Recordatorio de viaje proximo";
					String mensaje = "Recuerde que usted tiene un viaje con destino a "
							+ cp.getViaje().getLocalidadDestino().getNombre() + " que sale en 1 hora aproximadamente";
					for (String token : tokenAEnviar) { // son los tokens del usuario al que se le va a enviar el mail
						// por cada usuario (dentro del for anterior, se ejecuta este for
						try {
							System.out.println("");
							System.out.println("el usuario a enviar es el del correo: " + cp.getUsuario().getEmail()
									+ " al dispositivo " + token);
							tokenService.enviarPushNotification(token, titulo, mensaje);
//							System.out.println("");
//							System.out.println("");
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			} else {
			}
		}
	}

	public List<DtoViaje> obtenerViajesPorBus(int idBus) {
		List<DtoViaje> dtoViajes = new ArrayList<>();
		List<Viaje> viajes = new ArrayList<>();
		try {
			viajes = viajeRepository.findByOmnibus_Id(idBus);
			for (Viaje v : viajes) {
				DtoViaje viaje = new DtoViaje();
				viaje.setFechaFin(v.getFechaFin());
				viaje.setFechaInicio(v.getFechaInicio());
				viaje.setHoraFin(v.getHoraFin());
				viaje.setHoraInicio(v.getHoraInicio());
				viaje.setId(v.getId());
				viaje.setIdLocalidadDestino(v.getLocalidadDestino().getId());
				viaje.setIdLocalidadOrigen(v.getLocalidadOrigen().getId());
				viaje.setIdOmnibus(v.getOmnibus().getId());
				viaje.setPrecio(v.getPrecio());
				dtoViajes.add(viaje);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dtoViajes;
	}

	public int cantidadDeViajesCreados() {
		return viajeRepository.findAll().size();
	}

	public int calificarViaje(DtoCalificarViaje dtoCalificar) {
		int idViaje = dtoCalificar.getIdViaje();
		int calificacion = dtoCalificar.getCalificacion();
		String comentario = dtoCalificar.getComentario();

		System.out.println("comentario: " + comentario);
		System.out.println("viaje: " + idViaje);
		if (calificacion == 0 || calificacion > 5) {
			System.out.println("La calificación debe de estar entre 1 y 5");
			return 2;
		}
		try {
			Optional<Viaje> Oviaje = viajeRepository.findById(idViaje);
			if (Oviaje.isPresent()) {
				Viaje viaje = Oviaje.get();

				List<String> comentarios = viaje.getComentarios();
				if (comentarios == null) {
					comentarios = new ArrayList<>();
				}

				System.out.println("comentarios Actuales = " + comentarios.size());
				if (!comentario.equals("")) {
					comentarios.add(comentario);
					viaje.setComentarios(comentarios);
				}
				System.out.println("Entre para obtener los comentarios y agregar los nuevos: Nuevos:"
						+ viaje.getComentarios().size());
				System.out.println("calificacion Actual: " + viaje.getCalificacion());
				viaje.setCalificacion(viaje.getCalificacion() + calificacion);
				System.out.println("calificacion Nueva: " + viaje.getCalificacion());

				viajeRepository.save(viaje); // ✅ PERSISTE LOS CAMBIOS

				return 1;
			} else {
				System.out.println("No se encontró el viaje con id: " + idViaje);
			}
		} catch (Exception e) {
			e.printStackTrace(); // Para que puedas ver el error si ocurre algo
		}

		return 0;
	}

	public DtoCalificacion verCalificacionYComentariosDeViaje(int idViaje) {
		System.out.println("entre al service de verCalificacionComentario");
		DtoCalificacion resultado = new DtoCalificacion();
		List<String> comentarios = new ArrayList<>();

		try {
			Optional<Viaje> Oviaje = viajeRepository.findById(idViaje);
//			System.out.println("encontre el viaje");

			int cantidadComentarios = Oviaje.get().getComentarios().size();
			resultado.setCalificacion(Oviaje.get().getCalificacion() / cantidadComentarios);

			for (String c : Oviaje.get().getComentarios()) {
				if (!c.equals("")) {
					comentarios.add(c);
				}
			}
			resultado.setComentarios(comentarios);// Oviaje.get().getComentarios());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resultado;
	}

//	public List<DtoViajeCompleto> obtenerViajes() {
//		List<DtoViajeCompleto> viajes = new ArrayList<>();
//		List<Viaje> total = viajeRepository.findAll();
//		int asientosOcupados = 0;
//		int asientosLibres = 0;
//		int totalAsientos = 0;
//		for (Viaje v : total) {
////			String localidadOrigen = null;
////			String localidadDestino = null;
//			try {
//				DtoViajeCompleto nuevo = new DtoViajeCompleto();
//				nuevo.setFechaInicio(v.getFechaInicio());
//				nuevo.setFechaFin(v.getFechaFin());
//				nuevo.setHoraFin(v.getHoraFin());
//				nuevo.setHoraInicio(v.getHoraInicio());
//				nuevo.setId(v.getId());
//				System.out.println("id de bus en obtenerViajes = " + v.getOmnibus().getId());
////				if(v.getOmnibus().getId()==null) {
////					nuevo.setIdOmnibus(0);
////				}
//				nuevo.setIdOmnibus(v.getOmnibus().getId());
//				nuevo.setPrecio(v.getPrecio());
//				nuevo.setEstadoViaje(v.getEstadoViaje());
//				nuevo.setCalificacion(v.getCalificacion());
//				Optional<Localidad> OlocalidadO = localidadRepository.findById(v.getLocalidadOrigen().getId());
//				Optional<Localidad> OlocalidadD = localidadRepository.findById(v.getLocalidadDestino().getId());
//				nuevo.setIdLocalidadDestino(OlocalidadD.get().getNombre());
//				nuevo.setIdLocalidadOrigen(OlocalidadO.get().getNombre());
//				asientosLibres = asientosDisponibles(v.getId()).size();
//				totalAsientos = v.getOmnibus().getCant_asientos();
//				asientosOcupados = totalAsientos - asientosLibres;
//				nuevo.setAsientosOcupados(asientosOcupados);
//				viajes.add(nuevo);
//			} catch (Exception e) {
//				 System.err.println("Error procesando viaje con ID: " + v.getId());
//				    e.printStackTrace(); // o log.error(...) si estás usando logging
//			}
//
//		}
//		System.out.println("Cantidad de viajes a mostrar: "+viajes.size());
//		return viajes;
//	}

	public List<DtoViajeCompleto> obtenerViajes() {
		List<DtoViajeCompleto> viajes = new ArrayList<>();
		List<Viaje> total = viajeRepository.findAll();
		int errores = 0;

		System.out.println("Cantidad de viajes en DB: " + total.size());

		for (Viaje v : total) {
			System.out.println("Procesando viaje ID: " + v.getId());
			try {
				DtoViajeCompleto nuevo = new DtoViajeCompleto();

				// Datos básicos del viaje
				nuevo.setId(v.getId());
				nuevo.setFechaInicio(v.getFechaInicio());
				nuevo.setFechaFin(v.getFechaFin());
				nuevo.setHoraInicio(v.getHoraInicio());
				nuevo.setHoraFin(v.getHoraFin());
				nuevo.setPrecio(v.getPrecio());
				nuevo.setEstadoViaje(v.getEstadoViaje());
				nuevo.setCalificacion(v.getCalificacion());

				// Omnibus
				if (v.getOmnibus() != null) {
					nuevo.setIdOmnibus(v.getOmnibus().getId());

					Integer cantAsientos = v.getOmnibus().getCant_asientos();
					int totalAsientos = (cantAsientos != null) ? cantAsientos : 0;
					int asientosLibres = asientosDisponibles(v.getId()).size();
					int asientosOcupados = totalAsientos - asientosLibres;

					nuevo.setAsientosOcupados(asientosOcupados);
				} else {
					System.out.println("Error en omnibus del viaje ID: " + v.getId());
					nuevo.setIdOmnibus(null); // Solo si el DTO usa Integer, no int
					nuevo.setAsientosOcupados(0); // o -1 si querés indicar "no definido"
				}

				// Localidad Origen
				if (v.getLocalidadOrigen() != null) {
					localidadRepository.findById(v.getLocalidadOrigen().getId()).ifPresentOrElse(
							lo -> nuevo.setIdLocalidadOrigen(lo.getNombre()),
							() -> nuevo.setIdLocalidadOrigen("Desconocida"));
				} else {
					nuevo.setIdLocalidadOrigen("No asignada");
				}

				// Localidad Destino
				if (v.getLocalidadDestino() != null) {
					localidadRepository.findById(v.getLocalidadDestino().getId()).ifPresentOrElse(
							ld -> nuevo.setIdLocalidadDestino(ld.getNombre()),
							() -> nuevo.setIdLocalidadDestino("Desconocida"));
				} else {
					nuevo.setIdLocalidadDestino("No asignada");
				}

				// Agregar a la lista
				viajes.add(nuevo);

			} catch (Exception e) {
				errores++;
				System.err.println("Error procesando viaje con ID: " + v.getId());
				e.printStackTrace();
			}
		}

		System.out.println("Cantidad de errores: " + errores);
		System.out.println("Cantidad de viajes a mostrar: " + viajes.size());
		return viajes;
	}

	public DtoCompraViaje obtenerCompraViaje(int idViaje, int idCompra, int idUsuario) {
		// List<DtoCompraViaje> resultado = new ArrayList<>();
		System.out.println("idCompra = " + idCompra);
		System.out.println("idViaje = " + idViaje);
		System.out.println("idUsuario = " + idUsuario);
		DtoCompraViaje cv = new DtoCompraViaje();
//		// datos del viaje
		String localidadDestinoDepartamento = null;
		String localidadDestinoLocalidad = null;
		String localidadOrigenDepaartamento = null;
		String localidadOrigenLocalidad = null;
		LocalTime horaInicio = null;
		LocalTime horaFin = null;
		Date fechaInicio = null;
		Date fechaFin = null;
		int idOmnibus = 0;
//
//		// datos de la compra
//		int cantidadAsientos;
//		float precio;
//		int descuento;
//		
		try {
			Optional<Viaje> Oviaje = viajeRepository.findById(idViaje);
			localidadDestinoDepartamento = Oviaje.get().getLocalidadDestino().getDepartamento();
			localidadDestinoLocalidad = Oviaje.get().getLocalidadDestino().getNombre();
			localidadOrigenDepaartamento = Oviaje.get().getLocalidadOrigen().getDepartamento();
			localidadOrigenLocalidad = Oviaje.get().getLocalidadOrigen().getNombre();
			horaInicio = Oviaje.get().getHoraInicio();
			horaFin = Oviaje.get().getHoraFin();
			fechaInicio = Oviaje.get().getFechaInicio();
			fechaFin = Oviaje.get().getFechaFin();
			idOmnibus = Oviaje.get().getOmnibus().getId();
		} catch (Exception e) {
			// TODO: handle exception
		}
		cv.setFechaFin(fechaFin);
		cv.setFechaInicio(fechaInicio);
		cv.setLocalidadDestinoLocalidad(localidadDestinoLocalidad);
		cv.setLocalidadDestinoNombre(localidadDestinoDepartamento);
		cv.setLocalidadOrigenLocalidad(localidadOrigenLocalidad);
		cv.setLocalidadOrigenNombre(localidadOrigenDepaartamento);
		cv.setHoraInicio(horaInicio);
		cv.setHoraFin(horaFin);
		cv.setFechaFin(fechaFin);
		cv.setFechaInicio(fechaInicio);
		cv.setIdOmnibus(idOmnibus);
		try {
			List<CompraPasaje> listadoCompras = compraPasajeRepository.findByViajeId((long) idViaje);

			for (CompraPasaje cp : listadoCompras) {
//				System.out.println("idCompra Listado = " + cp.getId());
//				System.out.println("idCompra = " + idCompra);
//				System.out.println("");
				if (cp.getId() == idCompra) {
					System.out.println("encontre la compra");
					cv.setCantidadAsientos(cp.getCat_pasajes());
					cv.setPrecio(cp.getTotal());
					cv.setDescuento(cp.getDescuentoAplicado());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return cv;
	}

	public DtoViajeCompleto obtenerViajeId(int idViaje) {
		DtoViajeCompleto resultado = new DtoViajeCompleto();

		try {
			Optional<Viaje> Oviaje = viajeRepository.findById(idViaje);
			Viaje viaje = Oviaje.get();
			resultado.setFechaFin(viaje.getFechaFin());
			resultado.setFechaInicio(viaje.getFechaInicio());
			resultado.setHoraFin(viaje.getHoraFin());
			resultado.setHoraInicio(viaje.getHoraInicio());
			resultado.setId(viaje.getId());
			resultado.setIdLocalidadDestino(viaje.getLocalidadDestino().getNombre());
			resultado.setIdLocalidadOrigen(viaje.getLocalidadOrigen().getNombre());
			resultado.setPrecio(viaje.getPrecio());
			resultado.setIdOmnibus(viaje.getOmnibus().getId());
			resultado.setCalificacion(viaje.getCalificacion());
			int asientosLibres = asientosDisponibles(viaje.getId()).size();
			int totalAsientos = viaje.getOmnibus().getCant_asientos();
			resultado.setAsientosOcupados(totalAsientos - asientosLibres);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return resultado;
	}

	public List<DtoViajeCompleto> obtenerViajePorDestino(String destino) {
		List<DtoViajeCompleto> resultado = new ArrayList<>();
		List<Viaje> listadoViaje = viajeRepository.findAll();
		int idViaje = 0;

		try {
			for (Viaje v : listadoViaje) {
				DtoViajeCompleto viaje = new DtoViajeCompleto();

				if (v.getLocalidadDestino().getNombre().equals(destino)) {
					idViaje = v.getId();
					viaje = obtenerViajeId(idViaje);
					resultado.add(viaje);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resultado;
	}

	public List<DtoViajeCompleto> obtenerViajePorOrigen(String origen) {
		List<DtoViajeCompleto> resultado = new ArrayList<>();
		List<Viaje> listadoViaje = viajeRepository.findAll();
		int idViaje = 0;

		try {
			for (Viaje v : listadoViaje) {
				DtoViajeCompleto viaje = new DtoViajeCompleto();
				if (v.getLocalidadOrigen().getNombre().equals(origen)) {
					idViaje = v.getId();
					viaje = obtenerViajeId(idViaje);
					resultado.add(viaje);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resultado;
	}

	public List<DtoViajeCompleto> obtenerViajeMejorCalificados() {
		List<Integer> listadoObtenido = new ArrayList<>();
		List<DtoViajeCompleto> resultado = new ArrayList<>();
		try {

			listadoObtenido = viajeRepository.findTop5IdsByCalificacion();

			for (Integer i : listadoObtenido) {
				DtoViajeCompleto viaje = new DtoViajeCompleto();
				viaje = obtenerViajeId(i);
				if (viaje.getCalificacion() > 0) {
					resultado.add(viaje);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return resultado;
	}

	public boolean omnibusDisponible(int idBus, Date fechaInicio, LocalTime horaInicio) {
		System.out.println("Entre a omnibusDisponible");
		List<Viaje> viajes = new ArrayList<>();
		int viajesEncontrados = 0;

		try {
			viajes = viajeRepository.findByOmnibusId(idBus);
			System.out.println("Cantidad de viajes = " + viajes.size());
			for (Viaje v : viajes) {
				System.out.println("viaje id: " + v.getId());
				// Suponiendo que v.getFechaInicio() y v.getFechaFin() son también de tipo
				// java.util.Date
				if (fechaInicio.after(v.getFechaInicio()) && fechaInicio.before(v.getFechaFin())) {
					viajesEncontrados++;
					System.out.println("No se puede asignar ese bus porque la fecha de inicio coincide con el "
							+ "periodo de viaje al cual está asignado ese bus");
				}

				// Suponiendo que v.getHoraFin() es de tipo LocalTime
				if (fechaInicio.equals(v.getFechaFin()) && horaInicio.isBefore(v.getHoraFin())) {
					viajesEncontrados++;
					System.out.println("No se puede asignar ese bus porque la hora de inicio del viaje"
							+ " esta antes de la hora de fin de un viaje existente para este bus");
				}

				if (fechaInicio.equals(v.getFechaInicio()) && horaInicio.equals(v.getHoraInicio())) {
					viajesEncontrados++;
					System.out.println("No se puede asignar ese bus porque la fecha y hora de incio del viaje"
							+ " coincide exactamente con un viaje creado para ese bus");
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // Para ver qué sucede si hay un error
		}

		if (viajesEncontrados == 0) {
			System.out.println("Viajes encontrados: " + viajesEncontrados);
			return true;
		} else {
			return false;
		}
	}

	public int cantidadAsientosVendidos(Long viajeId) {
		int cantidadAsientos = 0;
		List<CompraPasaje> pasajes = compraPasajeRepository.findByViajeId(viajeId);

		for (CompraPasaje cp : pasajes) {
			if (!cp.getEstadoCompra().equals(EstadoCompra.CANCELADA)) {
				System.out.println("Entre al if de cantidadAsientosVendidos");
				int asientos = cp.getCat_pasajes();
				cantidadAsientos = cantidadAsientos + asientos;
				System.out.println("cantidad de asientos vendidos: " + cantidadAsientos);
			}
		}
		System.out.println("cantidad de asientos vendidos: " + cantidadAsientos);
		return cantidadAsientos;
	}

	public List<DtoViajeIdDestino> obtenerViajesIdDestino() {
		List<DtoViajeIdDestino> viajes = new ArrayList<>();
		List<Viaje> total = viajeRepository.findAll();

		for (Viaje v : total) {

			try {
				DtoViajeIdDestino nuevo = new DtoViajeIdDestino();
				nuevo.setId(v.getId());
				nuevo.setOrigenDestino(v.getLocalidadOrigen().getNombre() + "-" + v.getLocalidadDestino().getNombre()
						+ " " + v.getHoraInicio() + "-" + v.getHoraFin());
				viajes.add(nuevo);
			} catch (Exception e) {
			}

		}
		return viajes;
	}

	public String crearViajesMasivos(MultipartFile file) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		String line;
		List<Map<String, String>> dataList = new ArrayList<>();
		int fila = 1;

		try {
			while ((line = reader.readLine()) != null) {
				Map<String, String> row = new HashMap<>();
				DtoViaje viaje = new DtoViaje();
				String[] values = line.split("/");

				// Validamos que haya exactamente 7 valores por línea
				if (values.length != 7) {
					throw new Exception("Formato incorrecto en el CSV. Cada fila debe tener 7 valores.");
				}

				// Construimos el JSON correctamente
				row.put("precio", values[0]);
				row.put("fechaInicio", values[1]);
				row.put("fechaFin", values[2]);
				row.put("horaInicio", values[3]);
				row.put("horaFin", values[4]);
				row.put("idLocalidadOrigen", values[5]);
				row.put("idLocalidadDestiono", values[6]);

				viaje.setPrecio(Float.parseFloat(values[0]));
				// viaje.setPrecio(values[0]);
				// Parsea a java.util.Date
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date fechaInicioUtil = sdf.parse(values[1]);
				java.util.Date fechaFinUtil = sdf.parse(values[2]);
				// Convierte a java.sql.Date
				java.sql.Date fechaInicio = new java.sql.Date(fechaInicioUtil.getTime());
				java.sql.Date fechaFin = new java.sql.Date(fechaFinUtil.getTime());
				viaje.setFechaInicio(fechaInicio);
				viaje.setFechaFin(fechaFin);

				DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				LocalTime horaInicio = LocalTime.parse(values[3], timeFormatter);
				LocalTime horaFin = LocalTime.parse(values[4], timeFormatter);
				viaje.setHoraInicio(horaInicio);
				viaje.setHoraFin(horaFin);

				Optional<Localidad> localidadOrigenOpt = localidadRepository.findByNombre(values[5]);
				Optional<Localidad> localidadDestinoOpt = localidadRepository.findByNombre(values[6]);

//				if (!localidadOrigenOpt.isPresent() || !localidadDestinoOpt.isPresent()) {
//					Map<String, String> response = new HashMap<>();
//
//					String origen = localidadOrigenOpt.isPresent() ? localidadOrigenOpt.get().getNombre() : values[5];
//					String destino = localidadDestinoOpt.isPresent() ? localidadDestinoOpt.get().getNombre()
//							: values[6];
//					String localidad = "";
//					if(!localidadOrigenOpt.isPresent()) {
//						localidad = localidadOrigenOpt.get().getNombre();
//						response.put("error", "Linea " + fila + ": El viaje de " + origen + " a " + destino
//								+ " no se pudo crear (la localidad " + localidad + " no se encuentra registrada)");
//						dataList.add(response);
//						fila++;
//						continue;
//					}
//					if(!localidadDestinoOpt.isPresent()) {
//						localidad = localidadDestinoOpt.get().getNombre();
//						response.put("error", "Linea " + fila + ": El viaje de " + origen + " a " + destino
//								+ " no se pudo crear (la localidad " + localidad + " no se encuentra registrada)");
//						dataList.add(response);
//						fila++;
//						continue;
//					}
//
//				}
				
				
				if (!localidadOrigenOpt.isPresent() || !localidadDestinoOpt.isPresent()) {
				    Map<String, String> response = new HashMap<>();

				    String origen = values[5];
				    String destino = values[6];

				    if (!localidadOrigenOpt.isPresent()) {
				        response.put("error", "Linea " + fila + ": El viaje de " + origen + " a " + destino
				                + " no se pudo crear (la localidad " + origen + " no se encuentra registrada)");
				        dataList.add(response);
				        fila++;
				        continue;
				    }

				    if (!localidadDestinoOpt.isPresent()) {
				        response.put("error", "Linea " + fila + ": El viaje de " + origen + " a " + destino
				                + " no se pudo crear (la localidad " + destino + " no se encuentra registrada)");
				        dataList.add(response);
				        fila++;
				        continue;
				    }
				}

				int idOrigen = localidadOrigenOpt.get().getId();
				int idDestino = localidadDestinoOpt.get().getId();

				viaje.setIdLocalidadOrigen(idOrigen);
				viaje.setIdLocalidadDestino(idDestino);
				System.out.println("fila antes de guardar el viaje: " + fila);
				if (crearViaje(viaje) != 3) {
					Map<String, String> response = new HashMap<>();
					System.out.println("fila dentro del if de guardar el viaje: " + fila);
					response.put("error", "Linea " + fila + ":" + "El viaje de " + localidadOrigenOpt.get().getNombre()
							+ " a " + localidadDestinoOpt.get().getNombre() + " no se pudo crear, una de las ciudades" + 
							" se encuentra inactiva");
					dataList.add(response);
				}
				System.out.println("fila despues de guardar el viaje: " + fila);
				fila++;

			}

			// Convertir la lista a formato JSON
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(dataList);

		} catch (Exception e) {
			throw new Exception("Error procesando el archivo CSV: " + e.getMessage());
		} finally {
			reader.close(); // Cerramos el BufferedReader
		}
	}

}
