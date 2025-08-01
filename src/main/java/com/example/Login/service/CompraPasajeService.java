package com.example.Login.service;

import java.io.IOException;
import java.net.Authenticator.RequestorType;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import com.example.Login.controller.DtoCompraPasajeNombre;
import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.DtoComprasPorTipo;
import com.example.Login.dto.DtoComprasUsuarios;
import com.example.Login.dto.DtoRespuestaCompraPasaje;
import com.example.Login.dto.DtoRespuestaVentaPasaje;
import com.example.Login.dto.DtoTipoDeCompra;
import com.example.Login.dto.DtoTotalPorMes;
import com.example.Login.dto.DtoVenderPasaje;
import com.example.Login.dto.EstadoCompra;
import com.example.Login.dto.EstadoViaje;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.Categoria;
import com.example.Login.model.CerrarViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Usuario;
import com.example.Login.model.Viaje;
import com.example.Login.repository.AsientoPorViajeRepository;
import com.example.Login.repository.CategoriaRepository;
import com.example.Login.repository.CerrarViajeRepository;
import com.example.Login.repository.CompraPasajeRepository;
import com.example.Login.repository.UsuarioRepository;
import com.example.Login.repository.ViajeRepository;
//import com.sun.org.apache.bcel.internal.generic.RETURN;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CompraPasajeService {

	private SecurityFilterChain securityFilterChain;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CerrarViajeRepository cerrarViajeRepository;

	@Autowired
	private AsientoPorViajeRepository asientoPorViajeRepository;

	@Autowired
	private CompraPasajeRepository compraPasajeRepository;

	@Autowired
	private TransactionalService transactionalService;

	@Autowired
	private ViajeRepository viajeRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private TokenService tokenService;

//    CompraPasajeService(SecurityFilterChain securityFilterChain) {
//        this.securityFilterChain = securityFilterChain;
//    }

	public CompraPasajeService(CompraPasajeRepository compraPasajeRepository, SecurityFilterChain securityFilterChain) {
		this.compraPasajeRepository = compraPasajeRepository;
		this.securityFilterChain = securityFilterChain;
	}

	public DtoRespuestaCompraPasaje comprarPasaje(DtoCompraPasaje request) {
		Usuario vendedor = new Usuario();
		Usuario usuario = new Usuario();
		Viaje viaje = new Viaje();

		int descuento = 0;

		List<Integer> asientosOcupado = new ArrayList<>();
		List<Integer> asientosInexistentes = new ArrayList<>();
		EstadoCompra estado = request.getEstadoCompra(); // Ya es un enum
		DtoRespuestaCompraPasaje asientosOcupados = new DtoRespuestaCompraPasaje();
		asientosOcupados.setEstado(estado);

		try {
			Optional<Usuario> Ousuario = usuarioRepository.findById(request.getUsuarioId());
			usuario = Ousuario.get();
			try {
				Optional<Categoria> Ocategoria = categoriaRepository
						.findBynombreCategoria(usuario.getCategoria().name());
				System.out.println("Encontre la categoria: " + Ocategoria.get().getNombreCategoria());
				descuento = Ocategoria.get().getDescuento();
				asientosOcupados.setDescuento(descuento);
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (usuario.getRol() != 100) {
				return asientosOcupados;
			}

			if (usuario.getActivo() == false) {
				return asientosOcupados;
			}
		} catch (Exception e) {
			return asientosOcupados;
		} //
		try {
			Optional<Usuario> Ovendedor = usuarioRepository.findById(request.getVendedorId());
			vendedor = Ovendedor.get();
		} catch (Exception e) {
			vendedor = null;
		}
		try {
			Optional<Viaje> Oviaje = viajeRepository.findById(request.getViajeId());
			viaje = Oviaje.get();
		} catch (Exception e) {
			return asientosOcupados;
		}

		CompraPasaje compra = new CompraPasaje();
		if (vendedor == null) {
			compra.setTipo_venta("Online");
		} else {
			compra.setTipo_venta("Ventanilla");
		}
		compra.setUsuario(usuario);
		compra.setVendedor(vendedor);
		compra.setDescuentoAplicado(descuento);
		System.out.println("Descuento aplicado = " + descuento);

		EstadoViaje ev = viaje.getEstadoViaje();
		switch (ev) {
		case CANCELADO:
			System.out.println("No se puede comprar pasaje para un viaje cancelado");
			asientosOcupados.setEstado(EstadoCompra.CANCELADA);
			return asientosOcupados;
		case CERRADO:
			System.out.println("No se puede comprar pasaje para un viaje cancelado");
			asientosOcupados.setEstado(EstadoCompra.CANCELADA);
			return asientosOcupados;
		default:
			compra.setViaje(viaje);
		}

		compra.setFechaHoraCompra(LocalDateTime.now());

		List<AsientoPorViaje> asientosReservados = new ArrayList<>();

		for (Integer nroAsiento : request.getNumerosDeAsiento()) {
			Optional<AsientoPorViaje> apvOpt = asientoPorViajeRepository.findByViajeIdAndNroAsiento(viaje.getId(),
					nroAsiento);
			if (apvOpt.isPresent()) {
				AsientoPorViaje apv = apvOpt.get();
				if (!apv.isReservado()) {
					apv.setReservado(true);
					asientosReservados.add(apv);
				} else {
					asientosOcupado.add(nroAsiento);
					// return 2;
				}
			} else {
				System.out.println("No existe el asiento a comprar nro " + nroAsiento);
				asientosInexistentes.add(nroAsiento);
			}
		}

		if (!asientosOcupado.isEmpty()) {
			System.out.println("asientos ocupados: " + asientosOcupado);
			asientosOcupados.setAsientosOcupados(asientosOcupado);
			return asientosOcupados;
		}
		if (!asientosInexistentes.isEmpty()) {
			System.out.println("asientos inexistentes: " + asientosOcupado);
			asientosOcupados.setAsientosInexistentes(asientosInexistentes);
			return asientosOcupados;
		}

		switch (estado) {
		case REALIZADA:
			compra.setEstadoCompra(EstadoCompra.REALIZADA);
			break;
		case RESERVADA:
			compra.setEstadoCompra(EstadoCompra.RESERVADA);
			break;
		default:
			System.out.println("Estado desconocido: " + estado);
		}
		compra.setCat_pasajes(asientosReservados.size());

		System.out.println("Asientos a comprar: " + asientosReservados.size());
		if (asientosReservados.size() > 5) {
			asientosOcupados.setAsientosComprados(asientosReservados.size());
			return asientosOcupados;
		}
		asientosOcupados.setAsientosComprados(asientosReservados.size());
		float total = (compra.getCat_pasajes() * viaje.getPrecio())
				- (compra.getCat_pasajes() * viaje.getPrecio() * descuento / 100);
		// compra.setTotal(compra.getCat_pasajes() * viaje.getPrecio());
		compra.setTotal(total);
		compra.setAsientos(asientosReservados);

		// obtengo la última compra
		Optional<CompraPasaje> ultimoId = compraPasajeRepository.findTopByOrderByIdDesc();
		CompraPasaje aux = ultimoId.get();
		// fin

		compraPasajeRepository.save(compra);

		// cargo el siguiente id de compra para mostrar
		// asientosOcupados.
		asientosOcupados.setIdCompra((int) (aux.getId() + 1));
		// fin

		for (AsientoPorViaje apv : asientosReservados) {
			asientoPorViajeRepository.save(apv);
		}
		return asientosOcupados;
	}

	public DtoRespuestaVentaPasaje venderPasaje(DtoVenderPasaje request) {
		DtoRespuestaVentaPasaje asientosOcupados = new DtoRespuestaVentaPasaje();
		Usuario vendedor = new Usuario();
		Usuario usuario = new Usuario();
		Viaje viaje = new Viaje();
		int descuento = 0;
		try {
			Optional<Usuario> Ousuario = usuarioRepository.findByEmail(request.getEmailCliente());// Class(). getClass()
																									// getClass()
																									// UsuarioId());
			usuario = Ousuario.get();
		} catch (Exception e) {
			usuario = null;
		}
		try {
			Optional<Usuario> Ovendedor = usuarioRepository.findById(request.getVendedorId());
			vendedor = Ovendedor.get();
			if (vendedor.getRol() != 200) {
				asientosOcupados.setCodigoSalida(8);
				return asientosOcupados;
			}
			if (!vendedor.getActivo()) {
				asientosOcupados.setCodigoSalida(4);
				return asientosOcupados;
			}
		} catch (Exception e) {
			vendedor = null;
			asientosOcupados.setCodigoSalida(1);
			return asientosOcupados;
		}
		try {
			Optional<Viaje> Oviaje = viajeRepository.findById(request.getViajeId());
			viaje = Oviaje.get();
		} catch (Exception e) {
			asientosOcupados.setCodigoSalida(5);
			return asientosOcupados;
		}
		try {
			Optional<Categoria> Ocategoria = categoriaRepository.findBynombreCategoria(usuario.getCategoria().name());
			System.out.println("Encontre la categoria: " + Ocategoria.get().getNombreCategoria());
			descuento = Ocategoria.get().getDescuento();
			asientosOcupados.setDescuento(descuento);
		} catch (Exception e) {
			// TODO: handle exception
		}

		CompraPasaje venta = new CompraPasaje();
		if (vendedor == null) {
			venta.setTipo_venta("Online");
		} else {
			venta.setTipo_venta("Ventanilla");
		}
		venta.setUsuario(usuario);
		venta.setVendedor(vendedor);
		venta.setViaje(viaje);
		venta.setFechaHoraCompra(LocalDateTime.now());

		List<AsientoPorViaje> asientosReservados = new ArrayList<>();

		for (Integer nroAsiento : request.getNumerosDeAsiento()) {
			Optional<AsientoPorViaje> apvOpt = asientoPorViajeRepository.findByViajeIdAndNroAsiento(viaje.getId(),
					nroAsiento);
			if (apvOpt.isPresent()) {
				AsientoPorViaje apv = apvOpt.get();
				if (!apv.isReservado()) {
					apv.setReservado(true);
					asientosReservados.add(apv);
				} else {
					asientosOcupados.setCodigoSalida(2);
					return asientosOcupados;
				}
			}
		}
		EstadoCompra estado = request.getEstadoCompra();
		System.out.println("Estado compre del request: " + estado);
		// EstadoCompra estado = EstadoCompra.REALIZADA;
		venta.setEstadoCompra(estado);
		venta.setCat_pasajes(asientosReservados.size());
		float total = (venta.getCat_pasajes() * viaje.getPrecio())
				- (venta.getCat_pasajes() * viaje.getPrecio() * descuento / 100);
		venta.setTotal(total);
		// venta.setTotal(venta.getCat_pasajes() * viaje.getPrecio());
		venta.setAsientos(asientosReservados);

		// obtengo la última compra
		Optional<CompraPasaje> ultimoId = compraPasajeRepository.findTopByOrderByIdDesc();
		CompraPasaje aux = ultimoId.get();
		// fin

		// cargo el siguiente id de compra para mostrar
		// asientosOcupados.
		asientosOcupados.setIdCompra((int) (aux.getId() + 1));
		// fin

		venta.setDescuentoAplicado(descuento);
		System.out.println("Descuento aplicado = " + descuento);
		compraPasajeRepository.save(venta);

		for (AsientoPorViaje apv : asientosReservados) {
			asientoPorViajeRepository.save(apv);
		}
		switch (estado) {
		case REALIZADA:
			asientosOcupados.setCodigoSalida(3);
			return asientosOcupados;
		case RESERVADA:
			asientosOcupados.setCodigoSalida(6);
			return asientosOcupados;
		default:
			System.out.println("Estado desconocido: " + estado);
		}
		asientosOcupados.setCodigoSalida(7);
		return asientosOcupados;
	}



	public int cancelarCompra(int idCompra) {
		Viaje viaje = new Viaje();
		CerrarViaje cerrarViaje = cerrarViajeRepository.getById(1);
		String tiempo = cerrarViaje.getTiempo();
		int minutos = Integer.parseInt(tiempo);
		System.out.println("Tiempo para el cierre de ventas de pasajes: " + tiempo);

		try {
			Optional<CompraPasaje> Ocompra = compraPasajeRepository.findById(idCompra);
			CompraPasaje compra = Ocompra.get();

			try {
				int idViaje = compra.getViaje().getId();
				Optional<Viaje> Oviaje = viajeRepository.findById(idViaje);
				viaje = Oviaje.get();
			} catch (Exception e) {
				// TODO: handle exception
			}
			// Fecha y hora del viaje
			Date fechaViajeActual = compra.getViaje().getFechaInicio();// .valueOf("2025-06-20");
			LocalTime horaViaje = compra.getViaje().getHoraInicio();// .of(14, 0); // 14:00

			// Fecha y hora que queremos comparar
			LocalDate fechaConsulta = LocalDate.now();
			LocalTime horaConsulta = LocalTime.now();// of(12, 59); // 12:59

			// Convertir la fecha SQL a LocalDate
			LocalDate fechaViaje = fechaViajeActual.toLocalDate();

			// Combinar fecha y hora del viaje
			LocalDateTime fechaHoraViaje = LocalDateTime.of(fechaViaje, horaViaje);

			// Restar una hora
			//LocalDateTime fechaHoraViajeMenosUnaHora = fechaHoraViaje.minusHours(minutos);
			LocalDateTime fechaHoraViajeMenosUnaHora = fechaHoraViaje.minusMinutes(minutos);
			
			// Combinar fecha y hora de consulta
			LocalDateTime fechaHoraConsulta = LocalDateTime.of(fechaConsulta, horaConsulta);

			// Comparar
			boolean esMayor = fechaHoraConsulta.isBefore(fechaHoraViajeMenosUnaHora);

			if (esMayor == true) {

				if (!compra.getEstadoCompra().equals(EstadoCompra.CANCELADA)) { // POR LAS DUDAS REVISAR ESTO

					for (AsientoPorViaje asiento : compra.getAsientos()) {
						asiento.setReservado(false);
						asientoPorViajeRepository.save(asiento); // Suponiendo que tenés este repo
					}
					compra.setEstadoCompra(EstadoCompra.CANCELADA);
					compraPasajeRepository.save(compra);
					
					//enviar push notifications
					Optional<Usuario> Ousuario  = usuarioRepository.findById(compra.getUsuario().getId());// getId();
					//String idUsuario = String.valueOf(usuarioRepository.findById(compra.getUsuario().getId()));// cp.getUsuario().getId());
					String idUsuario = String.valueOf(Ousuario.get().getId());// usuarioRepository.findById(compra.getUsuario().getId()));// cp.getUsuario().getId());
					System.out.println("*** id usuario: " + idUsuario);
					String titulo = "Cancelación de compra";
					System.out.println("*** titulo: " + titulo);
					String mensaje = "La compra del pasaje con destino a " + viaje.getLocalidadDestino().getNombre() + " a sido cancelada";
					System.out.println("*** mensaje: " + mensaje);
					try {
						tokenService.enviarPushNotification(idUsuario, titulo, mensaje);
						System.out.println("se envio la push notification al dispositivo");
					} catch (IOException e) {
						System.out.println("*** NO se envio la push notification al dispositivo***");
						e.printStackTrace();
					} catch (InterruptedException e) {
						System.out.println("*** NO se envio la push notification al dispositivo***");
						e.printStackTrace();
					}
					//hasta aca					
					return 1;
				} else {
					return 2;
				}
			} else if (esMayor == false) {
				System.out.println("No se puede cancelar la compra dado que el mismo ya salió o esta próximo a salir");
				return 5;
			} else {
				System.out.println("No se puede cancelar la compra dado que el viaje comienza en menos de una hora");
				return 4;
			}

		} catch (Exception e) {
			return 3;// TODO: handle exception
		}

	}

	// Ejecuta cada minuto
	@Scheduled(fixedRate = 60000) // cada 60 segundos
	public void eliminarReservasVencidas() {
		LocalDateTime tiempoDeReserva = LocalDateTime.now().minusMinutes(10);

		List<CompraPasaje> vencidas = compraPasajeRepository
				.findByEstadoCompraAndFechaHoraCompraBefore(EstadoCompra.RESERVADA, tiempoDeReserva);

		if (!vencidas.isEmpty()) {
			for (CompraPasaje compra : vencidas) {
				long idCompra = compra.getId();
				transactionalService.eliminarCompra(idCompra);
				System.out.println("Eliminando reservas vencidas: " + vencidas.size());
			}
		} else {

		}
	}

	public List<DtoCompraPasajeNombre> obtenerComprasPorViaje(long idViaje) {
		List<CompraPasaje> listadoCompra = new ArrayList<>();
		List<DtoCompraPasajeNombre> dtoListadoCompra = new ArrayList<>();
		try {
			listadoCompra = compraPasajeRepository.findByViajeId(idViaje);
			System.out.println("cantidad de compras: " + listadoCompra.size());
			for (CompraPasaje cp : listadoCompra) {
				System.out.println("Procesando compra con ID: " + cp.getId());
				System.out.println("Usuario: " + cp.getUsuario());
				System.out.println("Vendedor: " + cp.getVendedor());
				System.out.println("Viaje: " + cp.getViaje());
				DtoCompraPasajeNombre compra = new DtoCompraPasajeNombre();
				try {
					compra.setNombreUsuario(cp.getUsuario().getNombre());
					compra.setNombreVendedor(cp.getVendedor().getNombre());
				} catch (Exception e) {
					// TODO: handle exception
				}
				compra.setIdCompra(cp.getId());
				compra.setOrigenDestino(cp.getViaje().getLocalidadOrigen().getNombre() + "-"
						+ cp.getViaje().getLocalidadDestino().getNombre());
				compra.setEstadoCompra(cp.getEstadoCompra());

				LocalDateTime fechaHora = cp.getFechaHoraCompra();

				// Formato para la fecha
				DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String soloFecha = fechaHora.format(formatoFecha);

				// Formato para la hora (HH:mm)
				DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
				String soloHora = fechaHora.format(formatoHora);

				compra.setFechaHora(soloFecha + " " + soloHora);

				List<Integer> asientos = new ArrayList<>();
				for (AsientoPorViaje apv : cp.getAsientos()) {
					asientos.add(apv.getOmnibusAsiento().getAsiento().getId());
				}
				compra.setNumerosDeAsiento(asientos);
				dtoListadoCompra.add(compra);
			}
		} catch (Exception e) {
			e.printStackTrace(); // Esto te mostrará el error exacto en consola
		}
		System.out.println("cantidad de compras para controller: " + dtoListadoCompra.size());
		// TODO Auto-generated method stub
		return dtoListadoCompra;
	}

	public List<DtoTotalPorMes> TotalGanadoPorMes() {
		return compraPasajeRepository.findTotalPorMes();
	}

	public List<DtoTipoDeCompra> comprasPorTipo() {
		return compraPasajeRepository.contarPorTipoVenta();
	}

	public List<DtoComprasUsuarios> comprasPorUsuario() {
		List<DtoComprasUsuarios> resultado = compraPasajeRepository.obtenerComprasPorUsuario();
		return resultado;
	}

	public List<DtoComprasPorTipo> comprasPorTipoVenta() {
		List<DtoComprasPorTipo> resultado = compraPasajeRepository.obtenerComprasPorTipo();
		return resultado;
	}
}
