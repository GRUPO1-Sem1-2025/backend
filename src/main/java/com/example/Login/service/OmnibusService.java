package com.example.Login.service;

import com.example.Login.dto.DtoBus;
import com.example.Login.model.Asiento;
import com.example.Login.model.Localidad;
import com.example.Login.model.Omnibus;
import com.example.Login.model.OmnibusAsiento;
import com.example.Login.repository.LocalidadRepository;
import com.example.Login.repository.OmnibusAsientoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.example.Login.model.Usuario;
import com.example.Login.repository.OmnibusRepository;
import com.example.Login.repository.ViajeRepository;
//import com.example.Login.repository.UsuarioRepository;

@Service
public class OmnibusService {

	@Autowired
	private OmnibusRepository omnibusRepository;

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

//	public List<Integer> mostrarAsientosLibres(int bus_id) {
//		Optional<Omnibus> omnibus = omnibusRepository.findById(bus_id);
//		Omnibus bus = omnibus.get();
//		System.out.println("Encontre el bus");
//
//		List<Integer> asientosLibres = new ArrayList<>();
//		List<OmnibusAsiento> listaOmnibusAsiento = bus.getOmnibusAsientos();
//
//		for (OmnibusAsiento oa : listaOmnibusAsiento) {
//			boolean estado = oa.isEstado();
//			System.out.println("Estado del asiento: " + estado);
//
//			if (estado == true) {
//				System.out.println("El asiento " + oa.getAsiento().getNro() + " esta libre");
//				asientosLibres.add(oa.getAsiento().getNro());
//			} else {
//				System.out.println("El asiento " + oa.getAsiento().getNro() + " esta ocupado");
//			}
//		}
//		return asientosLibres;
//	}

//	public boolean cambiarEstadoAsiento(int bus_id, int nro_asiento) {
//		Optional<Omnibus> omnibus = omnibusRepository.findById(bus_id);
//		Omnibus bus = omnibus.get();
//		System.out.println("Encontre el bus");
//
//		if (bus.isActivo()) {
//			List<OmnibusAsiento> listaOmnibusAsiento = bus.getOmnibusAsientos();
//
//			for (OmnibusAsiento oa : listaOmnibusAsiento) {
//				int numeroAsiento = oa.getAsiento().getNro();
//				System.out.println("Estado del asiento: " + numeroAsiento);
//
//				if (nro_asiento == numeroAsiento) {
//					System.out
//							.println("El asiento buscado (" + oa.getAsiento().getNro() + ") fue encontrado en el bus");
//					if (oa.isEstado() == true) {
//						oa.setEstado(false);
//					} else {
//						oa.setEstado(true);
//					}
//					omnibusasientoRepository.save(oa);
//					return true;
//				} else {
//					System.out.println("El asiento " + oa.getAsiento().getNro() + " no esta disponible en el bus");
//				}
//			}
//		} else {
//			System.out.println("El bus se encuenta en mantenimiento");
//			return false;
//		}
//		return false;
//	}

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

}