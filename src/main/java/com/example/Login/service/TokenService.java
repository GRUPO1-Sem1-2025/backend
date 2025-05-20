package com.example.Login.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.dto.DtoUsuarioToken;
import com.example.Login.model.Token;
import com.example.Login.model.Usuario;
import com.example.Login.repository.TokenRepository;
import com.example.Login.repository.UsuarioRepository;

@Service
public class TokenService {

	@Autowired
	UsuarioRepository usuariorepository;

	@Autowired
	TokenRepository tokenRepository;

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

}
