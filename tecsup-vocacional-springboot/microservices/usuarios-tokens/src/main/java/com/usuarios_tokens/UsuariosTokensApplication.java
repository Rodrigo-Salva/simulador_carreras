package com.usuarios_tokens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class UsuariosTokensApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosTokensApplication.class, args);

		System.out.println("========================================");
		System.out.println("TECSUP VOCACIONAL - USUARIOS SERVICE");
		System.out.println("========================================");
		System.out.println("Microservicio iniciado exitosamente!");
		System.out.println("Puerto: 8081");
		System.out.println("Health: http://localhost:8081/api/auth/health");
		System.out.println("Login: POST http://localhost:8081/api/auth/login");
		System.out.println("Register: POST http://localhost:8081/api/auth/register");
		System.out.println("Validate: POST http://localhost:8081/api/auth/validate-password");
		System.out.println("========================================");
	}
}