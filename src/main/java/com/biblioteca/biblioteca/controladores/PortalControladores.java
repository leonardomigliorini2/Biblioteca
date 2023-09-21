package com.biblioteca.biblioteca.controladores;

import com.biblioteca.biblioteca.entidades.Usuario;
import com.biblioteca.biblioteca.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControladores {

    @GetMapping("/")
    public String index() {
        return "Index.html";
    }
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "Registro.html";
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String nombre, @RequestParam String email, @RequestParam String password, String password2, ModelMap modelo) {
        try {

            usuarioServicio.registrar(archivo, nombre, email, password, password2);
            modelo.put("exito", "se ha registrado exitosamente un usuario");
            return "Index.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "Registro.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos");

        }
        return "login.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_User','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {

            return "redirect:/admin/dashboard";
        } else {
            return "inicio.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_User', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "usuario_modificar.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_User', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo,
            @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2,
            ModelMap modelo) {
        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2);
            modelo.put("exito", "Usuario actualizado Correctamente");
            return "inicio.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "usuario_modificar.html";
        }

    }
}
