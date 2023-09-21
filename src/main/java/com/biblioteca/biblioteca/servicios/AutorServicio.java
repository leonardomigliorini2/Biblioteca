package com.biblioteca.biblioteca.servicios;

import com.biblioteca.biblioteca.Excepciones.MisExcepciones;
import com.biblioteca.biblioteca.entidades.Autor;
import com.biblioteca.biblioteca.repositorio.AutorRepositorio;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws MisExcepciones {
        validar(nombre);
        Autor autor = new Autor();
        autor.setNombre(nombre);

        autorRepositorio.save(autor);
    }

    public List<Autor> ListaAutor() {
        List<Autor> autor = new ArrayList();
        autor = autorRepositorio.findAll();
        return autor;
    }

    @Transactional
    public void modificarAutor(String Id, String nombre) throws MisExcepciones {
        Optional<Autor> respuestaIdAutor = autorRepositorio.findById(Id);
        Autor autor = new Autor();
        if (respuestaIdAutor.isPresent()) {
            autor = respuestaIdAutor.get();
        }
        autor.setNombre(nombre);
        autorRepositorio.save(autor);

    }

    @Transactional
    public void EliminarAutor(String Id) {

        Optional<Autor> respuestaIdAutor = autorRepositorio.findById(Id);
        Autor autor = new Autor();
        if (respuestaIdAutor.isPresent()) {
            autor = respuestaIdAutor.get();
        }
        
        autorRepositorio.delete(autor);
    }

    public Autor getOne(String id) {
        return autorRepositorio.getOne(id);
    }

    public void validar(String nombre) throws MisExcepciones {

//        if (id==null || id.isEmpty()) {
//            throw new MisExcepciones("la id de la editorial, no puede estar vacio o ser nulo");
//        }
        if (nombre == null || nombre.isEmpty()) {
            throw new MisExcepciones("el nombre no puede ser nulo o estar vacio");
        }

    }
}
