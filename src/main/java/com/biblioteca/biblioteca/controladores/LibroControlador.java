package com.biblioteca.biblioteca.controladores;

import com.biblioteca.biblioteca.Excepciones.MisExcepciones;
import com.biblioteca.biblioteca.entidades.Autor;
import com.biblioteca.biblioteca.entidades.Editorial;
import com.biblioteca.biblioteca.entidades.Libro;
import com.biblioteca.biblioteca.servicios.AutorServicio;
import com.biblioteca.biblioteca.servicios.EditorialServicio;
import com.biblioteca.biblioteca.servicios.LibroServicio;
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
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private EditorialServicio editorialServicio;
    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Autor> autores = autorServicio.ListaAutor();
        List<Editorial> editoriales = editorialServicio.ListarEditorial();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        return "Form_Cargar_Libro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn,
            @RequestParam String titulo, @RequestParam String idAutor,
            @RequestParam String idEditorial,
            @RequestParam(required = false) Integer ejemplares, ModelMap modelo) {

        try {

            libroServicio.crearLibro(isbn, titulo, ejemplares,
                    idAutor, idEditorial);
            modelo.put("exito", "El libro fue cargado correctamente");

        } catch (MisExcepciones e) {
            modelo.put("error", e.getMessage());
            List<Autor> autores = autorServicio.ListaAutor();
            List<Editorial> editoriales = editorialServicio.ListarEditorial();
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            return "Form_Cargar_Libro.html";
        }
        return "Index.html";

    }
    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        List<Libro> libros=libroServicio.ListarLibros();
        modelo.addAttribute("libros", libros);
   return "libro_Lista.html";
    }
    @GetMapping("/modificar/{isbn}")
    public String modificarLibro (@PathVariable Long isbn, String titulo, Integer ejemplares, ModelMap modelo){
        List<Autor> autores = autorServicio.ListaAutor();
        List<Editorial> editoriales = editorialServicio.ListarEditorial();
        modelo.put("libro", libroServicio.getOne(isbn));
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        return "Libro_modificar.html";
    }
    
    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, String titulo, String idAutor,String idEditorial,Integer ejemplares,ModelMap modelo){
        try {
            
            libroServicio.modificarLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            System.out.println("pasamos el modificar");
            return "redirect:../lista";
        } catch (Exception e) {
        modelo.put("Error", e.getMessage());
        return "Libro_modificar.html";
                }
   
    }
    @GetMapping("/eliminarLibro/{isbn}")
    public String eliminarLibro(@PathVariable Long isbn){
        libroServicio.eliminarLibro(isbn);
        return "redirect:/libro/lista";
    }
}
