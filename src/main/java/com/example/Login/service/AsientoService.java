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

			while ((line = reader.readLine()) != null) {
			    String[] values = line.split(":");

			    if (values.length != 1) {
			        throw new Exception("Formato incorrecto en el CSV. Cada fila debe tener 2 valores.");
			    }

			    // Extraemos valores
			    int nroAsiento = Integer.parseInt(values[0]);
			    //String estadoAsiento = values[1];

			    // Buscamos si ya existe un asiento con ese 'nro'
			    Asiento asiento = asientoRepository.findById(nroAsiento).orElse(new Asiento());
			    asiento.setNro(nroAsiento);

			    asientoRepository.save(asiento);

			    Map<String, String> row = new HashMap<>();
			    row.put("nro", String.valueOf(nroAsiento));
			    dataList.add(row);
			
				try{
					asientoRepository.save(asiento);// save(asiento);
				}catch (Exception e) {
					// TODO: handle exception
				}
				System.out.println("El asiento " + values[0] + " fue registrado");
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
