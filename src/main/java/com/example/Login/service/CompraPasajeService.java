package com.example.Login.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Usuario;
import com.example.Login.repository.AsientoPorViajeRepository;
import com.example.Login.repository.CompraPasajeRepository;
import com.example.Login.repository.UsuarioRepository;

@Service
public class CompraPasajeService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AsientoPorViajeRepository asientoPorViajeRepository;

    @Autowired
    private CompraPasajeRepository compraPasajeRepository;

    public void comprarPasaje(DtoCompraPasaje request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        for (Integer nroAsiento : request.getNumerosDeAsiento()) {
            // Buscar el asientoPorViaje por número y viaje
            Optional<AsientoPorViaje> asientoOpt = asientoPorViajeRepository
                     .findByViajeIdAndNroAsiento(request.getViajeId(), nroAsiento);

            if (asientoOpt.isPresent()) {
                AsientoPorViaje asiento = asientoOpt.get();

                if (!asiento.isReservado()) {
                    // Marcar como reservado
                    asiento.setReservado(true);
                    asientoPorViajeRepository.save(asiento);

                    // Registrar compra
                    CompraPasaje compra = new CompraPasaje();
                    compra.setUsuario(usuario);
                    compra.setAsientoPorViaje(asiento);
                    compra.setFechaHoraCompra(LocalDateTime.now());
                    compraPasajeRepository.save(compra);
                } else {
                    throw new RuntimeException("El asiento " + nroAsiento + " ya está reservado");
                }
            } else {
                throw new RuntimeException("Asiento nro " + nroAsiento + " no encontrado para ese viaje");
            }
        }
    }
}
