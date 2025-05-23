package com.example.Login.service;

import com.example.Login.repository.AsientoPorViajeRepository;
import com.example.Login.repository.CompraPasajeRepository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

import com.example.Login.repository.ViajeRepository;

import io.jsonwebtoken.lang.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.DtoViajeDestinoFecha;
import com.example.Login.dto.EstadoCompra;
import com.example.Login.dto.EstadoViaje;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Localidad;
import com.example.Login.model.Omnibus;
import com.example.Login.model.OmnibusAsiento;
import com.example.Login.model.OmnibusAsientoViaje;
import com.example.Login.model.Token;
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

	ViajeService(UsuarioService usuarioService) {
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

		try {
			Optional<Omnibus> busOpt = omnibusRepository.findById(omnibus.getId());

			if (busOpt.isPresent()) {
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
		//System.out.println("Cantidad de viajes a cerrar: " + viajesACerrar.size());
		List<String> tokenAEnviar = new ArrayList<>();
		for (Viaje v : viajesACerrar) {
			if (!v.getEstadoViaje().equals(EstadoViaje.CERRADO)) {
				v.setEstadoViaje(EstadoViaje.CERRADO);
				viajeRepository.save(v);

				// enviar mail a los compradores de pasajes para ese viaje
				List<CompraPasaje> comprapasaje = new ArrayList<>();
				comprapasaje = compraPasajeRepository.findByViajeId((long) v.getId());
//				System.out.println("cantidad de compras para ese viaje: " + comprapasaje.size());
//				System.out.println("");
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
							System.out.println("el usuario a enviar es el del correo: " + cp.getUsuario().getEmail() +
									" al dispositivo " + token);
							tokenService.enviarPushNotification(token, titulo, mensaje);
//							System.out.println("");
//							System.out.println("");
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					// fin de enviar pushNotification

				}
			} else {
				//System.out.println("El viaje no se puede cerrar porque ya esta cerrado");
			}
		}
	}
//	@Query("SELECT v FROM Viaje v " +
//		       "WHERE FUNCTION('TIMESTAMP', v.fechaInicio, v.horaInicio) BETWEEN CURRENT_TIMESTAMP AND FUNCTION('TIMESTAMPADD', 'MINUTE', 60, CURRENT_TIMESTAMP)")
//		List<Viaje> findViajesConInicioEnLosProximos60Minutos();

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

}
