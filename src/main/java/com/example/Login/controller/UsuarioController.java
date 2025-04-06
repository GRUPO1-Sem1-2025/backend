package com.example.Login.controller;

import com.example.Login.model.Usuario;
import com.example.Login.service.EmailService;
import com.example.Login.service.GenerarContraseniaService;
import com.example.Login.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
//@RequestMapping("/usuarios")
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "API para gestionar usuarios")

public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de usuarios")
    
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }


    @PostMapping
    @Operation(summary = "Crear un usuario", description = "Agrega un nuevo usuario")
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
    @Operation(summary = "Obtener un usuario por email", description = "Retorna un usuario basado en su email")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        return usuario.map(ResponseEntity::ok) // Si el usuario existe, devolver 200 OK
                      .orElseGet(() -> ResponseEntity.notFound().build()); // Si no existe, devolver 404
    }
    
    
    // 🔹 Recibe email y password en el cuerpo de la petición (POST)
    @PostMapping("/login")
    @Operation(summary = "Login de un usuario", description = "Permite el inicio de sesión de un usuario con email y contraseña")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        String token = usuarioService.authenticate(email, password);
        System.out.println("Token: "+ token);
        System.out.println("password primero: " + password);
		
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        Map<String, String> response = new HashMap<>();

        if (usuario.isPresent()) {
            Usuario usuarioEncontrado = usuario.get();
            // 🔹 Compara la contraseña ingresada encriptada con la almacenada en la BD
			if (usuarioService.encriptarSHA256(password).equals(usuarioEncontrado.getPassword())) {
				System.out.println("las contraseñas coinciden en controller");
				if (token != null) {
					return ResponseEntity.ok(Map.of("token", token));
				} 
			}
        }

        response.put("mensaje", "Credenciales incorrectas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    } 
    
    //@DeleteMapping("/{email}")
    @DeleteMapping("/{email}")
    @Operation(summary = "Borrar un usuario por email", description = "Elimina un usuario de la base de datos por su email")
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
    
    @Autowired
    private UsuarioService fileConversionService;

    @PostMapping("/crearUsuariosMasivos")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo procesado esta vacio");
        }

        try {
            // Llamamos al servicio para convertir el archivo a JSON
            String json = fileConversionService.convertCsvToJson(file);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo: " + e.getMessage());
        }
    }
    
    @GetMapping("/contar")
    @Operation(summary = "Obtener cantidad de usuarios", description = "Retorna la cantidad de usuarios del sistema")
    
    public long verCantidadUsuarios() {
        return usuarioService.verCantidadUsuarios();
    }
    
	@Autowired
	private EmailService emailService;

	@PostMapping("/cambiarcontrasenia")
	public String cambiarContraseña(@RequestParam String para) {
		String asunto = "Nueva contraseña temporal";
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(para);

		if (usuario.isPresent()) {

			// Nueva contraseña aleatoria
			String contrasenia = GenerarContraseniaService.generarContraseniaAleatoria();

			String mensaje = "Su nueva contraseñia temporal es la siguiente: " + contrasenia;

			// Setear nueva contraseña encriptada
			usuario.get().setPassword(usuarioService.encriptarSHA256(contrasenia));
			usuarioService.actualizar(usuario.get());

			emailService.enviarCorreo(para, asunto, mensaje);

			return "Correo enviado!";
		} else {
			return "No existe el usuario ingresado";
		}
	}
		
		
//		// Nueva contraseña aleatoria
//		String contrasenia = GenerarContraseniaService.generarContraseniaAleatoria();
//		String cuerpo = "Su nueva contraseñia temporal es la siguiente: "
//				+ contrasenia;
//		
//		// Nueva contraseña encriptada
//		//String contrasenia = usuarioService.encriptarSHA256(GenerarContraseniaService.generarContraseniaAleatoria()); 
//		System.out.println(contrasenia);
//		
//		// busca el usuario si existe
//		Optional<Usuario> usuario = usuarioService.buscarPorEmail(para); 
//
//		if (usuario.isPresent()) {
//			usuario.get().setPassword(contrasenia); // le cambia la contraseña
//			emailService.enviarCorreo(para, asunto, cuerpo);
//			return "Correo enviado!";
//		} else {
//			return "No existe el usuario ingresado";
//		}
//	}      
}
