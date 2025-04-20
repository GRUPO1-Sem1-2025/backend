package com.example.Login.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Usuario;
import com.example.Login.model.Viaje;
import com.example.Login.repository.AsientoPorViajeRepository;
import com.example.Login.repository.CompraPasajeRepository;
import com.example.Login.repository.UsuarioRepository;
import com.example.Login.repository.ViajeRepository;

@Service
public class CompraPasajeService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private AsientoPorViajeRepository asientoPorViajeRepository;

	@Autowired
	private CompraPasajeRepository compraPasajeRepository;

	@Autowired
	private ViajeRepository viajeRepository;

	public void comprarPasaje(DtoCompraPasaje request) {
		Usuario vendedor = new Usuario();
		Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		try {
			Optional<Usuario> Ovendedor = usuarioRepository.findById(request.getVendedorId());
			vendedor = Ovendedor.get();
		} catch (Exception e) {
			vendedor = null;
		}

		Optional<Viaje> Oviaje = viajeRepository.findById(request.getViajeId());
		Viaje viaje = Oviaje.get();

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
					throw new RuntimeException("Asiento nro " + nroAsiento + " ya está reservado.");
				}
			}
		}
		compra.setCat_pasajes(asientosReservados.size());
		compra.setTotal(compra.getCat_pasajes() * viaje.getPrecio());
		compra.setAsientos(asientosReservados);
		compraPasajeRepository.save(compra);

		for (AsientoPorViaje apv : asientosReservados) {
			asientoPorViajeRepository.save(apv);
		}
	}
}
