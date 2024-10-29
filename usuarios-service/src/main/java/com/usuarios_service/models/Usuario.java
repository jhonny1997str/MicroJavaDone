// usuarios-service/src/main/java/com/usuarios_service/models/Usuario.java

package com.usuarios_service.models;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
}
