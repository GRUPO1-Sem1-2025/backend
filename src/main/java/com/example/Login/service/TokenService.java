package com.example.Login.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoUsuarioToken;
import com.example.Login.model.Token;
import com.example.Login.model.Usuario;
import com.example.Login.repository.TokenRepository;
import com.example.Login.repository.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.api.client.http.HttpRequest;
//import com.google.api.client.http.HttpResponse;
import com.google.firebase.auth.FirebaseToken;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TokenService {

	@Autowired
	UsuarioRepository usuariorepository;

	@Autowired
	TokenRepository tokenRepository;
	
	public FirebaseToken verifyToken(String idToken) throws Exception {
	    return FirebaseAuth.getInstance().verifyIdToken(idToken);
	}

	public int crearToken(DtoUsuarioToken usuarioToken) {
		Token token = new Token();
		Usuario usuario = new Usuario();

		try {
			String tokens = usuarioToken.getToken();
			if (tokens == null || tokens.trim().isEmpty()) {
				return 1; // token vacio
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// obtener usuario
		try {
			Optional<Usuario> Ousuario = usuariorepository.findById(usuarioToken.getId_usuario());
			usuario = Ousuario.get();
			token.setUsuario(usuario);
			token.setToken(usuarioToken.getToken());
			tokenRepository.save(token);
			return 2;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 3; // no existe el usuario
	}
	
	public void enviarPushNotification(String expoPushToken, String title, String body) throws IOException, InterruptedException {
	    JsonObject json = new JsonObject();
	    json.addProperty("to", expoPushToken);
	    json.addProperty("sound", "default");
	    json.addProperty("title", title);
	    json.addProperty("body", body);
	    json.addProperty("priority", "high");

	    HttpRequest request = HttpRequest.newBuilder()
	        .uri(URI.create("https://exp.host/--/api/v2/push/send"))
	        .header("Content-Type", "application/json")
	        .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
	        .build();

	    HttpClient client = HttpClient.newHttpClient();
	    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	    System.out.println("Token: " + expoPushToken);
	    System.out.println("Expo response: " + response.body());
	    System.out.println("");
	}
	
//	public void enviarPushNotification(String expoPushToken, String title, String body) throws IOException, InterruptedException {
//		System.out.println("Entre para enviar la  notificacion");
//		String json = """
//	    {
//	      "to": %s,
//	      "sound": "default",
//	      "title": %s,
//	      "body": %s
//	    }
//	    """.formatted(expoPushToken, title, body);
//
//	    HttpRequest request = HttpRequest.newBuilder()
//	        .uri(URI.create("https://exp.host/--/api/v2/push/send"))
//	        .header("Content-Type", "application/json")
//	        .POST(HttpRequest.BodyPublishers.ofString(json))
//	        .build();
//
//	    HttpClient client = HttpClient.newHttpClient();
//	    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//	    System.out.println("Expo response: " + response.body());
//	}
	
//	public void enviarPushNotification(DtoUsuarioToken dtoToken, String title, String body) throws IOException, InterruptedException {
//		String expoPushToken = dtoToken.getToken();
//		String json = """
//	    {
//	      "to": "%s",
//	      "sound": "default",
//	      "title": "%s",
//	      "body": "%s"
//	    }
//	    """.formatted(expoPushToken, title, body);
//
//	    HttpRequest request = HttpRequest.newBuilder()
//	        .uri(URI.create("https://exp.host/--/api/v2/push/send"))
//	        .header("Content-Type", "application/json")
//	        .POST(HttpRequest.BodyPublishers.ofString(json))
//	        .build();
//
//	    HttpClient client = HttpClient.newHttpClient();
//	    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//	    System.out.println("Expo response: " + response.body());
//	}

}
