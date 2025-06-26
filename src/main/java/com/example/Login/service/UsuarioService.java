package com.example.Login.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoCantidadPorRol;
import com.example.Login.dto.DtoCantidadPorRolQuery;
import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.DtoCrearCuenta;
import com.example.Login.dto.DtoMisCompras;
import com.example.Login.dto.DtoMisViajes;
import com.example.Login.dto.DtoNewUsuariosPorMes;
import com.example.Login.dto.DtoRegistrarse;
import com.example.Login.dto.DtoUsuario;
import com.example.Login.dto.DtoUsuarioMensaje;
import com.example.Login.dto.DtoUsuarioPerfil;
import com.example.Login.dto.DtoUsuariosActivos;
import com.example.Login.dto.DtoUsuariosPorCategoria;
import com.example.Login.dto.DtoUsuariosPorEdad;
import com.example.Login.dto.DtoUsuariosPorRol;
import com.example.Login.dto.DtoUsuariosPorRolQuery;
import com.example.Login.dto.DtoVenderPasaje;
import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.EstadoCompra;
import com.example.Login.dto.categoriaUsuario;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.Categoria;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Usuario;
import com.example.Login.model.Viaje;
import com.example.Login.repository.CategoriaRepository;
import com.example.Login.repository.CompraPasajeRepository;
import com.example.Login.repository.UsuarioRepository;
import com.example.Login.repository.ViajeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.function.text.Concatenate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

	@Autowired
	private final UsuarioRepository usuarioRepository;

	@Autowired
	private ViajeRepository viajeRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private CompraPasajeRepository comprapasajerepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private EmailService emailService;

	// private final BCryptPasswordEncoder passwordEncoder = new
	// BCryptPasswordEncoder();

	// Inyección de dependencias
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
		// this.viajeRepository = ;
	}

	// Generar codigo de 5 digitos
	public int generarCodigo() {
		Random random = new Random();
		return random.nextInt(99999 - 11111 + 1) + 11111;
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

	public void actualizar(Usuario usuario) {
		usuarioRepository.save(usuario); // esto hace un UPDATE si el usuario existe
	}

//	public List<Usuario> obtenerUsuarios() {
//		return usuarioRepository.findAll();
//	}

	public List<DtoUsuario> obtenerUsuarios() {
		List<DtoUsuario> resultado = new ArrayList<>();
		List<Usuario> usuario = usuarioRepository.findAll();
		
		for(Usuario u: usuario) {
			DtoUsuario user = new DtoUsuario();
			user.setActivo(u.getActivo());
			user.setApellido(u.getApellido());
			user.setCategoria(u.getCategoria());
			user.setCi(u.getCi());
			user.setCod_empleado(u.getCod_empleado());
			user.setEmail(u.getEmail());
			user.setFechaNac(u.getFechaNac());
			user.setId(u.getId());
			user.setNombre(u.getNombre());
			user.setRol(u.getRol());
			
			resultado.add(user);
		}		
		return resultado;
	}
	
	

	// Guardar usuario con contraseña encriptada
	public int registrarse(DtoRegistrarse registrarse) {
		System.out.println("Entre al usuario service");

		LocalDate fechaNacimiento = registrarse.getFecha_nac();
		LocalDate fechaActual = LocalDate.now();
		int edad = Period.between(fechaNacimiento, fechaActual).getYears();

		Usuario usuario = new Usuario();
		if (registrarse.getCi() == "") {
			return 2;
		} else {
			usuario.setCi(registrarse.getCi());
		}
		categoriaUsuario categoria = registrarse.getCategoria();
		System.out.println("categoria: " + categoria);
		System.out.println("edad: " + edad);
		usuario.setCategoria(categoria);
		usuario.setNombre(registrarse.getNombre());
		usuario.setApellido(registrarse.getApellido());
		usuario.setEmail(registrarse.getEmail());
		usuario.setPassword(encriptarSHA256(registrarse.getPassword()));
		usuario.setRol(100);
		usuario.setActivo(true);
		usuario.setFechaCreacion(LocalDate.now());
		usuario.setCodigo(generarCodigo());
		usuario.setContraseniaValida(true);
		usuario.setCategoria(categoriaUsuario.GENERAL);
		
		//Agregar la fecha de nacimiento al usuario
		//LocalDate fechaNacimiento = registrarse.getFecha_nac();
		java.sql.Date fechaNacimientoSql = java.sql.Date.valueOf(fechaNacimiento);
		usuario.setFechaNac(fechaNacimientoSql);		
		usuarioRepository.save(usuario);
		return 1;
	}

	// Guardar usuario con contraseña encriptada
	public int crearCuenta(DtoCrearCuenta dtocrearCuenta) {
		
		Usuario usuario = new Usuario();
		Integer rol = 0;
		System.out.println("Rol del usuario agregado = " + dtocrearCuenta.getRol());

		switch (dtocrearCuenta.getRol()) {
		case "User":
			rol = null;
			break;
		case "Vendedor":
			rol = 200;
			break;
		case "Admin":
			rol = 300;
			break;
		}

		if (rol != 100 && rol != 0) {
			int setCod_empleado = (usuarioRepository.findMaxCodEmpleado());
			System.out.println("max cod emp: " + setCod_empleado);
			try {
				usuario.setCod_empleado(usuarioRepository.findMaxCodEmpleado() + 1);
			} catch (Exception e) {
				usuario.setCod_empleado(100);
			}
		}
		String nombre = dtocrearCuenta.getNombre().toLowerCase();
		String apellido = dtocrearCuenta.getApellido().toLowerCase();
		usuario.setNombre(dtocrearCuenta.getNombre());
		usuario.setApellido(dtocrearCuenta.getApellido());
		//usuario.setEmail(dtocrearCuenta.getEmail());
		
		usuario.setEmail(nombre + "." + apellido + "@tecnobus.uy");
		
		try {
			Optional<Usuario> Ousuario = usuarioRepository.findByEmail(usuario.getEmail());
			if(Ousuario.isPresent()) {
				return 0;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		usuario.setCategoria(dtocrearCuenta.getCategoria());
		usuario.setCi(dtocrearCuenta.getCi());
		usuario.setFechaNac(dtocrearCuenta.getFechaNac());
		usuario.setRol(rol);
		System.out.println("Rol del usuario agregado = " + usuario.getRol());
		usuario.setActivo(true);
		usuario.setFechaCreacion(LocalDate.now());
		String password = usuario.getApellido() + usuario.getNombre() + "_2025";
		System.out.println("Contraseña: " + password);
		usuario.setPassword(encriptarSHA256(password));
		usuario.setContraseniaValida(false);
		
		enviarMailCrearCuenta(usuario);
		usuarioRepository.save(usuario);
		return 1;
	}
	
	public void enviarMailCrearCuenta(Usuario usuario) {
		String para = usuario.getEmail();
		System.out.println("dirección de envio de mail: " +  para);
		String asunto = "Contrasenia de inicio de sesion";
		String mensaje = "Bienvenido " + usuario.getNombre() + " usted ha sido dado de alta en nuestro sistema."
				+ " La contraseña temporal para acceder es  " + usuario.getApellido() + usuario.getNombre() + "_2025"
				+ "  no se olvide de cambiarla una vez que haya ingresado";
		emailService.enviarCorreo(para, asunto, mensaje);
	}

	public Usuario bajaUsuario(Optional<Usuario> user) {
		Usuario u = user.get();
		u.setActivo(false);
		usuarioRepository.save(u);
		return u;
	}

	public String convertCsvToJson(MultipartFile file) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		String line;
		List<Map<String, String>> dataList = new ArrayList<>();

		try {
			while ((line = reader.readLine()) != null) {
				Usuario user = new Usuario();
				Optional<Usuario> usuarioNoEncontrado;// = usuarioService.buscarPorEmail(email);
				String[] values = line.split(":"); // Suponiendo que el delimitador es ":"

				// Validamos que haya exactamente 9 valores por línea
				if (values.length != 9) {
					throw new Exception("Formato incorrecto en el CSV. Cada fila debe tener 9 valores.");
				}

				// Construimos el JSON correctamente
				Map<String, String> row = new HashMap<>();
				String estado = values[5].toLowerCase();
				row.put("nombre", values[0]);
				row.put("Apellido", values[1]);
				row.put("email", values[2]);
				row.put("password", values[3]);
				row.put("rol", values[4]);
				row.put("activo", values[5]);
				row.put("categoria", values[6]);
				row.put("ci", values[7]);
				row.put("fechaNac", values[8]);

				user.setNombre(values[0]);
				user.setApellido(values[1]);
				//user.setEmail(values[2]);
				System.out.println("correo: " + values[2]);
				user.setPassword(values[3]);
				user.setPassword(encriptarSHA256(user.getPassword()));

				if (values[4].equals("User")) {
					user.setRol(100);
					user.setCod_empleado(null);
				} else if (values[4].equals("Vendedor")) {
					user.setRol(200);
					System.out.println("max codEmp: " + usuarioRepository.findMaxCodEmpleado());
					try {
						user.setCod_empleado(usuarioRepository.findMaxCodEmpleado() + 1);
					} catch (Exception e) {
						user.setCod_empleado(100);
					}
				} else if (values[4].equals("Admin")) {
					user.setRol(300);
					System.out.println("max codEmp: " + usuarioRepository.findMaxCodEmpleado());
					try {
						user.setCod_empleado(usuarioRepository.findMaxCodEmpleado() + 1);
					} catch (Exception e) {
						user.setCod_empleado(100);
					}
				} else {
					System.out.println("No existe el Rol: " + values[4]);
				}
				if (values[5].equals("true")) {
					user.setActivo(true);
				} else {
					user.setActivo(false);
				}
				String categoria = values[6];
				switch (categoria) {
				case "GENERAL":
					user.setCategoria(categoriaUsuario.GENERAL);
					break;
				case "JUBILADO":
					user.setCategoria(categoriaUsuario.JUBILADO);
					break;
				case "ESTUDIANTE":
					user.setCategoria(categoriaUsuario.ESTUDIANTE);
					break;
				}
				// user.setCategoria(values[6]);
				user.setCi(values[7]);
				user.setFechaCreacion(LocalDate.now());
				String nombre = user.getNombre().toLowerCase();
				String apellido = user.getApellido().toLowerCase();
				
				user.setEmail(nombre + "." + apellido + "@tecnobus.uy");

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaNacimiento = sdf.parse(values[8]);

				// Conversión segura:
				java.sql.Date sqlFechaNacimiento = new java.sql.Date(fechaNacimiento.getTime());

				//dataList.add(row);

				usuarioNoEncontrado = buscarPorEmail(user.getEmail());
				if (usuarioNoEncontrado.isEmpty()) {
					System.out.println("Usuario no encontrado con el correo " + user.getEmail());
					//DtoRegistrarse reg = new DtoRegistrarse();
					DtoCrearCuenta cuenta = new DtoCrearCuenta();
					cuenta.setApellido(user.getApellido());
					cuenta.setCategoria(user.getCategoria());
					cuenta.setCi(user.getCi());
					cuenta.setEmail(user.getEmail());
					cuenta.setFechaNac(user.getFechaNac());
					cuenta.setNombre(user.getNombre());
					cuenta.setRol(values[4]);//user.getRol());
					crearCuenta(cuenta);
					
					//enviarMailCrearCuenta(user);
					//enviarMailRegistrarse(null);
					// Guardo el usuario nuevo
					
					
					//usuarioRepository.save(user);
					System.out.println("El usuario fue registrado");
				} else {
					Map<String, String> usuarioExistente = new HashMap<>();
					usuarioExistente.put("error","el usuario de correo " + user.getEmail() + " ya se encuentra registrado");
					System.out.println("El correo : " + values[2] + " ya esta registrado en el sistema");
					dataList.add(usuarioExistente);
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

	public String login(String email, String password) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		System.out.println("encontró usuario en service ");

		if (usuario.isPresent() && encriptarSHA256(password).equals(usuario.get().getPassword())) {
			System.out.println("Las contraseñas coinciden");

			// le cargo el codigo generado al usuario que se autentico
			usuario.get().setCodigo(generarCodigo());
			usuarioRepository.save(usuario.get());
			String nombre = usuario.get().getNombre();
			int codigo = usuario.get().getCodigo();
			String para = usuario.get().getEmail();
			String asunto = "Código de autorización";
			String mensaje = String.format("<p>Bienvenido <b>%s</b>,</p>"
					+ "<p>Utilice el siguiente código <b>%s</b> para iniciar sesión en el sistema.</p>" + "<br>"
					+ "<p>Saludos,</p>" + "<p><strong>Tecnobus</strong><br>" + "Uruguay<br>", nombre, codigo);

			emailService.enviarCorreo(para, asunto, mensaje);

			System.out.println("codigo: " + usuario.get().getCodigo());
			// return jwtService.generateToken(email, rol);
			return "OK";
		} else {
			System.out.println("retorna null");
			return null;
		}
	}

	public String verificarCodigo(String email, int codigo) {
		Usuario usuario = new Usuario();
		try {
			Optional<Usuario> Ousuario = usuarioRepository.findByEmail(email);
			usuario = Ousuario.get();
		} catch (Exception e) {
			// TODO: handle exception
		}
		int id = usuario.getId();
		int rol = usuario.getRol();
		String nombre = usuario.getNombre();
		// int id = usuario.get().getId();
		if (usuario.getCodigo() == codigo) {
			return jwtService.generateToken(email, rol, id, nombre);
		} else {
			System.out.println("El código ingresado no coincide con el enviado por email");
			return null;
		}
	}

	public void vaciarCodigo(String email) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		usuario.get().setCodigo(0);
		usuarioRepository.save(usuario.get());
	}

	public long verCantidadUsuarios() {
		return usuarioRepository.count();
	}

	public String obtenerToken(String email, int rol, int id, String nombre) {
		String token = null;
		token = jwtService.generateToken(email, rol, id, nombre);
		return token;
	}

	public int obtenerCodigo(String email) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		int codigo = usuario.get().getCodigo();
		System.out.println("codigo de verificacion service: " + codigo);
		if (codigo != 0) {
			return codigo;
		} else {
			return 2;
		}
	}

	public void enviarMailRegistrarse(DtoRegistrarse registrarse) {
		String email = registrarse.getEmail();
		int codigo = obtenerCodigo(email);
		String para = email;
		String asunto = "Código de autorización";
		String mensaje = "utilize el siguiente código: " + codigo + " para iniciar sesión en el sistema";
		emailService.enviarCorreo(para, asunto, mensaje);
	}

	public void enviarMailCompraPasaje(DtoCompraPasaje compraPasaje) {
		Optional<Usuario> Ousuario = usuarioRepository.findById(compraPasaje.getUsuarioId());
		Usuario usuario = Ousuario.get();
		String email = usuario.getEmail();
		int viajeId = compraPasaje.getViajeId();
		Optional<Viaje> Oviaje = viajeRepository.findById(viajeId);
		Viaje viaje = Oviaje.get();
		String destino = viaje.getLocalidadDestino().getNombre();
		Date fechaInicio = viaje.getFechaInicio();
		LocalTime hora = viaje.getHoraInicio();
		String para = email;
		String asunto = "Compra realizada";

		String mensaje = String.format(
				"Usted ha realizado una compra con destino <b>%s</b> para el día <b>%s</b> a la hora <b>%s</b>.",
				destino, fechaInicio, hora);
		emailService.enviarCorreo(para, asunto, mensaje);
	}

	public int enviarMailVenderPasaje(DtoVenderPasaje venderPasaje) {
		Usuario usuario = new Usuario();
		try {
			Optional<Usuario> Ousuario = usuarioRepository.findByEmail(venderPasaje.getEmailCliente());// Id(venderPasaje.getUsuarioId());
			usuario = Ousuario.get();
		} catch (Exception e) {
			return 0;
		}
		String email = usuario.getEmail();
		int viajeId = venderPasaje.getViajeId();
		Optional<Viaje> Oviaje = viajeRepository.findById(viajeId);
		Viaje viaje = Oviaje.get();
		String destino = viaje.getLocalidadDestino().getNombre();
		Date fechaInicio = viaje.getFechaInicio();
		LocalTime hora = viaje.getHoraInicio();
		String para = email;
		String asunto = "Compra realizada";

		String mensaje = String.format(
				"Usted ha realizado una compra con destino <b>%s</b> para el día <b>%s</b> a la hora <b>%s</b>.",
				destino, fechaInicio, hora);
		emailService.enviarCorreo(para, asunto, mensaje);
		return 1;
	}

	public void enviarMailCancelarCompra(int idCompra) {
		Optional<CompraPasaje> Ocompra = comprapasajerepository.findById(idCompra);
		CompraPasaje compra = Ocompra.get();
		Optional<Usuario> Ousuario = usuarioRepository.findByEmail(compra.getUsuario().getEmail());
		Usuario usuario = Ousuario.get();
		String email = usuario.getEmail();
		int viajeId = compra.getViaje().getId();
		Optional<Viaje> Oviaje = viajeRepository.findById(viajeId);
		Viaje viaje = Oviaje.get();
		String destino = viaje.getLocalidadDestino().getNombre();
		Date fechaInicio = viaje.getFechaInicio();
		LocalTime hora = viaje.getHoraInicio();
		String para = email;
		String asunto = "Compra cancelada";

		String mensaje = String.format(
				"La compra con destino <b>%s</b> para el día <b>%s</b> a la hora <b>%s</b> ha sido cancelada.", destino,
				fechaInicio, hora);
		emailService.enviarCorreo(para, asunto, mensaje);
	}

	public void enviarMailReenviarCodigo(String email) {
		Optional<Usuario> Ousuario = usuarioRepository.findByEmail(email);// Id(compraPasaje.getUsuarioId());
		Usuario usuario = Ousuario.get();
		int codigo = usuario.getCodigo();
		System.out.println("codigo a reenviar: " + codigo);
		String para = email;
		String asunto = "Codigo de acceso";
		String mensaje = String.format("utilize el siguiente código: <b>%s</b> para iniciar sesión en el sistema",
				codigo);
		emailService.enviarCorreo(para, asunto, mensaje);

	}

	public void enviarMailReservarPasaje(DtoCompraPasaje dtoComprarPasaje) {
		Optional<Usuario> Ousuario = usuarioRepository.findById(dtoComprarPasaje.getUsuarioId());
		Usuario usuario = Ousuario.get();
		String email = usuario.getEmail();
		int viajeId = dtoComprarPasaje.getViajeId();
		Optional<Viaje> Oviaje = viajeRepository.findById(viajeId);
		Viaje viaje = Oviaje.get();
		String destino = viaje.getLocalidadDestino().getNombre();
		Date fechaInicio = viaje.getFechaInicio();
		LocalTime hora = viaje.getHoraInicio();
		String para = email;
		String asunto = "Compra reservada";

		String mensaje = String.format(
				"Usted ha realizado una reserva con destino <b>%s</b> para el día <b>%s</b> a la hora <b>%s</b>.",
				destino, fechaInicio, hora);
		emailService.enviarCorreo(para, asunto, mensaje);

	}

	public void enviarMailAvisandoDeViaje(int idCompra) {
		Optional<CompraPasaje> Ocompra = comprapasajerepository.findById(idCompra);
		CompraPasaje compra = Ocompra.get();
		Optional<Usuario> Ousuario = usuarioRepository.findByEmail(compra.getUsuario().getEmail());
		Usuario usuario = Ousuario.get();
		String email = usuario.getEmail();
		int viajeId = compra.getViaje().getId();
		Optional<Viaje> Oviaje = viajeRepository.findById(viajeId);
		Viaje viaje = Oviaje.get();
		String destino = viaje.getLocalidadDestino().getNombre();
		Date fechaInicio = viaje.getFechaInicio();
		LocalTime hora = viaje.getHoraInicio();
		String para = email;
		String asunto = "¡¡¡ Aviso, su viaje comienza en menos de una hora!!!";

		String mensaje = String.format(
				"Recuerde que usted tiene un viaje con destino <b>%s</b> para el día <b>%s</b> a la hora <b>%s</b> .",
				destino, fechaInicio, hora);
		emailService.enviarCorreo(para, asunto, mensaje);

	}

	public int cambiarEstadoCompra(int idCompra) {
	    Optional<CompraPasaje> optionalCompra = comprapasajerepository.findById(idCompra);
	    
	    if (!optionalCompra.isPresent()) {
	        return 0;//throw new IllegalArgumentException("No se encontró la compra con id: " + idCompra);
	    }

	    CompraPasaje compra = optionalCompra.get();
	    EstadoCompra estado = compra.getEstadoCompra();

	    if (estado == null) {
	        return 2;//throw new IllegalStateException("La compra con id " + idCompra + " no tiene un estado definido.");
	    }

	    switch (estado) {
	        case REALIZADA:
	            compra.setEstadoCompra(EstadoCompra.RESERVADA);
	            comprapasajerepository.save(compra);
	            break;
	        case RESERVADA:
	            compra.setEstadoCompra(EstadoCompra.REALIZADA);
	            comprapasajerepository.save(compra);
	            break;
	        default:
	            System.out.println("Estado desconocido: " + estado);
	    }
	    return 1;
	}

	public List<DtoMisViajes> obtenerMisViajes(String email) {
		List<Viaje> viajes = viajeRepository.findAll();
		List<DtoMisViajes> misViajes = new ArrayList<>();
		int idUsuario = usuarioRepository.findByEmail(email).get().getId();
		int cantidadViajes = 0;
		for (Viaje v : viajes) {
			if (v.getId() == idUsuario) {
				cantidadViajes++;
				DtoMisViajes viaje = new DtoMisViajes();
				viaje.setFechaInicio(v.getFechaInicio());// setFechaInicio() = v.ge//misViajes.add(v);
				viaje.setIdLocalidadDestino(v.getLocalidadDestino().getId());
				viaje.setPrecio(v.getPrecio());
				viaje.setIdOmnibus(v.getOmnibus().getId());
				misViajes.add(viaje);
			}
		}
		System.out.println("cantidad de viajes: " + cantidadViajes);
		return misViajes;
	}

	public List<DtoMisCompras> obtenerMisCompras(String email) {
		List<CompraPasaje> compras = comprapasajerepository.findAll();
		List<DtoMisCompras> misCompras = new ArrayList<>();
		int idUsuario = 0;
//		categoriaUsuario categoria = usuarioRepository.findByEmail(email).get().getCategoria();
//		Optional<Categoria> OcategoriaUsuario = categoriaRepository.findBynombreCategoria(categoria.name());
//		int descuento = OcategoriaUsuario.get().getDescuento();
		try {
			idUsuario = usuarioRepository.findByEmail(email).get().getId();
		} catch (Exception e) {
			// TODO: handle exception
		}

		for (CompraPasaje c : compras) {
			if (c.getUsuario().getId() == idUsuario) {
				DtoMisCompras compra = new DtoMisCompras();
				EstadoCompra estado = c.getEstadoCompra();
				switch (estado) {
				case REALIZADA:
					System.out.println("compraId: " + c.getId());
					compra.setEstadoCompra(c.getEstadoCompra());
					compra.setViajeId(c.getViaje().getId());
					compra.setCompraId(c.getId().intValue());
					List<Integer> asientos = new ArrayList<>();
					for (AsientoPorViaje apv : c.getAsientos()) {
						asientos.add(apv.getOmnibusAsiento().getAsiento().getId());
					}
					compra.setNumerosDeAsiento(asientos);
					compra.setEstadoCompra(c.getEstadoCompra());
					compra.setDescuento(c.getDescuentoAplicado());
					misCompras.add(compra);
				default:
					System.out.println("Estado compra: " + estado);
				}
			}
		}
		return misCompras;
	}

	public List<DtoMisCompras> obtenerMisReservas(String email) {
		List<CompraPasaje> compras = comprapasajerepository.findAll();
		List<DtoMisCompras> misReservas = new ArrayList<>();
		int idUsuario = usuarioRepository.findByEmail(email).get().getId();
		categoriaUsuario categoria = usuarioRepository.findByEmail(email).get().getCategoria();
		Optional<Categoria> OcategoriaUsuario = categoriaRepository.findBynombreCategoria(categoria.name());
		int descuento = OcategoriaUsuario.get().getDescuento();
		for (CompraPasaje c : compras) {
			if (c.getUsuario().getId() == idUsuario) {
				DtoMisCompras reserva = new DtoMisCompras();
				EstadoCompra estado = c.getEstadoCompra();
				switch (estado) {
				case RESERVADA:
					System.out.println("ReservaId: " + c.getId());
					reserva.setEstadoCompra(c.getEstadoCompra());
					reserva.setViajeId(c.getViaje().getId());
					List<Integer> asientos = new ArrayList<>();
					for (AsientoPorViaje apv : c.getAsientos()) {
						asientos.add(apv.getOmnibusAsiento().getAsiento().getId());
					}
					reserva.setNumerosDeAsiento(asientos);
					reserva.setEstadoCompra(c.getEstadoCompra());
					reserva.setCompraId(c.getId().intValue());
					reserva.setDescuento(c.getDescuentoAplicado());
					misReservas.add(reserva);
				default:
					System.out.println("Estado desconocido: " + estado);
				}
			}
		}
		return misReservas;
	}

	public DtoUsuario buscarPorId(int idUsuario) {
		DtoUsuario usuario = new DtoUsuario();
		Usuario user = new Usuario();
		try {
			Optional<Usuario> Ousuario = usuarioRepository.findById(idUsuario);
			user = Ousuario.get();
		} catch (Exception e) {
		}
		usuario.setActivo(user.getActivo());
		usuario.setApellido(user.getApellido());

		// categoriaUsuario categoria = user.getCategoria();
//		switch (categoria){
//		case "GENERAL":
//			user.setCategoria(categoriaUsuario.GENERAL);
//			break;
//		case "JUBILADO":
//			user.setCategoria(categoriaUsuario.JUBILADO);
//			break;
//		case "ESTUDIANTE":
//			user.setCategoria(categoriaUsuario.ESTUDIANTE);
//			break;
//		}

		usuario.setCategoria(user.getCategoria());
		usuario.setCi(user.getCi());
		usuario.setCod_empleado(user.getCod_empleado());
		usuario.setEmail(user.getEmail());
		usuario.setFechaNac(user.getFechaNac());
		usuario.setNombre(user.getNombre());
		usuario.setRol(user.getRol());
		usuario.setId(user.getId());
		return usuario;
	}

	public boolean modificarPerfil(DtoUsuarioPerfil usuario) {

		Usuario user = new Usuario();
		try {
			Optional<Usuario> Ousuario = usuarioRepository.findById(usuario.getId());
			System.out.println("email: " + usuario.getEmail());
			user = Ousuario.get();
		} catch (Exception e) {
			return false;
		}
		user.setApellido(usuario.getApellido());
		user.setEmail(usuario.getEmail());
		user.setNombre(usuario.getNombre());
		usuarioRepository.save(user);
		return true;
	}
//
//	public List<DtoNewUsuariosPorMes> obtenerUsuariosPorMes() {
//		List<DtoNewUsuariosPorMes> lista = usuarioRepository.contarUsuariosPorMes();
//		return lista;
//	}
	
	public List<DtoNewUsuariosPorMes> obtenerUsuariosPorMes() {
	    List<Object[]> resultados = usuarioRepository.contarUsuariosPorMes();
	    
	    List<DtoNewUsuariosPorMes> lista = resultados.stream()
	        .map(obj -> new DtoNewUsuariosPorMes(
	            (String) obj[0],
	            ((Number) obj[1]).longValue()
	        ))
	        .collect(Collectors.toList());

	    return lista;
	}

	public List<DtoUsuariosPorRol> obtenerUsuariosPorRol() {
		List<DtoUsuariosPorRol> lista = new ArrayList<>();
		List<DtoUsuariosPorRolQuery> consulta = usuarioRepository.usuariosPorRol();

		for (DtoUsuariosPorRolQuery query : consulta) {
			DtoUsuariosPorRol dto = new DtoUsuariosPorRol();
			dto.setCantidad(query.getCantidad());
			switch (query.getRol()) {
			case 100:
				dto.setRol("Usuario final");
				break;
			case 200:
				dto.setRol("Vendedor");
				break;
			case 300:
				dto.setRol("Admin");
				break;
			}
			lista.add(dto);
		}

		return lista;
	}

	public List<DtoCantidadPorRol> cantidadPorRol() {
		List<DtoCantidadPorRol> listado = new ArrayList<>();
		List<DtoCantidadPorRolQuery> listadoQuery = usuarioRepository.contarUsuariosPorRol();

		for (DtoCantidadPorRolQuery cprq : listadoQuery) {
			String rol = null;
			DtoCantidadPorRol cantidad = new DtoCantidadPorRol();
			Integer rols = cprq.getRol();

			switch (rols) {
			case 100:
				rol = "Usuario_final";
				break;
			case 200:
				rol = "Vendedor";
				break;
			case 300:
				rol = "Admin";
				break;
			}
			cantidad.setRol(rol);
			cantidad.setCantidad(cprq.getCantidad());

			listado.add(cantidad);
		}
		return listado;
	}

	public int resetearContraseña(String email) {
		String asunto = "Nueva contraseña temporal";
		Optional<Usuario> usuario = buscarPorEmail(email);

		if (usuario.isPresent()) {

			// Nueva contraseña aleatoria
			String contrasenia = GenerarContraseniaService.generarContraseniaAleatoria();

			String mensaje = "Su nueva contraseñia temporal es la siguiente: " + contrasenia;

			// Setear nueva contraseña encriptada
			usuario.get().setPassword(encriptarSHA256(contrasenia));
			usuario.get().setContraseniaValida(false);
			actualizar(usuario.get());

			emailService.enviarCorreo(email, asunto, mensaje);
			return 1;
		}
		return 0;
	}

	public int cambiarRol(String email, String rol) {

		Optional<Usuario> usuario = buscarPorEmail(email);
		try {
			System.out.println("Rol actual: " + usuario.get().getRol());
		}catch (Exception e) {
			// TODO: handle exception
		}

//		if (usuario.isPresent()) {
//			System.out.println("Rol actual: " + usuario.get().getRol());
//			if (rol.equals("User")) {
//				usuario.get().setRol(100);
//				usuario.get().setCod_empleado(null);
//				actualizar(usuario.get());
//			} else if (rol.equals("Vendedor")) {
//				usuario.get().setRol(200);
//				if (usuario.get().getCod_empleado() == 0) {
//					try {
//						usuario.get().setCod_empleado(usuarioRepository.findMaxCodEmpleado() + 1);
//					} catch (Exception e) {
//						usuario.get().setCod_empleado(100);
//					}
//				}
//				actualizar(usuario.get());
//			} else if (rol.equals("Admin")) {
//				usuario.get().setRol(300);
//				if (usuario.get().getCod_empleado() == 0) {
//					try {
//						usuario.get().setCod_empleado(usuarioRepository.findMaxCodEmpleado() + 1);
//					} catch (Exception e) {
//						usuario.get().setCod_empleado(100);
//					}
//				}
//				actualizar(usuario.get());
//			} else {
//				System.out.println("No existe el Rol ingresado");
//				return 0;
//			}
//			return 1;
//		}
		if (usuario.isPresent()) {
		    System.out.println("Rol actual: " + usuario.get().getRol());

		    if (rol.equals("User")) {
		        usuario.get().setRol(100);
		        usuario.get().setCod_empleado(null);
		        actualizar(usuario.get());

		    } else if (rol.equals("Vendedor") || rol.equals("Admin")) {
		        usuario.get().setRol(rol.equals("Vendedor") ? 200 : 300);

		        Integer codEmpleado = usuario.get().getCod_empleado();
		        if (codEmpleado == null || codEmpleado == 0) {
		            try {
		                usuario.get().setCod_empleado(usuarioRepository.findMaxCodEmpleado() + 1);
		            } catch (Exception e) {
		                usuario.get().setCod_empleado(100);
		            }
		        }

		        actualizar(usuario.get());

		    } else {
		        System.out.println("No existe el Rol ingresado");
		        return 0;
		    }

		    return 1;
		}
		return 2;
	}

	public DtoUsuarioMensaje buscarPorEmails(String email) {
		Usuario u = new Usuario();
		DtoUsuarioMensaje usuarios = new DtoUsuarioMensaje();
		DtoUsuario usuario = new DtoUsuario();
		try{
			Optional<Usuario> user = usuarioRepository.findByEmail(email);
			if(user.isPresent()) {
				usuarios.setResultado(1);
				u = user.get();
			}else {
				usuarios.setResultado(0);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		usuario.setId(u.getId());
		usuario.setActivo(u.getActivo());
		usuario.setApellido(u.getApellido());
		usuario.setCategoria(u.getCategoria());
		usuario.setCi(u.getCi());
		usuario.setCod_empleado(u.getCod_empleado());
		usuario.setEmail(u.getEmail());
		usuario.setFechaNac(u.getFechaNac());
		usuario.setNombre(u.getNombre());
		usuario.setRol(u.getRol());
		usuarios.setDtoUsuario(usuario);	
		return usuarios;
	}

	public void activarUsuario(Optional<Usuario> user) {
		Usuario u = user.get();
		u.setActivo(true);
		usuarioRepository.save(u);
		//return u;		
	}
	
	public int calcularEdad(Date fecha1) {
		// Convertir java.sql.Date a LocalDate
	    LocalDate fechaNacimiento = ((java.sql.Date) fecha1).toLocalDate();

	    // Fecha actual
	    LocalDate fechaActual = LocalDate.now();

	    // Calcular edad
	    return Period.between(fechaNacimiento, fechaActual).getYears();
	}

	public List<DtoUsuariosPorEdad> obtenerUsuariosPorEdad() {
		List<Usuario> Ousuario = usuarioRepository.findAll();
		List<DtoUsuariosPorEdad> resultado = new ArrayList<>();
		Date fechaActual = new Date();
		DtoUsuariosPorEdad primerFranja = new DtoUsuariosPorEdad(); // 16 a 25
		DtoUsuariosPorEdad segundaFranja = new DtoUsuariosPorEdad(); // 26 a 35
		DtoUsuariosPorEdad terceraFranja = new DtoUsuariosPorEdad(); // 36 a 45
		DtoUsuariosPorEdad cuartaFranja = new DtoUsuariosPorEdad(); // 46 a 55
		DtoUsuariosPorEdad quintaFranja = new DtoUsuariosPorEdad(); // + de 55
		int edad= 0;
		
		primerFranja.setRango("16 a 25");
		primerFranja.setCantidad(0);
		segundaFranja.setRango("26 a 35");
		segundaFranja.setCantidad(0);
		terceraFranja.setRango("36 a 45");
		terceraFranja.setCantidad(0);
		cuartaFranja.setRango("46 a 55");
		cuartaFranja.setCantidad(0);
		quintaFranja.setRango("+ de 55");
		quintaFranja.setCantidad(0);
		
		for(Usuario u: Ousuario) {
			edad = calcularEdad(u.getFechaNac());
			System.out.println("Edad: " + edad);
			if (edad >= 16 && edad <= 25) {
				primerFranja.setCantidad(primerFranja.getCantidad() + 1);
			}else if (edad >= 26 && edad <= 35) {
				segundaFranja.setCantidad(segundaFranja.getCantidad() + 1);
			}else if (edad >= 36 && edad <= 45) {
				terceraFranja.setCantidad(terceraFranja.getCantidad() + 1);
			}else if (edad >= 46 && edad <= 55) {
				cuartaFranja.setCantidad(cuartaFranja.getCantidad() + 1);
			}if (edad >= 56) {
				quintaFranja.setCantidad(quintaFranja.getCantidad() + 1);
			}else {
				System.out.println("no se puede agrupar la edad");
			}
			
		}
		
		resultado.add(quintaFranja);
		resultado.add(cuartaFranja);
		resultado.add(terceraFranja);
		resultado.add(segundaFranja);
		resultado.add(primerFranja);
				
		return resultado;
	}

	public DtoUsuariosActivos obtenerUsuariosActivos() {
		List<Usuario> Ousuario = usuarioRepository.findAll();
		DtoUsuariosActivos resultado = new DtoUsuariosActivos();
		int usuarioActivos = 0;
		int usuariosInactivos = 0;
		
		for(Usuario u: Ousuario) {
			if (u.getActivo()) {
				usuarioActivos++;
			}else {
				usuariosInactivos++;
			}
		}
		System.out.println("Usuarios activos: " + usuarioActivos);
		System.out.println("Usuarios inactivos: " + usuariosInactivos);
		resultado.setActivos(usuarioActivos);
		resultado.setInactivos(usuariosInactivos);
		return resultado;
	}

	public List<DtoUsuariosPorCategoria> obtenerUsuariosPorCategoria() {
		List<Usuario> Ousuario = usuarioRepository.findAll();
		List<DtoUsuariosPorCategoria> resultado = new ArrayList<>();
		DtoUsuariosPorCategoria general = new DtoUsuariosPorCategoria();
		general.setCategoria("General");
		DtoUsuariosPorCategoria estudiante = new DtoUsuariosPorCategoria();
		estudiante.setCategoria("Estudiante");
		DtoUsuariosPorCategoria jubilado = new DtoUsuariosPorCategoria();
		jubilado.setCategoria("Jubilado");
		
		for(Usuario u: Ousuario) {
			categoriaUsuario categoria = u.getCategoria();
			
			switch(categoria) {
			case GENERAL:
				general.setCantidad(general.getCantidad() + 1);
			case JUBILADO:
				jubilado.setCantidad(jubilado.getCantidad() + 1);
			case ESTUDIANTE:
				jubilado.setCantidad(jubilado.getCantidad() + 1);
			}
		}
		resultado.add(jubilado);
		resultado.add(general);
		resultado.add(estudiante);
		return resultado;
	}

}