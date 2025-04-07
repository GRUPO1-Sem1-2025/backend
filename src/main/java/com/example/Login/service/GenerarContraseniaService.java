package com.example.Login.service;

import java.security.SecureRandom;

public class GenerarContraseniaService {
	private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int LONGITUD = 8;
	private static final SecureRandom random = new SecureRandom();

	public static String generarContraseniaAleatoria() {
		StringBuilder sb = new StringBuilder(LONGITUD);
		for (int i = 0; i < LONGITUD; i++) {
			int index = random.nextInt(CARACTERES.length());
			sb.append(CARACTERES.charAt(index));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String contrasenia = generarContraseniaAleatoria();
		System.out.println("String generado: " + contrasenia);
	}
}

//	
//
//	public GenerarContraseniaService() {
//		// TODO Auto-generated constructor stub
//	}
//
//}
