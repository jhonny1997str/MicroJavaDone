// usuarios-service/src/main/java/com/usuarios_service/controller/UsuarioController.java

package com.usuarios_service.controller;

import com.usuarios_service.models.Usuario; // Importa la clase Usuario
import com.usuarios_service.repository.UsuarioRepository; // Importa el repositorio para la clase Usuario
import org.springframework.beans.factory.annotation.*; // Importa la anotación @Autowired
import org.springframework.http.ResponseEntity; // Importa ResponseEntity para manejar respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa las anotaciones para el manejo de solicitudes HTTP

import java.util.List; // Importa la clase List

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/usuarios") // Define el prefijo para todas las rutas en este controlador
public class UsuarioController {

    @Autowired // Inyección de dependencia; permite que Spring inyecte el UsuarioRepository
    private UsuarioRepository usuarioRepository; // Declara el repositorio para interactuar con la base de datos

    @GetMapping // Mapeo para la ruta GET "/api/usuarios"
    public List<Usuario> getAllUsuarios() {
        // Llama al método findAll del repositorio para obtener todos los usuarios
        return usuarioRepository.findAll();
    }

    @PostMapping // Mapeo para la ruta POST "/api/usuarios"
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        // Guarda el usuario recibido en la base de datos y devuelve el usuario creado
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/{id}") // Mapeo para la ruta GET "/api/usuarios/{id}"
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        // Busca el usuario por ID; si lo encuentra, lo devuelve; de lo contrario, devuelve 404 Not Found
        return usuarioRepository.findById(id)
                .map(usuario -> ResponseEntity.ok().body(usuario)) // Si el usuario existe, devuelve 200 OK y el usuario
                .orElse(ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }

    @PutMapping("/{id}") // Mapeo para la ruta PUT "/api/usuarios/{id}"
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario detallesUsuario) {
        // Busca el usuario por ID; si lo encuentra, actualiza sus datos; de lo contrario, devuelve 404 Not Found
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(detallesUsuario.getNombre()); // Actualiza el nombre
                    usuario.setEmail(detallesUsuario.getEmail()); // Actualiza el email
                    Usuario actualizado = usuarioRepository.save(usuario); // Guarda los cambios en la base de datos
                    return ResponseEntity.ok().body(actualizado); // Devuelve 200 OK con el usuario actualizado
                }).orElse(ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }

    @DeleteMapping("/{id}") // Mapeo para la ruta DELETE "/api/usuarios/{id}"
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        // Busca el usuario por ID; si lo encuentra, lo elimina; de lo contrario, devuelve 404 Not Found
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuarioRepository.delete(usuario); // Elimina el usuario de la base de datos
                    return ResponseEntity.ok().build(); // Devuelve 200 OK
                }).orElse(ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }
}
