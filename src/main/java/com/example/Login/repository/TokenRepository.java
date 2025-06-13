package com.example.Login.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Login.model.Omnibus;
import com.example.Login.model.Token;

@Repository public interface TokenRepository extends JpaRepository<Token,
Integer>{ 
	
	@Query(value = "SELECT token FROM usuario_token WHERE usuario_id = :usuarioId", nativeQuery = true)
    List<String> findTokensByUsuarioId(@Param("usuarioId") int usuarioId);
	
	Optional<Token> findByToken(String token);
	
	
	
}



