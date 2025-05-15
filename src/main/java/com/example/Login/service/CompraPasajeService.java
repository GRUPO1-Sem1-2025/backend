package com.example.Login.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.DtoRespuestaCompraPasaje;
import com.example.Login.dto.DtoVenderPasaje;
import com.example.Login.dto.EstadoCompra;
import com.example.Login.dto.EstadoViaje;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Usuario;
import com.example.Login.model.Viaje;
import com.example.Login.repository.AsientoPorViajeRepository;
import com.example.Login.repository.CompraPasajeRepository;
import com.example.Login.repository.UsuarioRepository;
import com.example.Login.repository.ViajeRepository;
//import com.sun.org.apache.bcel.internal.generic.RETURN;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CompraPasajeService {

    private final SecurityFilterChain securityFilterChain;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private AsientoPorViajeRepository asientoPorViajeRepository;

	@Autowired
	private CompraPasajeRepository compraPasajeRepository;
	
	@Autowired
	private TransactionalService transactionalService;

	@Autowired
	private ViajeRepository viajeRepository;

    CompraPasajeService(SecurityFilterChain securityFilterChain) {
        this.securityFilterChain = securityFilterChain;
    }

	public DtoRespuestaCompraPasaje comprarPasaje(DtoCompraPasaje request) {
		Usuario vendedor = new Usuario();
		Usuario usuario = new Usuario();
		Viaje viaje = new Viaje();
		
		List <Integer> asientosOcupado = new ArrayList<>();
		EstadoCompra estado = request.getEstadoCompra(); // Ya es un enum
		DtoRespuestaCompraPasaje asientosOcupados = new DtoRespuestaCompraPasaje();
		asientosOcupados.setEstado(estado);
		
		try {
			Optional<Usuario> Ousuario = usuarioRepository.findById(request.getUsuarioId());
			usuario = Ousuario.get();
			
			if (usuario.getRol()!= 100) {
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
					//return 2;
				}
			}
		}
		
		if(!asientosOcupado.isEmpty()) {
			System.out.println("asientos ocupados: " + asientosOcupado);
			asientosOcupados.setAsientosOcupados(asientosOcupado);
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
		compra.setTotal(compra.getCat_pasajes() * viaje.getPrecio());
		compra.setAsientos(asientosReservados);
		compraPasajeRepository.save(compra);

		for (AsientoPorViaje apv : asientosReservados) {
			asientoPorViajeRepository.save(apv);
		}
//		switch (estado) {
//		case REALIZADA:
//			return asientosOcupados;
//		case RESERVADA:
//			return asientosOcupados;
//		default:
//			System.out.println("Estado desconocido: " + estado);
//		}
		
	
		

		return asientosOcupados;
	}
	
	public int venderPasaje(DtoVenderPasaje request) {
		Usuario vendedor = new Usuario();
		Usuario usuario = new Usuario();
		Viaje viaje = new Viaje();
		try {
			Optional<Usuario> Ousuario = usuarioRepository.findByEmail(request.getEmailCliente());// Class(). getClass() getClass() UsuarioId());
			usuario = Ousuario.get();
		} catch (Exception e) {
			usuario = null;
		}
		try {
			Optional<Usuario> Ovendedor = usuarioRepository.findById(request.getVendedorId());
			vendedor = Ovendedor.get();
			if (vendedor.getRol()!= 200) {
				return 8;
			}
			if(!vendedor.getActivo()) {
				return 4;
			}
		} catch (Exception e) {
			vendedor = null;
			return 1;
		}
		try {
			Optional<Viaje> Oviaje = viajeRepository.findById(request.getViajeId());
			viaje = Oviaje.get();
		} catch (Exception e) {
			return 5;
		}

		CompraPasaje compra = new CompraPasaje();
		if (vendedor == null) {
			compra.setTipo_venta("Online");
		} else {
			compra.setTipo_venta("Ventanilla");
		}
		compra.setUsuario(usuario);
		compra.setVendedor(vendedor);
		compra.setViaje(viaje);
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
					return 2;
				}
			}
		}
		EstadoCompra estado = EstadoCompra.REALIZADA;
		compra.setEstadoCompra(estado);
//		EstadoCompra estado = request.getEstadoCompra(); // Ya es un enum
//		switch (estado) {
//		case REALIZADA:
//			compra.setEstadoCompra(EstadoCompra.REALIZADA);
//			break;
//		case RESERVADA:
//			compra.setEstadoCompra(EstadoCompra.RESERVADA);
//			break;
//		default:
//			System.out.println("Estado desconocido: " + estado);
//		}

		compra.setCat_pasajes(asientosReservados.size());
		compra.setTotal(compra.getCat_pasajes() * viaje.getPrecio());
		compra.setAsientos(asientosReservados);
		compraPasajeRepository.save(compra);

		for (AsientoPorViaje apv : asientosReservados) {
			asientoPorViajeRepository.save(apv);
		}
		switch (estado) {
		case REALIZADA:
			return 3;
		case RESERVADA:
			return 6;
		default:
			System.out.println("Estado desconocido: " + estado);
		}

		return 7;
	}

	public int cancelarCompra(int idCompra) {
		try {
			Optional<CompraPasaje> Ocompra = compraPasajeRepository.findById(idCompra);
			CompraPasaje compra = Ocompra.get();

			if (!compra.getEstadoCompra().equals(EstadoCompra.CANCELADA)) {  // POR LAS DUDAS REVISAR ESTO

				for (AsientoPorViaje asiento : compra.getAsientos()) {
					asiento.setReservado(false);
					asientoPorViajeRepository.save(asiento); // Suponiendo que tenés este repo
				}
				compra.setEstadoCompra(EstadoCompra.CANCELADA);
				compraPasajeRepository.save(compra);
				return 1;
			} else {
				return 2;
			}
		} catch (Exception e) {
			return 3;// TODO: handle exception
		}
	}	

	// Ejecuta cada minuto
	@Scheduled(fixedRate = 60000) // cada 60 segundos
	public void eliminarReservasVencidas() {
	    LocalDateTime tiempoDeReserva = LocalDateTime.now().minusMinutes(10);

	    List<CompraPasaje> vencidas = compraPasajeRepository.findByEstadoCompraAndFechaHoraCompraBefore(
	            EstadoCompra.RESERVADA, tiempoDeReserva
	    );
	

	    if (!vencidas.isEmpty()) {
	        for (CompraPasaje compra: vencidas) {
		    	long idCompra = compra.getId();
		    	transactionalService.eliminarCompra(idCompra);
		    	System.out.println("Eliminando reservas vencidas: " + vencidas.size());
		    }
	        //System.out.println("Eliminando reservas vencidas: " + vencidas.size());
	        //compraPasajeRepository.deleteAll(vencidas);
	    }else {
	        System.out.println("No existen reservas para eliminar que cumplan con la condición: " + vencidas.size());

	    }
	}
}
