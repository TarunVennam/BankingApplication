package com.example.BankApp;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Banking Application",
				description = "Backend REST API for Banking Application",
				version = "v1.0",
				contact = @Contact(
						name = "Tarun Vennam",
						email = "vennamtarun123@gmail.com",
						url = "https://github.com/TarunVennam"
		),
				license = @License(
						name = "Tarun Vennam",
						url = "https://github.com/TarunVennam"
				)
),
   externalDocs = @ExternalDocumentation(
		   description = "Banking App Documentation",
		   url = "https://github.com/TarunVennam"
   )
)
public class BankAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAppApplication.class, args);
	}

}


