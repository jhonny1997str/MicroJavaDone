package com.productos_service.controller;

import com.productos_service.models.Producto;
import com.productos_service.repositorio.ProductoRepository;
import com.productos_service.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final String USUARIOS_SERVICE_URL = "http://app:8081/api/usuarios/";

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> getProductoById(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    Mono<UsuarioDTO> usuarioMono = webClientBuilder.build()
                            .get()
                            .uri(USUARIOS_SERVICE_URL + producto.getUsuarioId())
                            .retrieve()
                            .bodyToMono(UsuarioDTO.class)
                            .onErrorReturn(new UsuarioDTO());

                    return usuarioMono.map(usuario -> {
                        if (usuario != null) {
                            ProductoResponse response = new ProductoResponse(producto, usuario);
                            return ResponseEntity.ok().body(response);
                        } else {
                            return ResponseEntity.status(502).build();
                        }
                    });
                })
                .orElse(Mono.just(ResponseEntity.notFound().build()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto detallesProducto) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(detallesProducto.getNombre());
                    producto.setPrecio(detallesProducto.getPrecio());
                    Producto actualizado = productoRepository.save(producto);
                    return ResponseEntity.ok().body(actualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    productoRepository.delete(producto);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    public static class ProductoResponse {
        private Producto producto;
        private UsuarioDTO usuario;

        public ProductoResponse(Producto producto, UsuarioDTO usuario) {
            this.producto = producto;
            this.usuario = usuario;
        }

        public Producto getProducto() {
            return producto;
        }

        public void setProducto(Producto producto) {
            this.producto = producto;
        }

        public UsuarioDTO getUsuario() {
            return usuario;
        }

        public void setUsuario(UsuarioDTO usuario) {
            this.usuario = usuario;
        }
    }
}

