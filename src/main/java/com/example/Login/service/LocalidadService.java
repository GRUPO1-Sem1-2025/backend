package com.example.Login.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Login.model.Localidad;
import com.example.Login.model.Usuario;
import com.example.Login.repository.LocalidadRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LocalidadService {

	@Autowired
	private LocalidadRepository localidadRepository;

	public List<Localidad> obtenerLocalidades() {
		return localidadRepository.findAll();
	}

	// Guardar localidad
	public Localidad crearLocalidad(Localidad localidad) {
		return localidadRepository.save(localidad);

	}

	public Optional<Localidad> buscarPorNombre(String nombre) {
		
		return localidadRepository.findByNombre(nombre);
	}
	
	public String convertCsvToJson(MultipartFile file) throws Exception {
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

				row.put("nombre", values[0]);
				row.put("departamento", values[1]);
				row.put("activo", values[2]);
								
				if (values[5].equals("true")) {
					localidad.setActivo(true);// values[3].toString());
				} else {
					localidad.setActivo(false);
				}
								
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

}
