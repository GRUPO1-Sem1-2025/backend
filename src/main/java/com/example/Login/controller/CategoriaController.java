package com.example.Login.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Login.service.CategoriaService;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

	@Autowired
	CategoriaService categoriaService;

	@PostMapping("/cambiarDescuento")
	public ResponseEntity<Map<String, Object>> cambiarDescuento(@RequestParam String nombreCategoria, @RequestParam int descuento) {
		Map<String, Object> response = new HashMap<>();

		int respuesta = categoriaService.cambiarDescuento(nombreCategoria, descuento);

		if (respuesta == 1) {
			response.put("mensaje", "El descuento se cambio de forma correcta");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("error", "No se pudo cambiar el descuento");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
