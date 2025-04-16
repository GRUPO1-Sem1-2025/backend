package com.example.Login.service;

import java.util.List;
import java.util.Optional;
import com.example.Login.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.dto.EstadoViaje;
import com.example.Login.model.Localidad;
import com.example.Login.model.Omnibus;
import com.example.Login.model.OmnibusAsiento;
import com.example.Login.model.OmnibusAsientoViaje;
import com.example.Login.repository.LocalidadRepository;
import com.example.Login.repository.OmnibusAsientoRepository;
import com.example.Login.repository.OmnibusAsientoViajeRepository;
import com.example.Login.repository.OmnibusRepository;
import com.example.Login.model.Viaje;

@Service
public class ViajeService {

	@Autowired
	private LocalidadRepository localidadRepository;

	@Autowired
	private ViajeRepository viajeRepository;

	@Autowired
	private OmnibusRepository omnibusRepository;

	@Autowired
	private OmnibusAsientoRepository omnibusAsientoRepository;

	@Autowired
	private OmnibusAsientoViajeRepository omnibusAsientoViajeRepository;

	public boolean crearViaje(Viaje viaje) {
		Viaje nuevoViaje = new Viaje();
		Optional<Localidad> locOri = localidadRepository.findById(viaje.getLocalidadOrigen().getId());
		Optional<Localidad> locDest = localidadRepository.findById(viaje.getLocalidadDestino().getId());

		if (locOri.isPresent() && locDest.isPresent()) {
			if (locOri.get().getId() == locDest.get().getId()) {
				System.out.println("La ciudad de origen y destino no pueden ser las mismas");
				return false;
			}
			if (!locOri.get().isActivo() || !locDest.get().isActivo()) {
				System.out.println("Una de las ciudades no se encuentra disponible");
				return false;
			}

			Localidad localidadOrigen = locOri.get();
			Localidad localidadDestino = locDest.get();

			nuevoViaje.setFechaFin(viaje.getFechaFin());
			nuevoViaje.setFechaInicio(viaje.getFechaInicio());
			nuevoViaje.setHoraInicio(viaje.getHoraInicio());
			nuevoViaje.setHoraFin(viaje.getHoraFin());
			nuevoViaje.setLocalidadOrigen(localidadOrigen);
			nuevoViaje.setLocalidadDestino(localidadDestino);

			viajeRepository.save(nuevoViaje);
			return true;
		}
		System.out.println("Una de las ciudads ingresadas no existe en el sistema");
		return false;
	}
	
	public int asignarOmnibusAViaje(Omnibus omnibus, Viaje viaje) {
	    int resultado = 0;

	    try {
	        Optional<Omnibus> busOpt = omnibusRepository.findById(omnibus.getId());

	        if (busOpt.isPresent()) {
	            Omnibus bus = busOpt.get();

	            if (bus.getLocalidad() != null && viaje.getLocalidadOrigen() != null &&
	                bus.getLocalidad().equals(viaje.getLocalidadOrigen())) {

	                int idBus = bus.getId();
	                System.out.println("ID del bus: " + idBus);

	                List<OmnibusAsiento> omnibusAsientos = omnibusAsientoRepository.buscarPorBus(idBus);

	                for (OmnibusAsiento oa : omnibusAsientos) {
	                    OmnibusAsientoViaje oav = new OmnibusAsientoViaje();
	                    oav.setOmnibusAsiento(oa);
	                    oav.setViaje(viaje);
	                    oav.setEstadoViaje(EstadoViaje.NUEVO);
	                    omnibusAsientoViajeRepository.save(oav);
	                }

	                resultado = 1; // éxito
	            } else {
	                System.out.println("No se puede asignar el viaje porque el bus no se encuentra en la localidad origen del viaje.");
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

//	public int asignarOmnibusAViaje(Omnibus omnibus, Viaje viaje) {
//		int resultado = 0;
//
//		try {
//			Optional<Omnibus> bus = omnibusRepository.findById(omnibus.getId());
//
//			if (bus.isPresent()) {
//				String localidad = bus.get().getLocalidad();
//				if (localidad.equals(viaje.getLocalidadOrigen())) {
//					int idBus = bus.get().getId();
//					System.out.println("ID del bus: " + idBus);
//
//					List<OmnibusAsiento> omnibusAsientos = omnibusAsientoRepository.buscarPorBus(idBus);
//
//					for (OmnibusAsiento oa : omnibusAsientos) {
//						OmnibusAsientoViaje oav = new OmnibusAsientoViaje();
//						oav.setOmnibusAsiento(oa);
//						oav.setViaje(viaje);
//						oav.setEstadoViaje(EstadoViaje.NUEVO);
//						omnibusAsientoViajeRepository.save(oav);
//					}
//
//					resultado = 1; // éxito
//				}else {
//					System.out.println("No se puede asignar el viaje ya que el bus no se encuentra en la localida"
//							+ "origen del viaje");
//					return resultado = 4;
//				}
//			} else {
//				System.out.println("No se encontró el ómnibus.");
//				resultado = 2;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultado = 3;
//		}
//
//		return resultado;
//	}

//	public int asignarOmnibusAViaje(Omnibus omnibus, Viaje viaje) {
//
//		int resultado = 0;
//		OmnibusAsientoViaje oav = new OmnibusAsientoViaje();
//
//		try {
//			// Verificar si el omnibus existe
//			Optional<Omnibus> bus = omnibusRepository.findById(omnibus.getId());
//
//			if (bus.isPresent()) {
//				int idBus = bus.get().getId();
//				System.out.println("id de Bus: " + idBus);
//
//				// Llamar al repositorio para obtener OmnibusAsiento usando el id del omnibus
//				List<OmnibusAsiento> omnibusAsiento = omnibusAsientoRepository.buscarPorBus(idBus);
//
//				for (OmnibusAsiento oa : omnibusAsiento) {
//
//					if (oa.isPresent()) {
//						OmnibusAsiento oaEncontrado = omnibusAsiento.get();
//
//						// Aquí podés asignar los valores a 'oav' (OmnibusAsientoViaje)
//						oav.setOmnibusAsiento(oa);
//						oav.setViaje(viaje);
//						oav.setEstadoViaje(EstadoViaje.NUEVO);
//
//						// Aquí guardás 'oav' en la base de datos
//						omnibusAsientoViajeRepository.save(oav);
//
//						// Resultado de éxito
//						resultado = 1;
//					} else {
//						System.out.println("No se encontró un OmnibusAsiento con el id del omnibus.");
//						resultado = 2; // No se encontró el OmnibusAsiento
//					}
//				}
//			} else {
//				System.out.println("No se encontró el omnibus.");
//				resultado = 3; // No se encontró el omnibus
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultado = 4; // Error inesperado
//		}
//
//		return resultado;
//	}

//	public int asignarOmnibusAViaje(Omnibus omnibus, Viaje viaje) {
//		int resultado=0;
//		OmnibusAsientoViaje oav = new OmnibusAsientoViaje();
//		OmnibusAsiento oa = new OmnibusAsiento();
//		
//		
//		
//		
//		try {
//			Optional<Omnibus> bus = omnibusRepository.findById(omnibus.getId());
//			
//			if(bus.isPresent()) {
//				int id_bus = bus.get().getId();
//				System.out.println("id de Bus: "+ id_bus);
//				
//				@Query("SELECT oa FROM OmnibusAsiento oa WHERE oa.omnibus.id = :busId")
//				Optional<OmnibusAsiento> buscarUnoPorBus(@Param("busId") Long busId);
//				
//				
//				
//				//oav.set
//				
//			}
//		}
//		catch (Exception e) {
//	        //System.out.println("Error al buscar la localidad: " + e.getMessage());
//	        e.printStackTrace();
//			
//		}
//		
//		
//		return resultado;
//	}

}
