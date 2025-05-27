package com.example.Login.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Login.model.Categoria;
import com.example.Login.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	CategoriaRepository categoriaRepository;
	
	public int cambiarDescuento(String nombreCategoria, int descuento) {
		try{
			Optional<Categoria> Ocategoria= categoriaRepository.findBynombreCategoria(nombreCategoria);Categoria categoria = Ocategoria.get();
			categoria.setDescuento(descuento);
			categoriaRepository.save(categoria);
			return 1;
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

}
