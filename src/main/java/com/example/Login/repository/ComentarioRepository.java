package com.example.Login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Login.model.Categoria;
import com.example.Login.model.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
}
