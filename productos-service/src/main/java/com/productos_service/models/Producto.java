package com.productos_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

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
