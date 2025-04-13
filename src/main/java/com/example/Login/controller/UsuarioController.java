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
    	System.out.println("Entre al usuario controller");
    	
    	Optional<Usuario> user = usuarioService.buscarPorEmail(usuario.getEmail());
    	Map<String, String> response = new HashMap<>();
    	
    	if (user.isPresent()) {
    		response.put("mensaje", "El usuario ya se encuentra registrado con ese correo");
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    	}
    	
    	usuarioService.crearUsuario(usuario);
    	// 🔹 Prepara la respuesta exitosa
        response.put("mensaje", "Usuario registrado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // ✅ 201 - Creado
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener un usuario por email", description = "Retorna un usuario basado en su email")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
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
    public ResponseEntity<String> crearUsuariosMasivos(@RequestParam("file") MultipartFile file) {
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

	@PostMapping("/resetearcontrasenia")
	public String resetearContrasenia(@RequestParam String para) {
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
	
	@PostMapping("/cambiarcontrasenia")
	public String cambiarContrasenia(@RequestParam String mail, @RequestParam String old_pass,
			@RequestParam String new_pass, @RequestParam String new_pass1) {
		
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(mail);

		if (usuario.isPresent()) {

			// Nueva contraseña aleatoria
			String contrasenia = usuario.get().getPassword();// GenerarContraseniaService.generarContraseniaAleatoria();
			if (usuarioService.encriptarSHA256(old_pass).equals(contrasenia)) {
				if (new_pass.equals(new_pass1)) {
					String nuevaContrasenia = usuarioService.encriptarSHA256(new_pass);// new_pass;
					usuario.get().setPassword(nuevaContrasenia);
					usuarioService.actualizar(usuario.get());
					return "Contraseña cambiada";
				} else {
					return "Las nuevas contraseñas no coinciden";
				}
			} else {
				return "la contraseña ingresada no coincide con la registrada en el sistema";
			}
		} else {
			return "No existe el usuario ingresado";
		}
	}

	@PostMapping("/cambiarrol")
	public String cambiarRol(@RequestParam String mail, @RequestParam String rol) {
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(mail);

		if (usuario.isPresent()) {
			System.out.print("Rol actual: " + usuario.get().getRol());
			if (rol.equals("User")) {
				usuario.get().setRol(100);
			} else if (rol.equals("Vendedor")) {
				usuario.get().setRol(200);
			} else if (rol.equals("Admin")) {
				usuario.get().setRol(300);
			} else {
				System.out.print("No existe el Rol ingresado");
				return "no existe el rol ingresado";
			}
			usuarioService.actualizar(usuario.get());
			System.out.print("Nuevo rol: " + usuario.get().getRol());
			return "Rol modificado";
		}
		return "El correo ingresado no existe registrado en el sistema";// + nuevoRol;
	}		
     
}
