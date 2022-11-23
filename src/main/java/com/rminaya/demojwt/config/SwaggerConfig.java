package com.rminaya.demojwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * "@Configuration":
 * Anotación para que la clase anotada, tiene la capacidad de crear "beans", los cuales serán registrado en el "repositorio de spring"
 * Para luego poder ser inyectadas.
 * "@EnableSwagger2":
 * Indica que se está habilitando el swagger 2 en el proyecto.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Creamos el bean fundamental/principal de esta clase.
     * basePackage() : Debe contener el nombre del proyecto añadiendo el nombre del paquete de los controladores.
     * De esta forma escaneará en el paquete controller las clases que tengan la anotación "@RestController" para renderizar los métodos
     * y poder documentar la API Restful que hemos desarrollada.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rminaya.demojwt.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * Este método será un "grupo de información asociada al bean".
     * Contiene la información de la API que será mostrada en el "Swagger UI".
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Todo API",
                "La API REST de ToDo App.",
                "v1",
                "Terms of service",
                new Contact("SACAViX Tech", "www.example.com", "myeaddress@company.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
