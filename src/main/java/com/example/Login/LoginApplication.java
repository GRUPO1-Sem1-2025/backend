// sin zona horaria

//package com.example.Login;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//@SpringBootApplication
//@EnableScheduling
//public class LoginApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(LoginApplication.class, args);
//	}
//
//}

// hasta aca sin zona horaria


// con zona horaria

package com.example.Login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class LoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Establece la zona horaria por defecto para toda la aplicación
		TimeZone.setDefault(TimeZone.getTimeZone("America/Montevideo"));
		System.out.println("Zona horaria por defecto: " + TimeZone.getDefault().getID());
	}
}

// hasta aca con zona horaria
