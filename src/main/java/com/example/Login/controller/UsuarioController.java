package com.example.Login.controller;

import com.example.Login.dto.DtoCambiarContrasenia;
import com.example.Login.dto.DtoCantidadPorRol;
import com.example.Login.dto.DtoCantidadPorRolQuery;
import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.DtoCrearCuenta;
import com.example.Login.dto.DtoMisCompras;
import com.example.Login.dto.DtoMisViajes;
import com.example.Login.dto.DtoNewUsuariosPorMes;
import com.example.Login.dto.DtoRegistrarse;
import com.example.Login.dto.DtoRespuestaCompraPasaje;
import com.example.Login.dto.DtoUsuario;
import com.example.Login.dto.DtoUsuarioMensaje;
import com.example.Login.dto.DtoUsuarioPerfil;
import com.example.Login.dto.DtoUsuariosActivos;
import com.example.Login.dto.DtoUsuariosPorCategoria;
import com.example.Login.dto.DtoUsuariosPorEdad;
import com.example.Login.dto.DtoUsuariosPorRol;
import com.example.Login.dto.DtoValidarCodigo;
import com.example.Login.dto.DtoVenderPasaje;
import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.EstadoCompra;
import com.example.Login.dto.categoriaUsuario;
import com.example.Login.model.Usuario;
import com.example.Login.repository.UsuarioRepository;
import com.example.Login.service.CompraPasajeService;
import com.example.Login.service.EmailService;
import com.example.Login.service.GenerarContraseniaService;
import com.example.Login.service.TokenService;
import com.example.Login.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "API para gestionar usuarios")

public class UsuarioController {

	private final UsuarioService usuarioService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private EmailService emailService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping("/listarTodos")
	@Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de usuarios")

	public List<DtoUsuario> obtenerUsuarios() {
		return usuarioService.obtenerUsuarios();
	}

	@Autowired
	private UsuarioRepository usuariorepository;

	@Autowired
	CompraPasajeService comprarPasajeService;

	@PostMapping("/registrarse")
	@Operation(summary = "Crear un usuario", description = "Agrega un nuevo usuario")
	// public ResponseEntity<String> registrarse(@RequestBody DtoRegistrarse
	// registrarse) {
	ResponseEntity<Map<String, String>> registrarse(@RequestBody DtoRegistrarse registrarse) {

		Map<String, String> response = new HashMap<>();
		int rol = 100;
		int id = 0;
		try {
			Optional<Usuario> Ousuario = usuariorepository.findByEmail(registrarse.getEmail());
			id = Ousuario.get().getId();
		} catch (Exception e) {
			// TODO: handle exception
		}

		Optional<Usuario> user = usuarioService.buscarPorEmail(registrarse.getEmail());
		if (user.isPresent()) {
			response.put("mensaje", "El usuario ya se encuentra registrado con ese correo");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);// "El usuario ya se encuentra registrado
																				// con ese correo");
		} else {
			int resultado = usuarioService.registrarse(registrarse);
			if (resultado == 1) {
				usuarioService.enviarMailRegistrarse(registrarse);
				response.put("mensaje",
						"Se le ha enviado un correo electrónico con un código para poder validar el registro");
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			} else if (resultado == 0) {
				response.put("mensaje",
						"No se le puede asignar la categoria de JUBILADO ya que usted tiene menos de 60 anios");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				response.put("mensaje", "La cedula no puede estar vacia");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

		}
	}

	@PostMapping("/crearCuenta")
	@Operation(summary = "Crear un usuario", description = "Agrega un nuevo usuario")
	public ResponseEntity<Map<String, String>> crearCuenta(@RequestBody DtoCrearCuenta dtocrearCuenta) {
		System.out.println("Entre al usuario controller");

		Optional<Usuario> user = usuarioService.buscarPorEmail(dtocrearCuenta.getEmail());
		Map<String, String> response = new HashMap<>();

		if (usuarioService.crearCuenta(dtocrearCuenta) == 0) {
			response.put("error", "El usuario ya se encuentra registrado con ese correo");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		} else {
			response.put("mensaje", "Usuario registrado exitosamente");
			return ResponseEntity.status(HttpStatus.CREATED).body(response); // ✅ 201 - Creado
		}
	}

	@GetMapping("/emails/")
	@Operation(summary = "Obtener un usuario por email", description = "Retorna un usuario basado en su email")
	public ResponseEntity<Map<String, Object>> buscarPorEmails(@RequestParam String email) {
		Map<String, Object> response = new HashMap<>();
		DtoUsuarioMensaje usuario = usuarioService.buscarPorEmails(email);

		if (usuario.getResultado() == 1) {
			System.out.println("Usuario encontrado");
			response.put("OK", usuario.getDtoUsuario());
			return ResponseEntity.status(HttpStatus.OK).body(response);
			// return usuario.getDtoUsuario();
		} else {
			response.put("Error", "No existe un usuario ingresado con ese correo");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		// return null;
	}

	// 🔹 Recibe email y password en el cuerpo de la petición (POST)
	@PostMapping("/login")
	@Operation(summary = "Login de un usuario", description = "Permite el inicio de sesión de un usuario con email y contraseña")
	public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
		String email = loginRequest.get("email");
		String password = loginRequest.get("password");
		String ok = "Se le envió a su correo un código para terminar con el proceso de autenticación";
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
		Map<String, String> response = new HashMap<>();

		if (usuario.isPresent()) {
			Usuario usuarioEncontrado = usuario.get();
			if (usuarioEncontrado.getActivo() == true) {
				if (!usuarioEncontrado.isContraseniaValida()) {
					response.put("mensaje", "Debe de cambiar la contraseña para iniciar sesion");
					response.put("Login_directo", "0");
					return ResponseEntity.status(HttpStatus.OK).body(response);
				}
				if (usuarioService.encriptarSHA256(password).equals(usuarioEncontrado.getPassword())) {
					usuarioService.login(email, password);
					response.put("mensaje",
							"Se le envió a su correo un código para terminar con el proceso de autenticación");
					response.put("Login_directo", "1");
					return ResponseEntity.status(HttpStatus.OK).body(response);
				}
			} else {
				response.put("mensaje", "El usuario no se encuentra habilitado, pongase en contacto"
						+ " con un administrador para que lo habilite");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
			String mensaje = "El codigo ingresado ya fue utilizado o no conicide con el enviado por email";
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

	@PostMapping("/activarUsuario")
	public ResponseEntity<Map<String, String>> activarUsuario(@RequestParam String email) {

		Optional<Usuario> user = usuarioService.buscarPorEmail(email);
		Map<String, String> response = new HashMap<>();

		if (user.isPresent()) {
			response.put("mensaje", "El usuario ha sido activado");
			usuarioService.activarUsuario(user);// borrarUsuario(usuario);
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

	@PostMapping("/resetearcontrasenia")
	public ResponseEntity<Map<String, String>> resetearContrasenia(@RequestParam String para) {
		Map<String, String> response = new HashMap<>();

		int resultado = usuarioService.resetearContraseña(para);

		if (resultado == 0) {
			response.put("error", "No existe un usuario registrado con ese correo");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} else {
			response.put("Mensaje", "Se le ha enviado por correo una nueva contraseña temporal");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	@PostMapping("/cambiarContrasenia")
	public ResponseEntity<Map<String, String>> cambiarContrasenia(
			@RequestBody DtoCambiarContrasenia cambiarContrasenia) {
		Map<String, String> response = new HashMap<>();
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(cambiarContrasenia.getEmail());
		System.out.println("email: " + cambiarContrasenia.getEmail());// usuario.get().getEmail());

		if (usuario.isPresent()) {

			// Nueva contraseña aleatoria
			String contrasenia = usuario.get().getPassword();// GenerarContraseniaService.generarContraseniaAleatoria();
			if (usuarioService.encriptarSHA256(cambiarContrasenia.getOld_pass()).equals(contrasenia)) {
				if (cambiarContrasenia.getNew_pass().equals(cambiarContrasenia.getNew_pass1())) {
					String nuevaContrasenia = usuarioService.encriptarSHA256(cambiarContrasenia.getNew_pass());// new_pass;
					usuario.get().setPassword(nuevaContrasenia);
					usuario.get().setContraseniaValida(true);
					usuarioService.actualizar(usuario.get());
					response.put("mensaje", "Contraseña cambiada");// nuevaContrasenia)
					return ResponseEntity.status(HttpStatus.OK).body(response);
				} else {
					response.put("mensaje", "Las nuevas contraseñas no coinciden");// nuevaContrasenia)
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
					// return "Las nuevas contraseñas no coinciden";
				}
			} else {
				response.put("mensaje", "La contraseña ingresada no coincide con la registrada en el sistema");// nuevaContrasenia)
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
				// return "la contraseña ingresada no coincide con la registrada en el sistema";
			}
		} else {
			response.put("mensaje", "No existe el usuario ingresado");// nuevaContrasenia)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			// return "No existe el usuario ingresado";
		}
	}

	@PostMapping("/cambiarRol")
	public ResponseEntity<Map<String, String>> cambiarRol(@RequestParam String email, @RequestParam String rol) {
		Map<String, String> response = new HashMap<>();
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
		int resultado = usuarioService.cambiarRol(email, rol);

		switch (resultado) {
		case 0:
			response.put("mensaje", "No existe el rol ingresado");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		// break;
		case 1:
			System.out.print("Nuevo rol: " + usuario.get().getRol());
			response.put("mensaje", "Rol modificado");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		// break;
		case 2:
			response.put("mensaje", "El correo ingresado no existe registrado en el sistema");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		// break;
		}
		response.put("error", "Error desconocido");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@PostMapping("/cambiarCategoria")
	public ResponseEntity<Map<String, String>> cambiarCategoria(@RequestParam String email,
			@RequestParam String categoria) {
		Map<String, String> response = new HashMap<>();
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);

		if (usuario.isPresent() && usuario.get().getRol() == 100) {
			System.out.println("categoria actual: " + usuario.get().getCategoria());
			if (categoria.equals("GENERAL")) {
				usuario.get().setCategoria(categoriaUsuario.GENERAL);
				usuarioService.actualizar(usuario.get());
				response.put("mensaje", "Categoria cambiada con exito");
				System.out.println("Nueva categoria: " + usuario.get().getCategoria());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else if (categoria.equals("ESTUDIANTE")) {
				usuario.get().setCategoria(categoriaUsuario.ESTUDIANTE);
				usuarioService.actualizar(usuario.get());
				response.put("mensaje", "Categoria cambiada con exito");
				System.out.println("Nueva categoria: " + usuario.get().getCategoria());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else if (categoria.equals("JUBILADO")) {
				usuario.get().setCategoria(categoriaUsuario.JUBILADO);
				usuarioService.actualizar(usuario.get());
				response.put("mensaje", "Categoria cambiada con exito");
				System.out.println("Nueva categoria: " + usuario.get().getCategoria());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				System.out.println("Categoria invalida");
				response.put("mensaje", "Categoria invalida");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		} else {
			response.put("mensaje", "Solo se le puede cambiar la categoria si el usuario es USUARIO FINAL");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/comprarPasaje")
	public ResponseEntity<Map<String, Object>> comprarPasaje(@RequestBody DtoCompraPasaje dtoComprarPasaje) {
		DtoRespuestaCompraPasaje resultado = comprarPasajeService.comprarPasaje(dtoComprarPasaje);
		EstadoCompra estado = resultado.getEstado();
		List<Integer> asientos = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();

		if (resultado.getAsientosComprados() > 5) {
			response.put("error", "No se pueden comprar más de 5 pasajes por viaje");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		try {
			asientos = resultado.getAsientosOcupados();
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (!resultado.getAsientosOcupados().isEmpty()) {
			// Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Alguno de el/los asiento/s solicitado/s ya se encuentra/n reservado/s");
			response.put("asientosOcupados", asientos);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} else {
			switch (estado) {
			case REALIZADA:
				response.put("mensaje", "La compra ha sido realizada de forma exitosa");
				response.put("descuento",resultado.getDescuento());
				usuarioService.enviarMailCompraPasaje(dtoComprarPasaje);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			case RESERVADA:
				response.put("mensaje",
						"La compra ha sido reservada de forma exitosa. Recurede"
								+ " que tiene 10 minutos para completar el proceso de compra, de lo contrario su "
								+ "reserva sera cancelada de forma automatica");
				response.put("idCompra", resultado.getIdCompra());
				response.put("descuento",resultado.getDescuento());
				usuarioService.enviarMailReservarPasaje(dtoComprarPasaje);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			case CANCELADA:
				response.put("mensaje", "No se pueden comprar pasajesa a viajes cancelados o cerrados");
				// usuarioService.enviarMailCompraPasaje(dtoComprarPasaje);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		}

		response.put("mensaje", "Error desconocido");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PostMapping("/venderPasaje")
	public ResponseEntity<Map<String, String>> venderPasaje(@RequestBody DtoVenderPasaje dtoVenderPasaje) {
		Map<String, String> response = new HashMap<>();
		int resultadoCompra = comprarPasajeService.venderPasaje(dtoVenderPasaje);
		switch (resultadoCompra) {
		case 1:
			response.put("mensaje", "El vendedor ingresado no existe");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El
		// Vendedor ingresado no existe");
		case 2:
			response.put("mensaje", "Uno de los asientos solicitados ya se encuentra reservado");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		// .body("Uno de los asientos solicitados ya se encuentra reservado");
		case 3:
			usuarioService.enviarMailVenderPasaje(dtoVenderPasaje);
			response.put("mensaje", "La venta ha sido realizada de forma correcta");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		// return ResponseEntity.status(HttpStatus.OK).body("La venta ha sido realizada
		// de forma correcta");
		case 4:
			response.put("mensaje",
					"El vendedor ingresado no se encuentra habilitado, por lo tanto no puede realizar ventas");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		/// .body("El vendedor ingresado no se encuentra habilitado, por lo tanto no
		// puede realizar ventas");
		case 5:
			response.put("mensaje", "El viaje ingresado no existe");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El viaje
		// ingresado no existe");
		case 6:
			response.put("mensaje", "La compra ha sido reservada de forma exitosa");
			// usuarioService.enviarMailReservarPasaje(dtoVenderPasaje);
			return ResponseEntity.status(HttpStatus.OK).body(response);

		// return ResponseEntity.status(HttpStatus.OK).body("La compra ha sido reservada
		// de forma correcta");
		case 8:
			response.put("mensaje", "Solo los vendedores pueden vender pasajes");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		// usuarioService.enviarMailReservarPasaje(dtoVenderPasaje);
		// return ResponseEntity.status(HttpStatus.OK).body("Solo los vendedores pueden
		// vender pasajes");

		}
		response.put("mensaje", "Error desconosido");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(//"Error
		// desconocido");
	}

	@PostMapping("/cancelarCompra")
	public ResponseEntity<Map<String, String>> cancelarCompra(@RequestParam int idCompra) {
		Map<String, String> response = new HashMap<>();
		int resultado = comprarPasajeService.cancelarCompra(idCompra);

		switch (resultado) {
		case 1:
			usuarioService.enviarMailCancelarCompra(idCompra);
			response.put("mensaje", "La compra ha sido cancelada");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		// return ResponseEntity.status(HttpStatus.OK).body("La compra ha sido
		// cancelada");
		case 2:
			response.put("mensaje", "La compra no se puede cancelar porque ya se encuentra cancelada");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//
//					.body("La compra no se puede cancelar porque ya se encuentra cancelada");
		case 3:
			response.put("mensaje", "El id de compra ingresado no existe");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El id de compra
		// ingresado no existe");
		case 4:
			response.put("mensaje", "No se puede cancelar la compra dado que el viaje comienza en menos de una hora");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
		}
		response.put("mensaje", "Error desconocido");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error
		// desconocido");
	}

	@PostMapping("/cambiarEstadoCompra")
	public ResponseEntity<Map<String, String>> cambiarEstadoCompra(@RequestBody int idCompra) {
		Map<String, String> response = new HashMap<>();
		int resultado = usuarioService.cambiarEstadoCompra(idCompra);

		if (resultado == 1) {
			response.put("mensaje", "Se cambió el estado de la compra");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("error", "No se pudo cambiar el estado de la compra porque no existe");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	@PostMapping("/reenviarCodigo")
	@Operation(summary = "Reenviar código para autenticar", description = "Retorna un codigo")
	public ResponseEntity<Map<String, String>> reenviarCodigo(String email) {
		Map<String, String> response = new HashMap<>();
		int codigo = usuarioService.obtenerCodigo(email);

		if (codigo > 2) {
			usuarioService.enviarMailReenviarCodigo(email);
			response.put("mensaje", "Se le envió a su correo un código para terminar con el proceso de autenticación");
			return ResponseEntity.status(HttpStatus.OK).body(response);
			// return ResponseEntity.status(HttpStatus.OK).body("Se le envió a su correo un
			// código para terminar con el proceso de autenticación");
		} else if (codigo == 2) {
			response.put("mensaje", "Usted no ha iniciado sesion aun, por favor inicie sesión en nuestro sistema");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			// return ResponseEntity.status(HttpStatus.UNAUTHORIZED) Usted no ha iniciado
			// sesion aun, por favor inicie sesion en nuestro sistema");
		} else {
			response.put("mensaje", "Error desconocido");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error
			// desconocido");
		}
	}

	@PostMapping("/ObtenerMisViajes")
	@Operation(summary = "Obtener los viajes de un usuario", description = "Obtener los viajes de un usuaio")
	public List<DtoMisViajes> obtenerMisViajes(@RequestParam String email) {
		List<DtoMisViajes> misViajes = new ArrayList<>();
		misViajes = usuarioService.obtenerMisViajes(email);
		return misViajes;
	}

	@PostMapping("/ObtenerMisCompras")
	public List<DtoMisCompras> obtenerMisCompras(@RequestParam String email) {
		List<DtoMisCompras> misCompras = new ArrayList<>();
		misCompras = usuarioService.obtenerMisCompras(email);
		return misCompras;
	}

	@PostMapping("/ObtenerMisReservas")
	public List<DtoMisCompras> obtenerMisReservas(@RequestParam String email) {
		List<DtoMisCompras> misCompras = new ArrayList<>();
		misCompras = usuarioService.obtenerMisReservas(email);
		return misCompras;
	}

	@GetMapping("/buscarUsuarioPorId")
	public DtoUsuario buscarUsuariosPorId(@RequestParam int idUsuario) {
		DtoUsuario dtousuario = new DtoUsuario();
		dtousuario = usuarioService.buscarPorId(idUsuario);
		return dtousuario;
	}

	@PostMapping("/modificarPerfil")
	public ResponseEntity<Map<String, String>> modificarPerfil(@RequestBody DtoUsuarioPerfil usuario) {
		System.out.println("emailController: " + usuario.getEmail());
		Map<String, String> response = new HashMap<>();
		boolean resultado = usuarioService.modificarPerfil(usuario);

		if (resultado) {
			response.put("mensaje", "Perfil modificado de forma exitosa");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("mensaje", "Usuario no encontrado");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("usuariosPorMes")
	public List<DtoNewUsuariosPorMes> obtenerUsuariosPorMes() {
		List<DtoNewUsuariosPorMes> resultado = new ArrayList<>();
		resultado = usuarioService.obtenerUsuariosPorMes();
		return resultado;
	}

	@GetMapping("usuariosPorRol")
	public List<DtoUsuariosPorRol> obtenerUsuariosPorRol() {
		List<DtoUsuariosPorRol> resultado = new ArrayList<>();
		resultado = usuarioService.obtenerUsuariosPorRol();
		return resultado;
	}
	
	@GetMapping("usuariosPorEdad")
	public List<DtoUsuariosPorEdad> obtenerUsuariosPorEdad() {
		List<DtoUsuariosPorEdad> resultado = new ArrayList<>();
		resultado = usuarioService.obtenerUsuariosPorEdad();
		return resultado;
	}

	@PostMapping("/pobarPushNotification")
	public void probarPushNotification(@RequestParam String token) {
		String titulo = "prueba";
		String cuerpo = "mensaje";
		try {
			tokenService.enviarPushNotification(token, titulo, cuerpo);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@GetMapping("/cantidadUsuarios-rolesCreadosPorAdmin")
	List<DtoCantidadPorRol> cantidadPorRol() {
		return usuarioService.cantidadPorRol();
	}
	
	@GetMapping("usuariosActivos")
	public DtoUsuariosActivos obtenerUsuariosActivos() {
		//List<DtoUsuariosPorEdad> resultado = new ArrayList<>();
		DtoUsuariosActivos resultado  = usuarioService.obtenerUsuariosActivos();
		return resultado;
	}
	
	@GetMapping("usuariosPorCategoria")
	public List<DtoUsuariosPorCategoria> obtenerUsuariosPorCategoria() {
		//List<DtoUsuariosPorEdad> resultado = new ArrayList<>();
		List<DtoUsuariosPorCategoria> resultado  = usuarioService.obtenerUsuariosPorCategoria();
		return resultado;
	}


}
