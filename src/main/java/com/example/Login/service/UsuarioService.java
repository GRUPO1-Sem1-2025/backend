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

import com.example.Login.model.Usuario;
import com.example.Login.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.function.text.Concatenate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

	@Autowired
	private final UsuarioRepository usuarioRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private EmailService emailService;

	// private final BCryptPasswordEncoder passwordEncoder = new
	// BCryptPasswordEncoder();

	// Inyección de dependencias
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
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
	public Usuario crearUsuario(Usuario usuario) {
		System.out.println("Entre al usuario service");
		usuario.setPassword(encriptarSHA256(usuario.getPassword()));
		usuario.setRol(100);
		usuario.setActivo(true);
		usuario.setFechaCreacion(LocalDate.now());
		return usuarioRepository.save(usuario);
		// emailService.enviarCorreo(para, asunto, mensaje);

	}

	// Guardar usuario con contraseña encriptada
	public Usuario registrarNuevoUsuario(Usuario usuario) {
		// String contrasenia = encriptarSHA256(usuario.getApellido() +
		// usuario.getNombre());
		System.out.println("Entre al usuario service");
		// usuario.setPassword(encriptarSHA256(contrasenia));
		usuario.setRol(100);
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

	public Usuario borrarUsuario(Optional<Usuario> user) {
		Usuario u = user.get();
		usuarioRepository.delete(u);
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
				user.setEmail(values[2]);
				user.setPassword(values[3]);
				user.setPassword(encriptarSHA256(user.getPassword()));

				if (values[4].equals("User")) {
					user.setRol(100);
				} else if (values[4].equals("Vendedor")) {
					user.setRol(200);
				} else if (values[4].equals("Admin")) {
					user.setRol(300);
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

	public String authenticate(String email, String password) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		System.out.println("encontró usuario en service ");

		if (usuario.isPresent() && encriptarSHA256(password).equals(usuario.get().getPassword())) {
			System.out.println("Las contraseñas coinciden");
			//int rol = usuario.get().getRol();
			
			// le cargo el codigo generado al usuario que se autentico
			usuario.get().setCodigo(generarCodigo());
			usuarioRepository.save(usuario.get());
			
			String para = usuario.get().getEmail();
			String asunto = "Código de autorización";
			String mensaje = "Bienvenido " + usuario.get().getNombre() + " utilize el siguiente código " + usuario.get().getCodigo() + " para iniciar sesión en el sistema ";
					
			emailService.enviarCorreo(para, asunto, mensaje);
			
			System.out.println("codigo: " + usuario.get().getCodigo());
			//return jwtService.generateToken(email, rol);
			return "OK";
		} else {
			System.out.println("retorna null");
			return null;
		}
	}
	
	public String verificarCodigo(String email, int codigo) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		int rol = usuario.get().getRol();
		if (usuario.get().getCodigo() == codigo) {
			return jwtService.generateToken(email, rol);
		}
		else {
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

}