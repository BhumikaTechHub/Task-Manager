package com.example.taskmanager.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI taskManagerOpenAPI() {

        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()

                .info(new Info()

                        .title("Task Manager REST API")

                        .description("Task Management System built using Spring Boot, PostgreSQL, Spring Data JPA and JWT Authentication.")

                        .version("1.0")

                        .contact(new Contact()

                                .name("Bhumika Yadav")

                                .email("yadavbhumika026@gmail.com")

                                .url("https://github.com/BhumikaTechHub")

                        )

                        .license(new License()

                                .name("MIT License")

                                .url("https://opensource.org/licenses/MIT")

                        )
                )

//                .externalDocs(
//
//                        new ExternalDocumentation()
//
//                                .description("Project Repository")
//
//                                .url("https://github.com/yourusername/taskmanager")
//
//                )

                .addSecurityItem(
                        new SecurityRequirement().addList(securitySchemeName)
                )

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()

                                                .name(securitySchemeName)

                                                .type(SecurityScheme.Type.HTTP)

                                                .scheme("bearer")

                                                .bearerFormat("JWT")
                                )
                );

    }

}