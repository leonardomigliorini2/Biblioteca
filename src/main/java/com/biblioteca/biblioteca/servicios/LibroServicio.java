package com.biblioteca.biblioteca.servicios;

import com.biblioteca.biblioteca.Excepciones.MisExcepciones;
import com.biblioteca.biblioteca.entidades.Autor;
import com.biblioteca.biblioteca.entidades.Editorial;
import com.biblioteca.biblioteca.entidades.Libro;
import com.biblioteca.biblioteca.repositorio.AutorRepositorio;
import com.biblioteca.biblioteca.repositorio.EditorialRepositorio;
import com.biblioteca.biblioteca.repositorio.LibroRepositorio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer Ejemplares, String idAutor, String idEditorial) throws MisExcepciones {

        validar(isbn, titulo, Ejemplares, idAutor, idEditorial);
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();
        Autor autor = autorRepositorio.findById(idAutor).get();
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAlta(new Date());
        libro.setEditorial(editorial);
        libro.setAutor(autor);

        libroRepositorio.save(libro);

    }

    public List<Libro> ListarLibros() {
        List<Libro> libro = new ArrayList();
        libro = libroRepositorio.findAll();
        return libro;
    }

    @Transactional
    public void modificarLibro(Long isbn, String titulo, Integer Ejemplares, String idAutor, String idEditorial) throws MisExcepciones {
        validar(isbn, titulo, Ejemplares, idAutor, idEditorial);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Autor autor = new Autor();
        if (respuestaAutor.isPresent()) {
            autor = respuestaAutor.get();
        }
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);
        Editorial editorial = new Editorial();
        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();
        }

        Optional<Libro> respuestaLibro = libroRepositorio.findById(isbn);
        Libro libro = new Libro();
        if (respuestaLibro.isPresent()) {
            libro = respuestaLibro.get();
        }
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setEjemplares(Ejemplares);
        libroRepositorio.save(libro);

    }

    public void validar(Long isbn, String titulo, Integer Ejemplares, String idAutor, String idEditorial) throws MisExcepciones {

        if (isbn == null) {
            throw new MisExcepciones("el isbn no puede ser nulo");
        }
        if (titulo.isEmpty() || titulo == null) {
            throw new MisExcepciones("el titulo no puede estar vacio o ser nulo");
        }
        if (Ejemplares == null) {
            throw new MisExcepciones("el numero de ejemplares no puede ser nulo");
        }

        if (idAutor == null || idAutor.isEmpty()) {
            throw new MisExcepciones("el id del autor, no puede ser nullo o estar vacio");
        }
        if (idEditorial == null || idEditorial.isEmpty()) {
            throw new MisExcepciones("la id de la editorial, no puede estar vacio o ser nulo");
        }

    }

    public Libro getOne(Long isbn) {
        return libroRepositorio.getOne(isbn);
    }
    @Transactional
    public void eliminarLibro(Long isbn){
      Optional<Libro> respuestaLibro = libroRepositorio.findById(isbn);
        Libro libro = new Libro();
        if (respuestaLibro.isPresent()) {
            libro = respuestaLibro.get();
        }
        libroRepositorio.delete(libro);
    }

}
