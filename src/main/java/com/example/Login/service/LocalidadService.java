package com.example.Login.service;
import com.example.Login.dto.DtoDestinoMasVistos;
import com.example.Login.dto.DtoLocalidad;
import com.example.Login.model.Localidad;
import com.example.Login.repository.LocalidadRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LocalidadService {

	@Autowired
	private LocalidadRepository localidadRepository;

	public List<Localidad> obtenerLocalidades() {
		return localidadRepository.findAll();
	}

	// Guardar localidad
	public DtoLocalidad crearLocalidad(DtoLocalidad dtoLocalidad) {
		Localidad localidad = new Localidad();
		localidad.setActivo(true);//dtoLocalidad.isActivo());
		localidad.setDepartamento(dtoLocalidad.getDepartamento());
		localidad.setNombre(dtoLocalidad.getNombre());
		localidadRepository.save(localidad);
		return dtoLocalidad;

	}

	public Optional<Localidad> buscarPorNombre(String nombre) {
		
		return localidadRepository.findByNombre(nombre);
	}
	
	public String crearLocalidadesMasivas(MultipartFile file) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		String line;
		List<Map<String, String>> dataList = new ArrayList<>();

		try {
			while ((line = reader.readLine()) != null) {
				Localidad localidad = new Localidad();
				
				Optional<Localidad> localidadNoEncontrado;
				String[] values = line.split(":"); // Suponiendo que el delimitador es ":"

				// Validamos que haya exactamente 3 valores por línea
				if (values.length != 3) {
					throw new Exception("Formato incorrecto en el CSV. Cada fila debe tener 3 valores.");
				}

				// Construimos el JSON correctamente
				Map<String, String> row = new HashMap<>();
				String estado  = values[2].toLowerCase();
				
				row.put("nombre", values[0]);
				row.put("departamento", values[1]);
				row.put("activo", values[2]);
								
				if (estado.equals("true")) {
					localidad.setActivo(true);// values[3].toString());
				} else {
					localidad.setActivo(false);
				}
				localidad.setDepartamento(values[1]);
				localidad.setNombre(values[0]);				
				
				dataList.add(row);
				localidadNoEncontrado = buscarPorNombre(values[0]);
				if (localidadNoEncontrado.isEmpty()) {
					// Guardo el usuario nuevo
					localidadRepository.save(localidad);
					System.out.println("La localidad fue registrada");
				} else {
					System.out.println("la localidad de nombre : " + values[0] + " ya esta registrada en el sistema");
				}
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
	
	public List<Localidad> obtenerLocalidadesActivas(){
		List<Localidad> localidadesActivas = new ArrayList<>();
		List<Localidad> localidadesTotales = localidadRepository.findAll();
		
		for (Localidad localidad : localidadesTotales) {
			if (localidad.isActivo()){
				localidadesActivas.add(localidad);
			}
		}		
		return localidadesActivas;
	}
	
	 public List<DtoDestinoMasVistos> obtenerTop10DestinosConNombre() {
	        return localidadRepository.findTop10DestinosConNombre();
	    }

	public int cambiarEstadoLocalidad(int idLocalidad) {
		int resultado = 0;
		Localidad localidad = new Localidad();
		
		try {
		Optional<Localidad> Olocalidad = localidadRepository.findById(idLocalidad);
		localidad = Olocalidad.get();
		
		if(localidad.isActivo()) {
			localidad.setActivo(false);
		}else {
			localidad.setActivo(true);
		}
		
		localidadRepository.save(localidad);
		resultado = 1;
		
		}catch (Exception e) {
			// TODO: handle exception
		}
		return resultado;
		
		
	}

}
