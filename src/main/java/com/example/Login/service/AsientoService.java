package com.example.Login.service;


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

import com.example.Login.model.Asiento;
import com.example.Login.model.Omnibus;
import com.example.Login.model.Usuario;
import com.example.Login.repository.AsientoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AsientoService {
	
    @Autowired
	private final AsientoRepository asientoRepository;
    
    // Inyección de dependencias
    public AsientoService(AsientoRepository asientoRepository){
    	this.asientoRepository = asientoRepository;
    }
	
	public String convertCsvToJson(MultipartFile file) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		String line;
		List<Map<String, String>> dataList = new ArrayList<>();

		try {
//			while ((line = reader.readLine()) != null) {
//				Asiento asiento = new Asiento();
//				String[] values = line.split(":"); // Suponiendo que el delimitador es ":"
//
//				// Validamos que haya exactamente 2 valores por línea
//				if (values.length != 2) {
//					throw new Exception("Formato incorrecto en el CSV. Cada fila debe tener 2 valores.");
//				}
//
//				// Construimos el JSON correctamente
//				Map<String, String> row = new HashMap<>();
//
//				row.put("id", values[0]);
//				row.put("estado", values[1]);
//				
//				// cargar datos del asiento
//				asiento.setId(Integer.parseInt(values[0]));
//				asiento.setNro(Integer.parseInt(values[0]));
//				System.out.println("Asiento Id: " + asiento.getId());// asiento fue registrado");
//				System.out.println("Asiento Nro: " + asiento.getNro());// asiento fue registrado");
//
//				if (values[1].equals("libre")) {
//					asiento.setEstado(true);
//				} else if (values[1].equals("ocupado")) {
//					asiento.setEstado(false);
//				} else {
//					return "estado del asiento incorrecto";
//				}
//				System.out.println("Asiento estado: " + asiento.getEstado());// Nro());// asiento fue registrado");
//				
//				List<Bus> buses = null;// [];
//				asiento.setBuses(buses);
//				
//				
//				dataList.add(row);
			while ((line = reader.readLine()) != null) {
			    String[] values = line.split(":");

			    if (values.length != 2) {
			        throw new Exception("Formato incorrecto en el CSV. Cada fila debe tener 2 valores.");
			    }

			    // Extraemos valores
			    int nroAsiento = Integer.parseInt(values[0]);
			    String estadoAsiento = values[1];

			    // Buscamos si ya existe un asiento con ese 'nro'
			    Asiento asiento = asientoRepository.findById(nroAsiento).orElse(new Asiento());
			    asiento.setNro(nroAsiento);

			    if (estadoAsiento.equals("libre")) {
			        asiento.setEstado(true);
			    } else if (estadoAsiento.equals("ocupado")) {
			        asiento.setEstado(false);
			    } else {
			        return "estado del asiento incorrecto";
			    }

			    asiento.setBuses(null); // o simplemente no tocar este campo si no estás manejando buses

			    asientoRepository.save(asiento);

			    Map<String, String> row = new HashMap<>();
			    row.put("nro", String.valueOf(nroAsiento));
			    row.put("estado", estadoAsiento);
			    dataList.add(row);
			
				asientoRepository.save(asiento);// save(asiento);
				System.out.println("El asiento fue registrado");
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
	
	public long asientosTotales() {
		return asientoRepository.count();
	}
	

}
