package com.rminaya.demojwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemojwtApplication {
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        //1° Forma de ejecutar el proyecto, por defecto
//		SpringApplication.run(DemojwtApplication.class, args);

        //2° Opción de ejecutar el proyecto, imprimiendo los "beans" del "Contenedor de Spring"
        applicationContext = SpringApplication.run(DemojwtApplication.class, args);
        displayAllBeans();

    }

    public static void displayAllBeans() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : allBeanNames) {
            System.out.println(beanName);
        }
    }

}
