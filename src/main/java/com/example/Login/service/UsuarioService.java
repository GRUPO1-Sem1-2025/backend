package com.example.Login.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Login.model.Usuario;
import com.example.Login.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.stereotype.Service;



@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    

    // Inyección de dependencias
    public UsuarioService(UsuarioRepository usuarioRepository){
    	this.usuarioRepository = usuarioRepository;
    }
    
 // Método para encriptar con SHA-256
    public String encriptarSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                hexString.append(String.format("%02x", b)); // 🔹 Convierte a formato hexadecimal
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

     public List<Usuario> obtenerUsuarios() {
     return usuarioRepository.findAll();
    }
         
     // 🔹 Guardar usuario con contraseña encriptada
     public Usuario guardarUsuario(Usuario usuario) {
    	 usuario.setPassword(encriptarSHA256(usuario.getPassword()));
         return usuarioRepository.save(usuario);
         
     }
     
     public Usuario borrarUsuario(Optional<Usuario> user) {
    	 Usuario u = user.get();
    	 usuarioRepository.delete(u);
		return u;
         
     }
     
     public String convertCsvToJson(MultipartFile file) throws Exception {
         BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
         String line;
         List<Map<String, String>> dataList = new ArrayList<>();
         String[] headers = null;

         try {
             // Leer línea por línea el archivo CSV
             while ((line = reader.readLine()) != null) {
                 String[] values = line.split(":");  // Suponiendo que el delimitador es ":"
                 
                 // Si la primera línea contiene los encabezados
                 if (headers == null) {
                     headers = values;
                     continue;  // Saltamos a la siguiente línea si es la de encabezados
                 }

                 // Procesamos las filas posteriores como datos
                 if (values.length != headers.length) {
                     throw new Exception("La cantidad de columnas no coincide con los encabezados.");
                 }

                 Map<String, String> row = new HashMap<>();
                 for (int i = 0; i < values.length; i++) {
                     row.put(headers[i], values[i]);
                 }
                 dataList.add(row);
             }

             // Convertir la lista a formato JSON
             ObjectMapper objectMapper = new ObjectMapper();
             return objectMapper.writeValueAsString(dataList);

         } catch (Exception e) {
             throw new Exception("Error procesando el archivo CSV: " + e.getMessage());
         } finally {
             reader.close();  // Cerramos el BufferedReader
         }
     }
     
    
    
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
}