package com.example.Login.controller;

import com.example.Login.dto.DtoCambiarContrasenia;
import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.DtoCrearCuenta;
import com.example.Login.dto.DtoRegistrarse;
import com.example.Login.dto.DtoValidarCodigo;
import com.example.Login.model.Usuario;
import com.example.Login.repository.UsuarioRepository;
import com.example.Login.service.CompraPasajeService;
import com.example.Login.service.EmailService;
import com.example.Login.service.GenerarContraseniaService;
import com.example.Login.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/listarTodos")
	@Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de usuarios")

	public List<Usuario> obtenerUsuarios() {
		return usuarioService.obtenerUsuarios();
	}

	@Autowired
	private UsuarioRepository usuariorepository;

	@Autowired
	CompraPasajeService comprarPasajeService;

	@PostMapping("/registrarse")
	@Operation(summary = "Crear un usuario", description = "Agrega un nuevo usuario")
	public ResponseEntity<Map<String, String>> registrarse(@RequestBody DtoRegistrarse registrarse) {
		int rol = 100;
		String token = usuarioService.obtenerToken(registrarse.getEmail(), rol);// dtoValidarCodigo.getCodigo());
		System.out.println("Entre al usuario controller");

		Optional<Usuario> user = usuarioService.buscarPorEmail(registrarse.getEmail());
		Map<String, String> response = new HashMap<>();

		if (user.isPresent()) {
			response.put("error", "El usuario ya se encuentra registrado con ese correo");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		usuarioService.crearUsuario(registrarse);

		// 🔹 Prepara la respuesta exitosa
		response.put("token", token);// "Usuario registrado exitosamente");
		return ResponseEntity.status(HttpStatus.CREATED).body(response); // ✅ 201 - Creado
	}

	@PostMapping("/crearCuenta")
	@Operation(summary = "Crear un usuario", description = "Agrega un nuevo usuario")
	public ResponseEntity<Map<String, String>> crearCuenta(@RequestBody DtoCrearCuenta dtocrearCuenta) {
		System.out.println("Entre al usuario controller");

		Optional<Usuario> user = usuarioService.buscarPorEmail(dtocrearCuenta.getEmail());
		Map<String, String> response = new HashMap<>();

		if (user.isPresent()) {
			response.put("error", "El usuario ya se encuentra registrado con ese correo");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}

		usuarioService.crearCuenta(dtocrearCuenta);
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
		// String token = usuarioService.login(email, password);
		System.out.println("password primero: " + password);
		String ok = "Se le envió a su correo un código para terminar con el proceso de autenticación";
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
		Map<String, String> response = new HashMap<>();

		if (usuario.isPresent()) {
			Usuario usuarioEncontrado = usuario.get();
			if (usuarioEncontrado.getActivo() == true) {
				// 🔹 Compara la contraseña ingresada encriptada con la almacenada en la BD
				if (usuarioService.encriptarSHA256(password).equals(usuarioEncontrado.getPassword())) {
					System.out.println("las contraseñas coinciden en controller");
					// if (token != null) {
					// return ResponseEntity.ok(Map.of("token", token));
					String token = usuarioService.login(email, password);
					return ResponseEntity.ok(Map.of("Mensaje", ok));
				}
			} else {
				response.put("mensaje", "El usuario no se encuentra habilitado, pongase en contacto"
						+ " con un administrador para que lo habilite");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}
		}
		response.put("mensaje", "Credenciales incorrectas");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@PostMapping("/verificarCodigo")
	@Operation(summary = "Login de un usuario con codigo", description = "Permite verificar el codigo enviado a la hora de hacer login")
	public ResponseEntity<Map<String, String>> verificarCodigo(@RequestBody DtoValidarCodigo dtoValidarCodigo) {
		String token = usuarioService.verificarCodigo(dtoValidarCodigo.getEmail(), dtoValidarCodigo.getCodigo());
		if (token != null) {
			usuarioService.vaciarCodigo(dtoValidarCodigo.getEmail());
			return ResponseEntity.ok(Map.of("token", token));
		} else {
			String mensaje = "El codigo ingresado no conicide con el enviado por email";
			// verificar
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", mensaje));
		}
	}

	@PostMapping("/borrarUsuario")
	@Operation(summary = "Borrar un usuario por email", description = "Elimina un usuario de la base de datos por su email")
	public ResponseEntity<Map<String, String>> bajaUsuario(@RequestParam String email) {

		Optional<Usuario> user = usuarioService.buscarPorEmail(email);
		Map<String, String> response = new HashMap<>();

		if (user.isPresent()) {
			response.put("mensaje", "El usuario ha sido dado de baja");
			usuarioService.bajaUsuario(user);// borrarUsuario(usuario);
			return ResponseEntity.ok(response);
		}
		response.put("error", "No existe el usuario");
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al procesar el archivo: " + e.getMessage());
		}
	}

	@GetMapping("/contarUsuarios")
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

	@PostMapping("/cambiarContrasenia")
	public String cambiarContrasenia(@RequestBody DtoCambiarContrasenia cambiarContrasenia) {

		Optional<Usuario> usuario = usuarioService.buscarPorEmail(cambiarContrasenia.getEmail());
		System.out.println("email: " + cambiarContrasenia.getEmail());// usuario.get().getEmail());

		if (usuario.isPresent()) {

			// Nueva contraseña aleatoria
			String contrasenia = usuario.get().getPassword();// GenerarContraseniaService.generarContraseniaAleatoria();
			if (usuarioService.encriptarSHA256(cambiarContrasenia.getOld_pass()).equals(contrasenia)) {
				if (cambiarContrasenia.getNew_pass().equals(cambiarContrasenia.getNew_pass1())) {
					String nuevaContrasenia = usuarioService.encriptarSHA256(cambiarContrasenia.getNew_pass());// new_pass;
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

	@PostMapping("/cambiarRol")
	public String cambiarRol(@RequestParam String email, @RequestParam String rol) {
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);

		if (usuario.isPresent()) {
			System.out.println("Rol actual: " + usuario.get().getRol());
			if (rol.equals("User")) {
				usuario.get().setRol(100);
				usuario.get().setCod_empleado(null);
			} else if (rol.equals("Vendedor")) {
				usuario.get().setRol(200);
				if (usuario.get().getCod_empleado() == 0) {
					try {
						usuario.get().setCod_empleado(usuariorepository.findMaxCodEmpleado() + 1);
					} catch (Exception e) {
						usuario.get().setCod_empleado(100);
					}
				}
			} else if (rol.equals("Admin")) {
				usuario.get().setRol(300);
				if (usuario.get().getCod_empleado() == 0) {
					try {
						usuario.get().setCod_empleado(usuariorepository.findMaxCodEmpleado() + 1);
					} catch (Exception e) {
						usuario.get().setCod_empleado(100);
					}
				}
			} else {
				System.out.println("No existe el Rol ingresado");
				return "no existe el rol ingresado";
			}
			usuarioService.actualizar(usuario.get());
			System.out.print("Nuevo rol: " + usuario.get().getRol());
			return "Rol modificado";
		}
		return "El correo ingresado no existe registrado en el sistema";// + nuevoRol;
	}

	@PostMapping("/comprarPasaje")
	public ResponseEntity<String> comprarPasaje(@RequestBody DtoCompraPasaje dtoComprarPasaje) {
		int resultadoCompra = comprarPasajeService.comprarPasaje(dtoComprarPasaje);
		switch (resultadoCompra) {
		case 1:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El cliente ingresado no existe");
		case 2:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Uno de los asientos solicitados ya se encuentra reservado");
		case 3:
			return ResponseEntity.status(HttpStatus.OK).body("La compra ha sido realizada de forma correcta");
		case 4:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"El cliente ingresado no se encuentra habilitado, por lo tanto" + " no puede realizar compras");
		case 5:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El viaje ingresado no existe");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error desconocido");
	}

	@PostMapping("/cancelarCompra")
	public ResponseEntity<String> cancelarCompra(@RequestParam int idCompra) {
		int resultado = comprarPasajeService.cancelarCompra(idCompra);

		switch (resultado) {
		case 1:
			return ResponseEntity.status(HttpStatus.OK).body("La compra ha sido cancelada");
		case 2:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("La compra no se puede cancelar porque ya se encuentra cancelada");
		case 3:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El id de compra ingresado no existe");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error desconocido");
	}

	@PostMapping("/reenviarCodigo")
	@Operation(summary = "Reenviar código para autenticar", description = "Retorna un codigo")
	public ResponseEntity<String> reenviarCodigo(String email) {
		int codigo = usuarioService.reenviarCodigo(email);
		System.out.println("codigo de verificacion controller: " + codigo);
		String para = email;// usuario.get().getEmail();
		String asunto = "Código de autorización";
		String mensaje = "utilize el siguiente código: " + codigo + " para iniciar sesión en el sistema";
		// + usuario.get().getCodigo() + " para iniciar sesión en el sistema ";
		if (codigo > 2) {
			emailService.enviarCorreo(para, asunto, mensaje);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Se le envió a su correo un código para terminar con el proceso de autenticación");
		} else if (codigo == 2) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Usted no ha iniciado sesion aun, por favor inicie sesion en nuestro sistema");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error desconocido");
		}
	}
}
