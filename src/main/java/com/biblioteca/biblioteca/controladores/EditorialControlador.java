
package com.biblioteca.biblioteca.controladores;

import static antlr.Utils.error;
import com.biblioteca.biblioteca.Excepciones.MisExcepciones;
import com.biblioteca.biblioteca.entidades.Editorial;
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
@RequestMapping("/editorial")
public class EditorialControlador {
    
    @Autowired
    private EditorialServicio editorialServicio;
    @Autowired
    private LibroServicio LibroServicio;
    
    @GetMapping("/registrar")
    public String registrar(){
        return "Form_Cargar_Editorial";
    }
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo){
        try {
           editorialServicio.CrearEditorial(nombre, nombre);
           modelo.put("exitoEditorial", "La editorial ha sido creada exitosamente");
        } catch (MisExcepciones e) {
            modelo.put("error",e.getMessage());
            return "Form_Cargar_Editorial.html";
        } 
        return "Index.html";
    }
    @GetMapping("/lista")
    public String Lista(ModelMap modelo){
        List<Editorial> editoriales= editorialServicio.ListarEditorial();
        modelo.addAttribute("editoriales", editoriales);
        
        return "editorial_Lista.html";
    }
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre,ModelMap modelo){
        modelo.put("editorial", editorialServicio.getOne(id));
        return "Editorial_modificar.html";
    }
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo, String nombre){
        try {
            editorialServicio.modificarEditorial(id, nombre);
            return "redirect:../lista";
        } catch (Exception e) {
            modelo.put("error", e.getCause());
            return "Editorial_modificar.html";
        }
    
    }
    @GetMapping("/eliminarEditorial/{id}")
    public String eliminarEditorial(@PathVariable String id){
editorialServicio.eliminarEditorial(id);
        return "redirect:/editorial/lista";            
        } 


} 

