package com.productos_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
//4.3. Modificar la Entidad Producto para Incluir Usuario
//Asegúrate de que la entidad Producto incluye el usuarioId que relaciona el producto con un usuario.
@Data

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double precio;
    private Long usuarioId;
}
