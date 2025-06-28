package com.example.Login.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Login.dto.DtoViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.dto.DtoCompraPasaje;
import com.example.Login.dto.DtoComprasUsuarios;
import com.example.Login.dto.DtoTipoDeCompra;
import com.example.Login.dto.DtoTotalPorMes;
import com.example.Login.repository.CompraPasajeRepository;
import com.example.Login.service.CompraPasajeService;
import com.example.Login.service.LocalidadService;


@RestController
@RequestMapping("/compras")
public class ComprasController {
	
	@Autowired
    private CompraPasajeService compraPasajeService;

	@Autowired
	private CompraPasajeRepository comprapasajerepository;
	
	@GetMapping("/obtenerComprasPorViaje")
	public List<DtoCompraPasajeNombre> obtenerComprasPorViaje(@RequestParam long idViaje) {
		List<DtoCompraPasajeNombre> dtoCompras = new ArrayList<>();
		dtoCompras = compraPasajeService.obtenerComprasPorViaje(idViaje);
		System.out.println("comprasEnControler: " + dtoCompras.size());
		return dtoCompras;
	}
	
	@GetMapping("/obtenerTotalPorMesAnio")
	public List<DtoTotalPorMes> TotalGanadoPorMesAnio(){
		return compraPasajeService.TotalGanadoPorMes();
	}
	
	@GetMapping("/comprasPorTipo")
	public List<DtoTipoDeCompra> comprasPorTipo(){
		return compraPasajeService.comprasPorTipo();// contarPorTipoVenta();
	}
	
	@GetMapping("/comprasPorUsuario")
	public List<DtoComprasUsuarios> comprasPorUsuario(){
		return compraPasajeService.comprasPorUsuario();// contarPorTipoVenta();
	}
}
