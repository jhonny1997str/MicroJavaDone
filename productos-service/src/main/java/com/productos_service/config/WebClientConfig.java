// productos-service/src/main/java/com/productos_service/config/WebClientConfig.java

package com.productos_service.config;

import org.springframework.context.annotation.Bean; // Importa la anotación @Bean para definir un bean en el contexto de la aplicación.
import org.springframework.context.annotation.Configuration; // Importa la anotación @Configuration para marcar la clase como una clase de configuración.
import org.springframework.web.reactive.function.client.WebClient; // Importa la clase WebClient para realizar solicitudes HTTP de manera reactiva.

@Configuration // Indica que esta clase es una clase de configuración de Spring.
public class WebClientConfig {

    @Bean // Anota el método para indicar que debe ser tratado como un bean por el contenedor de Spring.
    //Builder es un patrón de diseño que se utiliza para construir objetos complejos paso a paso,
    //permitiendo personalizar su configuración, como URI, interceptores y timeouts,
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder(); // Devuelve una instancia del constructor de WebClient.
    }
}
//Esta clase es importante porque WebClient es una biblioteca que facilita la integración con servicios externos,
// manejando solicitudes asíncronas de manera eficiente. Además, permite una configuración centralizada y se puede
// inyectar como una dependencia, es decir, como un bean, lo que mejora la modularidad y el mantenimiento
// de la aplicación.
//WebClient es una biblioteca de Spring que forma parte del modulo  WebFlux y permite realizar solicitudes HTTP
// de forma no bloqueante y reactiva, lo que significa que tu aplicación puede manejar más solicitudes
// simultáneamente sin  quedar bloqueada mientras espera respuestas.

//BUILDER
//URI (Uniform Resource Identifier) es una cadena de caracteres que identifica un recurso en la web. Un URI
// puede ser un URL  que especifica la ubicación de un recurso y cómo acceder a él

//interceptors son componentes que permiten interceptar y modificar solicitudes y respuestas en aplicaciones de red.
// Se utilizan para agregar cabeceras o autenticación a las solicitudes, procesar respuestas y errores,
// registrar actividades para auditoría y depuración

//Timeouts: Facilita la configuración de tiempos de espera para las solicitudes

