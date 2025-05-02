package com.example.Login.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.EstadoCompra;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Usuario;
import com.example.Login.model.Viaje;
import com.example.Login.repository.AsientoPorViajeRepository;
import com.example.Login.repository.CompraPasajeRepository;
import com.example.Login.repository.UsuarioRepository;
import com.example.Login.repository.ViajeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CompraPasajeService {

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

//	public void comprarPasaje(DtoCompraPasaje request) {
//		Usuario vendedor = new Usuario();
//		Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
//				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//		try {
//			Optional<Usuario> Ovendedor = usuarioRepository.findById(request.getVendedorId());
//			vendedor = Ovendedor.get();
//		} catch (Exception e) {
//			vendedor = null;
//		}
//
//		Optional<Viaje> Oviaje = viajeRepository.findById(request.getViajeId());
//		Viaje viaje = Oviaje.get();
//
//		CompraPasaje compra = new CompraPasaje();
//		if (vendedor == null) {
//			compra.setTipo_venta("Online");
//		} else {
//			compra.setTipo_venta("Ventanilla");
//		}
//		compra.setUsuario(usuario);
//		compra.setVendedor(vendedor);
//		compra.setViaje(viaje);
//		compra.setFechaHoraCompra(LocalDateTime.now());
//
//		List<AsientoPorViaje> asientosReservados = new ArrayList<>();
//
//		for (Integer nroAsiento : request.getNumerosDeAsiento()) {
//			Optional<AsientoPorViaje> apvOpt = asientoPorViajeRepository.findByViajeIdAndNroAsiento(viaje.getId(),
//					nroAsiento);
//			if (apvOpt.isPresent()) {
//				AsientoPorViaje apv = apvOpt.get();
//				if (!apv.isReservado()) {
//					apv.setReservado(true);
//					asientosReservados.add(apv);
//				} else {
//					throw new RuntimeException("Asiento nro " + nroAsiento + " ya está reservado.");
//				}
//			}
//		}
//		compra.setCat_pasajes(asientosReservados.size());
//		compra.setTotal(compra.getCat_pasajes() * viaje.getPrecio());
//		compra.setAsientos(asientosReservados);
//		compra.setEstadoCompra("Realizada");
//		compraPasajeRepository.save(compra);
//
//		for (AsientoPorViaje apv : asientosReservados) {
//			asientoPorViajeRepository.save(apv);
//		}
//	}

	public int comprarPasaje(DtoCompraPasaje request) {
		Usuario vendedor = new Usuario();
		Usuario usuario = new Usuario();
		Viaje viaje = new Viaje();
		try {
			Optional<Usuario> Ousuario = usuarioRepository.findById(request.getUsuarioId());
			usuario = Ousuario.get();
			if (usuario.getActivo() == false) {
				return 4;
			}
		} catch (Exception e) {
			return 1;
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

		EstadoCompra estado = request.getEstadoCompra(); // Ya es un enum
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

			if (compra.getEstadoCompra().equals("Realizada")) {

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
