package com.example.ZenFlow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI zenFlowOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor de Desenvolvimento");

        Server prodServer = new Server();
        prodServer.setUrl("https://zenflow-api.com");
        prodServer.setDescription("Servidor de Produção");

        Contact contact = new Contact();
        contact.setEmail("zenflow@example.com");
        contact.setName("ZenFlow Team");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("ZenFlow API")
                .version("1.0.0")
                .contact(contact)
                .description("API REST para o sistema ZenFlow - Diário de Bem-Estar Anônimo. " +
                        "Sistema que permite aos funcionários registrarem seus níveis de estresse " +
                        "de forma anônima, com dashboard para visualização de métricas por departamento.")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}

