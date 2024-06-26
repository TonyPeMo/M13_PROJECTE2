package com.example.demo.controller;

import com.example.demo.bean.Configuracion;
import com.example.demo.bean.Usuario;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ConfiguracionRepository;
import com.example.demo.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioRepository usuarioService;
    private final ConfiguracionRepository configuracionRepository;

    @Autowired
    public UsuarioController(ConfiguracionRepository configuracionRepository, UsuarioRepository usuarioService) {
        this.configuracionRepository = configuracionRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        // Encripta la contraseña del usuario
        String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(hashedPassword);
        // Guarda el usuario en la base de datos
        Usuario savedUsuario = usuarioService.save(usuario);

        // Crea una nueva configuración y la asocia con el usuario
        Configuracion configuracion = new Configuracion();
        configuracion.setUsuario(savedUsuario);
        configuracionRepository.save(configuracion);

        // Devuelve el usuario guardado
        return savedUsuario;
    }

    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioDetails) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // Aquí puedes actualizar los campos del usuario
        usuario.setUsername(usuarioDetails.getUsername());

        // Encripta la nueva contraseña si está presente
        if (usuarioDetails.getPassword() != null && !usuarioDetails.getPassword().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(usuarioDetails.getPassword(), BCrypt.gensalt());
            usuario.setPassword(hashedPassword);
        }

        return usuarioService.save(usuario);
    }

    @GetMapping("/nombre/{nombre}")
    public Usuario getUsuarioByNombre(@PathVariable String nombre) {
        return usuarioService.findUsuarioByUsername(nombre);
    }

    @GetMapping("/verifyPassword")
    public boolean verifyUsuarioPassword(@RequestParam String nombre, @RequestParam String password) {
        Usuario usuario = usuarioService.findUsuarioByUsername(nombre);
        if (usuario != null) {
            return BCrypt.checkpw(password, usuario.getPassword());
        } else {
            throw new ResourceNotFoundException("Usuario", "nombre", nombre);
        }
    }

    @GetMapping("/id/{id}")
    public Usuario getUsuarioById(@PathVariable Integer id) {
        return usuarioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        Configuracion configuracion = usuario.getConfiguracion();
        if (configuracion != null) {
            configuracionRepository.delete(configuracion);
        }
        usuarioService.deleteUsuarioByIdUser(id);
    }
}
