package com.biblioteca.biblioteca.controladores;

import com.biblioteca.biblioteca.Excepciones.MisExcepciones;
import com.biblioteca.biblioteca.entidades.Autor;
import com.biblioteca.biblioteca.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/autor") //localhost:8080/autor
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar")//localhost:8080/autor/registrar
    public String registrar() {
        return "Form_Cargar_Autor.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String id, ModelMap modelo) {
        try {
            autorServicio.crearAutor(nombre);
            modelo.put("exitoAutor", "El autor se ha cargado con exito");
        } catch (MisExcepciones e) {
            modelo.put("error", e.getMessage());
            return "Form_Cargar_Autor.html";
        }
        return "Index.html";

    }

    @GetMapping("/lista")
    public String listarAutor(ModelMap modelo) {
        List<Autor> autores = autorServicio.ListaAutor();
        modelo.addAttribute("autores", autores);
        return "autor_Lista.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo) {
        try {
            modelo.put("Autor", autorServicio.getOne(id));
            return "Autor_modificar.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "autor_Lista.html";
        }

    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo, String nombre) {
        try {

            autorServicio.modificarAutor(id, nombre);
            return "redirect:../lista";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "Autor_modificar.html";
        }
    }
       @GetMapping("/eliminarAutor/{id}")
    public String cambiarRol(@PathVariable String id) {

        autorServicio.EliminarAutor(id);
        return "redirect:/autor/lista";

    }

    }
