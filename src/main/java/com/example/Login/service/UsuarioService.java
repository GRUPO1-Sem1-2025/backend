package com.example.Login.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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

import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.DtoCrearCuenta;
import com.example.Login.dto.DtoMisCompras;
import com.example.Login.dto.DtoMisViajes;
import com.example.Login.dto.DtoRegistrarse;
import com.example.Login.dto.DtoUsuario;
import com.example.Login.dto.DtoUsuarioPerfil;
import com.example.Login.dto.DtoVenderPasaje;
import com.example.Login.dto.DtoViaje;
import com.example.Login.dto.EstadoCompra;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Usuario;
import com.example.Login.model.Viaje;
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
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

	@Autowired
	private final UsuarioRepository usuarioRepository;

	@Autowired
	private ViajeRepository viajeRepository;

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

	public List<Usuario> obtenerUsuarios() {
		return usuarioRepository.findAll();
	}

	// Guardar usuario con contraseña encriptada
	public void crearUsuario(DtoRegistrarse registrarse) {
		System.out.println("Entre al usuario service");
		Usuario usuario = new Usuario();
		usuario.setNombre(registrarse.getNombre());
		usuario.setApellido(registrarse.getApellido());
		usuario.setEmail(registrarse.getEmail());
		usuario.setPassword(encriptarSHA256(registrarse.getPassword()));
		usuario.setRol(100);
		usuario.setActivo(true);
		usuario.setFechaCreacion(LocalDate.now());
		usuario.setCodigo(generarCodigo());
		usuarioRepository.save(usuario);
		// emailService.enviarCorreo(para, asunto, mensaje);

	}

	// Guardar usuario con contraseña encriptada
	public Usuario crearCuenta(DtoCrearCuenta dtocrearCuenta) {

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
		usuario.setNombre(dtocrearCuenta.getNombre());
		usuario.setApellido(dtocrearCuenta.getApellido());
		usuario.setEmail(dtocrearCuenta.getEmail());
		usuario.setCategoria(dtocrearCuenta.getCategoria());
		usuario.setCi(dtocrearCuenta.getCi());
		usuario.setFechaNac(dtocrearCuenta.getFechaNac());
		usuario.setRol(rol);
		System.out.print("Rol del usuario agregado = " + usuario.getRol());
		usuario.setActivo(true);
		usuario.setFechaCreacion(LocalDate.now());
		String password = usuario.getApellido() + usuario.getNombre() + "_2025";
		usuario.setPassword(encriptarSHA256(password));

		String para = usuario.getEmail();
		String asunto = "Contrasenia de inicio de sesion";
		String mensaje = "Bienvenido " + usuario.getNombre() + " usted ha sido dado de alta en nuestro sistema."
				+ " La contraseña temporal para acceder es " + password
				+ " no se olvide de cambiarla una vez que haya ingresado";

		emailService.enviarCorreo(para, asunto, mensaje);
		return usuarioRepository.save(usuario);
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
				user.setEmail(values[2]);
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
					user.setActivo(true);// values[3].toString());
				} else {
					user.setActivo(false);
				}
				user.setCategoria(values[6]);
				user.setCi(values[7]);
				user.setFechaCreacion(LocalDate.now());

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaNacimiento = sdf.parse(values[8]);

				// Conversión segura:
				java.sql.Date sqlFechaNacimiento = new java.sql.Date(fechaNacimiento.getTime());

				dataList.add(row);

				usuarioNoEncontrado = buscarPorEmail(values[2]);
				if (usuarioNoEncontrado.isEmpty()) {
					// Guardo el usuario nuevo
					usuarioRepository.save(user);
					System.out.println("El usuario fue registrado");
				} else {
					System.out.println("El correo : " + values[2] + " ya esta registrado en el sistema");
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
			String mensaje = String.format(
					"Bienvenido <b>%s</b> utilize el siguiente código <b>%s</b> para iniciar sesión en el sistema",
					nombre, codigo);

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
		// int id = usuario.get().getId();
		if (usuario.getCodigo() == codigo) {
			return jwtService.generateToken(email, rol, id);
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

	public String obtenerToken(String email, int rol, int id) {
		String token = null;
		token = jwtService.generateToken(email, rol, id);
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
				"Recuerde que usted tiene un viaje con destino <b>%s</b> para el día <b>%s</b> a la hora <b>%s</b> .", destino,
				fechaInicio, hora);
		emailService.enviarCorreo(para, asunto, mensaje);

	}

	public void cambiarEstadoCompra(int idCompra) {
		CompraPasaje compra = new CompraPasaje();
		try {
			Optional<CompraPasaje> Ocompra = comprapasajerepository.findById(idCompra);
			compra = Ocompra.get();
		} catch (Exception e) {

		}
		EstadoCompra estado = compra.getEstadoCompra();
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
		int idUsuario = usuarioRepository.findByEmail(email).get().getId();

		for (CompraPasaje c : compras) {
			if (c.getUsuario().getId() == idUsuario) {
				DtoMisCompras compra = new DtoMisCompras();
				EstadoCompra estado = c.getEstadoCompra();
				switch (estado) {
				case REALIZADA:
					System.out.println("compraId: " + c.getId());
					compra.setEstadoCompra(c.getEstadoCompra());
					compra.setViajeId(c.getViaje().getId());
					List<Integer> asientos = new ArrayList<>();
					for (AsientoPorViaje apv : c.getAsientos()) {
						asientos.add(apv.getOmnibusAsiento().getAsiento().getId());
					}
					compra.setNumerosDeAsiento(asientos);
					compra.setEstadoCompra(c.getEstadoCompra());
					misCompras.add(compra);
				default:
					System.out.println("Estado desconocido: " + estado);
				}
			}
		}
		return misCompras;
	}

	public List<DtoMisCompras> obtenerMisReservas(String email) {
		List<CompraPasaje> compras = comprapasajerepository.findAll();
		List<DtoMisCompras> misReservas = new ArrayList<>();
		int idUsuario = usuarioRepository.findByEmail(email).get().getId();

		for (CompraPasaje c : compras) {
			if (c.getUsuario().getId() == idUsuario) {
				DtoMisCompras compra = new DtoMisCompras();
				EstadoCompra estado = c.getEstadoCompra();
				switch (estado) {
				case RESERVADA:
					System.out.println("compraId: " + c.getId());
					compra.setEstadoCompra(c.getEstadoCompra());
					compra.setViajeId(c.getViaje().getId());
					List<Integer> asientos = new ArrayList<>();
					for (AsientoPorViaje apv : c.getAsientos()) {
						asientos.add(apv.getOmnibusAsiento().getAsiento().getId());
					}
					compra.setNumerosDeAsiento(asientos);
					compra.setEstadoCompra(c.getEstadoCompra());
					misReservas.add(compra);
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
		try{
			Optional<Usuario> Ousuario = usuarioRepository.findById(usuario.getId());	
			System.out.println("email: " + usuario.getEmail());
			user = Ousuario.get();
		}catch (Exception e) {
			return false;
		}
		user.setApellido(usuario.getApellido());
		user.setEmail(usuario.getEmail());
		user.setNombre(usuario.getNombre());
		usuarioRepository.save(user);		
		return true;
	}
}