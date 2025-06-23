package com.example.Login.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Login.dto.DtoEnviarNuevoToken;
import com.example.Login.dto.DtoRegistrarse;
import com.example.Login.dto.DtoUsuarioToken;
import com.example.Login.repository.UsuarioRepository;
import com.example.Login.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/token")
public class TokenController {

	@Autowired
	private TokenService tokenService;// usuariorepository;

	@PostMapping("/guardarToken")
	ResponseEntity<Map<String, String>> crearToken(@RequestBody DtoUsuarioToken usuarioToken) {
		Map<String, String> response = new HashMap<>();
		int resultado = tokenService.crearToken(usuarioToken);
		
		switch(resultado){
		case 1:
			response.put("error", "No se pudo guardar el token, el mismo estaba vacio");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		case 2:
			response.put("mensaje",
					"Se ha guardado el token de forma exitosa");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		case 3:
			response.put("error", "No se pudo guardar el token, el usuario no existe");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		response.put("error", "Error Desconocido");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
//	@PostMapping("/enviarMsjPorToken")
//	public void enviarMsjPorToken(@RequestParam String token) {
//		String tiuloPrueba = "titulo";
//		String msjPrueba = "mensaje";
//		try{
//			tokenService.enviarPushNotification(token,tiuloPrueba,msjPrueba);
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
//	}	
	
	@PostMapping("/enviarMsjPorToken")
	public void enviarMsjPorToken(@RequestBody DtoEnviarNuevoToken token) {
	    String tituloPrueba = "titulo";
	    String msjPrueba = "mensaje";
	    try {
	        tokenService.enviarPushNotification(token.getIdUsuario(), token.getTitulo(), token.getMsj());
	    } catch (Exception e) {
	        e.printStackTrace(); // Podés loguear el error mejor aquí
	    }
	}

}
