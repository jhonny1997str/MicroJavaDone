package com.productos_service.dto; // Especifica el paquete donde se encuentra esta clase DTO

import lombok.Data; // Importa la anotación @Data de Lombok para generar automáticamente métodos útiles

@Data // Anotación de Lombok que genera automáticamente los métodos getter, setter, toString, hashCode y equals para esta clase
public class UsuarioDTO {

    private Long id; // Campo que representa el identificador único del usuario
    private String nombre; // Campo que representa el nombre del usuario
    private String email; // Campo que representa el correo electrónico del usuario

    // No es necesario definir manualmente los métodos getter y setter,
    // ya que la anotación @Data se encarga de generarlos automáticamente.
}

// Crea un DTO (Data Transfer Object) para recibir la información del usuario desde el Servicio de Usuarios.
//La clase UsuarioDTO es un Data Transfer Object (DTO), que se utiliza principalmente para transportar datos
// entre diferentes capas de una aplicación, en este caso, entre servicios en una arquitectura de microservicios.
//DTO (Data Transfer Object) es un patrón de diseño que se utiliza para transportar datos entre diferentes
// partes de una aplicación.

