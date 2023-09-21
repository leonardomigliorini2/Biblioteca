
package com.biblioteca.biblioteca.servicios;

import com.biblioteca.biblioteca.Excepciones.MisExcepciones;
import com.biblioteca.biblioteca.entidades.Editorial;
import com.biblioteca.biblioteca.entidades.Libro;
import com.biblioteca.biblioteca.repositorio.EditorialRepositorio;
import com.biblioteca.biblioteca.repositorio.LibroRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {
    
    @Autowired
   private EditorialRepositorio editorialRepositorio;
   @Autowired
   private LibroRepositorio libroRepositorio;
    @Transactional
    public void CrearEditorial(String id,String nombre) throws MisExcepciones{
        validar(nombre, id);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setId(id);
    editorialRepositorio.save(editorial);
    
}
    public List<Editorial> ListarEditorial(){
        List<Editorial> editorial=new ArrayList();
        editorial=editorialRepositorio.findAll();
        return editorial;
     }
    @Transactional
    public void modificarEditorial(String id, String nombre)throws MisExcepciones{
        validar(nombre, id);
        try {
            Optional<Editorial> respuestaEditorial=editorialRepositorio.findById(id);
        Editorial editorial=new Editorial();
        if (respuestaEditorial.isPresent()) {
            editorial=respuestaEditorial.get();
            }
        editorial.setNombre(nombre);
        editorialRepositorio.save(editorial);
        } catch (Exception e) {
            e.getMessage();
        }
        
    }

    public EditorialServicio() {
    }
    public Editorial getOne(String id){
        return editorialRepositorio.getOne(id);
    }
    public void validar(String nombre, String id) throws MisExcepciones {
            
        if (id==null || id.isEmpty()) {
            throw new MisExcepciones("la id de la editorial, no puede estar vacio o ser nulo");
        }
        if (nombre==null || nombre.isEmpty()) {
            throw new MisExcepciones("el nombre no puede ser nulo o estar vacio");
}
    }
   @Transactional
   public void eliminarEditorial(String id){
       try {
       Optional<Editorial> respuestaEditorial= editorialRepositorio.findById(id);
        Editorial editorial=new Editorial();
        
       if (respuestaEditorial.isPresent()) {
          editorial=respuestaEditorial.get();
          
        
        editorialRepositorio.delete(editorial);
}    
       } catch (Exception e) {
           System.out.println("error, editorial no encontrada"+e.getMessage());
       }
 
          }
   
    
}