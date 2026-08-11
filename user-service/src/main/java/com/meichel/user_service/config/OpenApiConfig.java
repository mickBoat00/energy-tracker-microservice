package com.meichel.user_service.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration  
public class OpenApiConfig {

    @Value("${openapi.server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI userServiceApiDocs() {
        return new OpenAPI()
                .servers(List.of(new Server().url(serverUrl)))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("User Service API")
                        .description("User service API for Home Energy Tracker Project")
                        .contact(getContact())
                        .license(getLicense())
                        .version("1.0.0"));
    }

    private static License getLicense() {
        License license = new License();
        license.setName("Creative Commons Attribution-NonCommercial 4.0 International License");
        license.setUrl("https://creativecommons.org/licenses/by-nc/4.0/");
        return license;
    }

    private static Contact getContact() {
        Contact contact = new Contact();
        contact.setUrl("https://mickboat00.github.io/");
        contact.setEmail("m.boateng0000@gmail.com");
        return contact;
    }
}
