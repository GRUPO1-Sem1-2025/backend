package com.example.Login.service;

import java.util.List;
import java.util.Optional;
import com.example.Login.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.EstadoViaje;
import com.example.Login.model.AsientoPorViaje;
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

//	@Autowired
//	private OmnibusAsientoRepository omnibusAsientoRepository;

//	@Autowired
//	private OmnibusAsientoViajeRepository omnibusAsientoViajeRepository;

	public int crearViaje(DtoViaje dtoViaje) {
		Viaje nuevoViaje = new Viaje();
		System.out.println("idOrigen: " + dtoViaje.getIdLocalidadOrigen());
		System.out.println("idOrigen: " + dtoViaje.getIdLocalidadOrigen());
		Optional<Localidad> locOri = localidadRepository.findById(dtoViaje.getIdLocalidadOrigen());// getIdLocalidadOrigen().getId());
		Optional<Localidad> locDest = localidadRepository.findById(dtoViaje.getIdLocalidadDestino());//.getId());

		if (locOri.isPresent() && locDest.isPresent()) {
			if (locOri.get().getId() == locDest.get().getId()) {
				System.out.println("La ciudad de origen y destino no pueden ser las mismas");
				return 1;
			}
			if (!locOri.get().isActivo() || !locDest.get().isActivo()) {
				System.out.println("estado origen: " + locOri.get().isActivo());
				System.out.println("estado destino: " + locDest.get().isActivo());
				System.out.println("Una de las ciudades no se encuentra disponible");
				return 2;
			}

			Localidad localidadOrigen = locOri.get();
			Localidad localidadDestino = locDest.get();

			nuevoViaje.setFechaFin(dtoViaje.getFechaFin());
			nuevoViaje.setFechaInicio(dtoViaje.getFechaInicio());
			nuevoViaje.setHoraInicio(dtoViaje.getHoraInicio());
			nuevoViaje.setHoraFin(dtoViaje.getHoraFin());
			nuevoViaje.setLocalidadOrigen(localidadOrigen);
			nuevoViaje.setLocalidadDestino(localidadDestino);
			nuevoViaje.setPrecio(dtoViaje.getPrecio());
			nuevoViaje.setOmnibus(null);
			nuevoViaje.setAsientosPorViaje(null);
			nuevoViaje.setEstadoViaje(EstadoViaje.NUEVO);
			

			viajeRepository.save(nuevoViaje);
			return 3;
		}
		System.out.println("Una de las ciudads ingresadas no existe en el sistema");
		return 4;
	}

	public int asignarOmnibusAViaje_vieja(Omnibus omnibus, Viaje viaje) {
		int resultado = 0;
		Localidad localidadOrigen = viaje.getLocalidadOrigen();
		Optional<Localidad> loc = localidadRepository.findByNombre(localidadOrigen.getNombre());
		OmnibusAsientoViaje oav = new OmnibusAsientoViaje();
//
//		// Crear relación asiento/viaje
//		for (OmnibusAsiento asiento : bus.getAsientos()) {
//		    AsientoPorViaje apv = new AsientoPorViaje();
//		    apv.setOmnibusAsiento(asiento);
//		    apv.setViaje(viaje);
//		    apv.setReservado(false); // al inicio, todos libres
//		    viaje.getAsientosPorViaje().add(apv);
//		}
		try {
			Optional<Omnibus> busOpt = omnibusRepository.findById(omnibus.getId());

			if (busOpt.isPresent()) {
				Omnibus bus = busOpt.get();
				System.out.println(" Omnibus localidad: " + bus.getLocalidad());
				System.out.println(" Localidad localidad: " + loc.get().getNombre());

//	            if (bus.getLocalidad() != null && viaje.getLocalidadOrigen() != null &&
//	                bus.getLocalidad().equals(viaje.getLocalidadOrigen().getId())) {

				// if (bus.getLocalidad() != null && loc.get() != null &&
				// bus.getLocalidad().equals(loc.get())) {

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
					//oav.setOmnibusAsiento(bus.getAsientos());
					//oav.setViaje(viaje);
					//omnibusAsientoViajeRepository.save(oav);
//
//	                int idBus = bus.getId();
//	                System.out.println("ID del bus: " + idBus);
//
//	                List<OmnibusAsiento> omnibusAsientos = omnibusAsientoRepository.buscarPorBus(idBus);
//
//	                for (OmnibusAsiento oa : omnibusAsientos) {
//	                    OmnibusAsientoViaje oav = new OmnibusAsientoViaje();
//	                    oav.setOmnibusAsiento(oa);
//	                    oav.setViaje(viaje);
//	                    oav.setEstadoViaje(EstadoViaje.NUEVO);
//	                    omnibusAsientoViajeRepository.save(oav);
//	                }
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
	                

	                // Crear relación asiento/viaje (modelo AsientoPorViaje, si estás usando ambos modelos)
	                for (OmnibusAsiento asiento : bus.getAsientos()) {
	                    AsientoPorViaje apv = new AsientoPorViaje();
	                    apv.setOmnibusAsiento(asiento);
	                    apv.setViaje(viaje);
	                    apv.setReservado(false); // al inicio, todos libres
	                    viaje.getAsientosPorViaje().add(apv);	                    
	                }

//	                // Crear relación OmnibusAsientoViaje (uno por cada asiento)
//	                for (OmnibusAsiento asiento : bus.getAsientos()) {
//	                    OmnibusAsientoViaje oav = new OmnibusAsientoViaje();
//	                    oav.setOmnibusAsiento(asiento);
//	                    oav.setViaje(viaje);
//	                    oav.setEstadoViaje(EstadoViaje.NUEVO);
//	                    omnibusAsientoViajeRepository.save(oav);
//	                }

	                viajeRepository.save(viaje);
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
