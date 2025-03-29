package com.example.Login.controller;

import com.example.Login.model.Usuario;
import com.example.Login.service.UsuarioService;
import com.example.Login.configuration.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

	/*
	 * @PostMapping public Usuario crearUsuario(@RequestBody Usuario usuario) {
	 * return usuarioService.guardarUsuario(usuario); }
	 */
    
    @PostMapping
    public ResponseEntity<Map<String,String>> crearUsuario(@RequestBody Usuario usuario) {
    	
    	Optional<Usuario> user = usuarioService.buscarPorEmail(usuario.getEmail());
    	Map<String, String> response = new HashMap<>();
    	
    	if (user.isPresent()) {
    		response.put("mensaje", "El usuario ya se encuentra registrado con ese correo");
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    	}
    	
    	usuarioService.guardarUsuario(usuario);
    	// 🔹 Prepara la respuesta exitosa
        response.put("mensaje", "Usuario registrado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // ✅ 201 - Creado
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        return usuario.map(ResponseEntity::ok) // Si el usuario existe, devolver 200 OK
                      .orElseGet(() -> ResponseEntity.notFound().build()); // Si no existe, devolver 404
    }
    
    
    // 🔹 Recibe email y password en el cuerpo de la petición (POST)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
		/*
		 * @GetMapping("/login/{email}/{password}") public ResponseEntity<Map<String,
		 * String>> login(@PathVariable String email, @PathVariable String password) {
		 */
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        Map<String, String> response = new HashMap<>();

        if (usuario.isPresent()) {
            Usuario usuarioEncontrado = usuario.get();
            
            // 🔹 Compara la contraseña ingresada encriptada con la almacenada en la BD
            if (usuarioService.encriptarSHA256(password).equals(usuarioEncontrado.getPassword())) { 
                response.put("mensaje", "Inicio de sesión exitoso");
                return ResponseEntity.ok(response);
            }
        }

        response.put("mensaje", "Credenciales incorrectas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    } 
    
    @DeleteMapping("/{email}")
    public ResponseEntity<Map<String, String>> borrarUsuario(@PathVariable String email) {
    	
    	Optional<Usuario> user = usuarioService.buscarPorEmail(email);
    	Map<String, String> response = new HashMap<>();
    	
    	if (user.isPresent()) {
    		response.put("mensaje", "El usuario ha sido borrado");
    		usuarioService.borrarUsuario(user);// borrarUsuario(usuario);
    		return ResponseEntity.ok(response);
    	}
    	response.put("mensaje", "No existe el usuario");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }    
}
