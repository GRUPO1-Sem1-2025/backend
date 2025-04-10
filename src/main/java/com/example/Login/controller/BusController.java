package com.example.Login.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Login.service.BusService;


import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/buses")
@Tag(name = "Buses", description = "API para gestionar buses")
public class BusController {

	private final BusService busService;

	public BusController(BusService busService) {
		this.busService = busService;
	}

}
