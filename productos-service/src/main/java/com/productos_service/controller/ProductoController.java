// productos-service/src/main/java/com/productos_service/controller/ProductoController.java

package com.productos_service.controller;

// Actualizar el Controlador de Productos para Usar WebClient
//Modifica el ProductoController para utilizar WebClient en lugar de RestTemplate al comunicarse con el Servicio de Usuarios.
// Importaciones necesarias para la funcionalidad del controlador.
import com.productos_service.models.Producto; // Clase modelo para representar un Producto.
import com.productos_service.repositorio.ProductoRepository; // Repositorio para acceder a la base de datos de Productos.
import com.productos_service.dto.UsuarioDTO; // DTO (Data Transfer Object) para representar información de un usuario.
import org.springframework.beans.factory.annotation.Autowired; // Permite la inyección automática de dependencias.
import org.springframework.http.ResponseEntity; // Clase para manejar las respuestas HTTP de forma estructurada.
import org.springframework.web.bind.annotation.*; // Anotaciones para construir un controlador REST.
import org.springframework.web.reactive.function.client.WebClient; // Cliente para realizar solicitudes HTTP de forma reactiva.
import reactor.core.publisher.Mono; // Clase para representar un valor que puede ser calculado de manera asíncrona.

import java.util.List; // Importa la clase List para manejar listas de objetos.

@RestController // Anotación que indica que esta clase es un controlador REST y proporciona respuestas JSON.
@RequestMapping("/api/productos") // Define la ruta base para todas las peticiones a este controlador.
public class ProductoController {

    @Autowired // Inyección automática del repositorio de productos.
    private ProductoRepository productoRepository;

    @Autowired // Inyección automática del constructor de WebClient para realizar solicitudes externas.
    private WebClient.Builder webClientBuilder;

    // URL del servicio de usuarios, donde se realizará la consulta para obtener información de usuario.
    private final String USUARIOS_SERVICE_URL = "http://app:8081/api/usuarios/";

    // Maneja las peticiones GET a /api/productos para obtener todos los productos.
    @GetMapping // Define que este método maneja las peticiones GET.
    public List<Producto> getAllProductos() {
        // Llama al repositorio para obtener todos los productos y los retorna.
        return productoRepository.findAll();
    }

    // Maneja las peticiones POST a /api/productos para crear un nuevo producto.
    @PostMapping // Define que este método maneja las peticiones POST.
    public Producto crearProducto(@RequestBody Producto producto) {
        // Guarda el nuevo producto en la base de datos y lo retorna.
        return productoRepository.save(producto);
    }

    // Maneja las peticiones GET a /api/productos/{id} para obtener un producto específico por su ID.
    @GetMapping("/{id}") // Ruta que incluye un parámetro de ruta para el ID del producto.
    //Mono es una clase de reactor(spring webflux) puede representar un valor vacio fundamental para prog.reactiva
    //RespEntity clase de spring que respresenta respuesta http como el cuerpo, encabezados y codigo http
    // el signo ? indica que el cuerpo de la respuesta puede ser de cualquier tipo
    public Mono<ResponseEntity<?>> getProductoById(@PathVariable Long id) {
        // Busca el producto por su ID en la base de datos.
        return productoRepository.findById(id)
                //si el producto es encontrado el map se ejecuta y si no existe el flujo continua hasta el final y sera un mono vacio
                // .map() toma los elementos y los transforma en un nuevo valor
                .map(producto -> {
                    // Se crea una instancia de WebClient, que es un cliente para realizar solicitudes HTTP de forma reactiva.
                    Mono<UsuarioDTO> usuarioMono = webClientBuilder.build()
                            .get() // Indica que se realizará una petición GET.
                            //lo que permite obtener información específica sobre el usuario asociado al producto.
                            .uri(USUARIOS_SERVICE_URL + producto.getUsuarioId()) // Construye la URI con el ID del usuario.
                            .retrieve() // Realiza la solicitud y espera la respuesta del servidor.
                            //Se convierte el cuerpo de la respuesta(JSON) en un objeto de tipo UsuarioDTO(JAVA).
                            .bodyToMono(UsuarioDTO.class) // Convierte la respuesta a un objeto UsuarioDTO.
                            //SI HAY un error durante la solicitud, este método retornará un nuevo objeto
                            // UsuarioDTO vacío en lugar de lanzar una excepción.
                            .onErrorReturn(new UsuarioDTO()); // En caso de error, devuelve un objeto UsuarioDTO vacío.

                    // Mapea el resultado del usuario. MAPEAR ES TRANSFORMAR DE UN DATO A OTRO
                    return usuarioMono.map(usuario -> {
                        if (usuario != null) {
                            // Crea una respuesta que incluye el producto y el usuario asociado.
                            ProductoResponse response = new ProductoResponse(producto, usuario);
                            return ResponseEntity.ok().body(response); // Devuelve 200 OK con el contenido.
                        } else {
                            //// Devuelve 502 Bad Gateway si no se puede obtener el usuario.
                            return ResponseEntity.status(502).build();
                        }
                    });
                })
                // Devuelve 404 Not Found si el producto no existe.
                .orElse(Mono.just(ResponseEntity.notFound().build()));
    }

    // Maneja las peticiones PUT a /api/productos/{id} para actualizar un producto existente.
    @PutMapping("/{id}") // Ruta que incluye un parámetro de ruta para el ID del producto.
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto detallesProducto) {
        // Busca el producto por ID.
        return productoRepository.findById(id)
                .map(producto -> {
                    // Actualiza los detalles del producto con la información proporcionada.
                    producto.setNombre(detallesProducto.getNombre());
                    producto.setPrecio(detallesProducto.getPrecio());
                    // Guarda el producto actualizado y lo retorna.
                    Producto actualizado = productoRepository.save(producto);
                    return ResponseEntity.ok().body(actualizado); // Devuelve 200 OK con el producto actualizado.
                }).orElse(ResponseEntity.notFound().build()); // Devuelve 404 Not Found si el producto no existe.
    }

    // Maneja las peticiones DELETE a /api/productos/{id} para eliminar un producto.
    @DeleteMapping("/{id}") // Ruta que incluye un parámetro de ruta para el ID del producto.
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        // Busca el producto por ID.
        return productoRepository.findById(id)
                .map(producto -> {
                    // Elimina el producto encontrado.
                    productoRepository.delete(producto);
                    return ResponseEntity.ok().build(); // Devuelve 200 OK tras la eliminación.
                }).orElse(ResponseEntity.notFound().build()); // Devuelve 404 Not Found si el producto no existe.
    }

    //clase interna ProductoResponse es útil para estructurar de manera organizada y clara la respuesta que se
    // enviará a los clientes de la API, encapsulando los datos del producto y del usuario asociado en un solo objeto.
    public static class ProductoResponse {
        private Producto producto; // Producto asociado a la respuesta.
        private UsuarioDTO usuario; // Usuario asociado a la respuesta.

        // Constructor para inicializar los atributos de la respuesta.
        public ProductoResponse(Producto producto, UsuarioDTO usuario) {
            this.producto = producto;
            this.usuario = usuario;
        }

        // Getters y Setters para acceder y modificar los atributos.
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

//ResponseEntity es una clase de Spring que representa una respuesta HTTP completa, incluyendo el cuerpo de la
// respuesta, los encabezados y el código de estado HTTP. Es especialmente útil en controladores REST para
// construir respuestas

//Mono es fundamental para manejar operaciones asíncronas en la arquitectura reactiva de Spring.
// Permite realizar llamadas a servicios externos y manipular los resultados de forma fluida y eficiente, sin bloquear el hilo principal de la aplicación
//Mono: Es una clase de Reactor (parte de Spring WebFlux) que representa un solo valor o la ausencia de un valor (es decir, puede ser vacío

