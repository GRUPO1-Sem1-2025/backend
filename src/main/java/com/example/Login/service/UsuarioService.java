package com.example.Login.service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Login.model.Usuario;
import com.example.Login.repository.UsuarioRepository;
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
    
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
}