package com.example.Login.configuration;

//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

// SwaggerConfig.java
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tecnobus.Uy - Documentacion API",
                version = "Beta 0.0.1",
                description = "Esta es la API de Tecnobus.Uy, Sistema online para venta de pasajes de ómnibus de larga distancia.",
                contact = @Contact(
                        name = "Web Tecnobus.Uy",
                        url = "http://localhost:8080/"
                )
        ),
        //security = @SecurityRequirement(name = "bearerAuth"),
        servers = {
        @Server(
                url = "http://localhost:8080/", // URL para desarrollo local
                description = "Servidor local"
        ),
        @Server(
                url = "https://backend.tecnobus.uy/", // Cambia esta URL para el deploy
                description = "Servidor de producción"
        )
}
)

public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Tecnobus API")
                .pathsToMatch("/**")  // Evita incluir endpoints de Spring Data REST
                .build();
    }
}
