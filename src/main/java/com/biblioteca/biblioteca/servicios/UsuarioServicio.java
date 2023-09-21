package com.biblioteca.biblioteca.servicios;

import com.biblioteca.biblioteca.Excepciones.MisExcepciones;
import com.biblioteca.biblioteca.entidades.Imagen;
import com.biblioteca.biblioteca.entidades.Usuario;
import com.biblioteca.biblioteca.enumeraciones.Rol;
import com.biblioteca.biblioteca.repositorio.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ImagenServicio ImagenServicio;

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2) throws MisExcepciones {
        validar(nombre, email, password, password2);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.User);
        Imagen imagen = ImagenServicio.guardar(archivo);
        usuarioRepositorio.save(usuario);
    }

    private void validar(String nombre, String email, String password, String password2) throws MisExcepciones {
        if (nombre.isEmpty() || nombre == null) {
            throw new MisExcepciones("el nombre esta vacio o es nulo");
        }
        if (email.isEmpty() || email == null) {
            throw new MisExcepciones("el email esta vacio o es nulo");
        }
        if (password.isEmpty() || password == null) {
            throw new MisExcepciones("la contraseña esta vacia o es nula");
        }
        if (password2.isEmpty() || password2 == null) {
            throw new MisExcepciones("la confirmacion de contraseña esta vacia o es nula");
        }
        if (!password.equals(password2)) {
            throw new MisExcepciones("error, la contraseña no coincide con su confirmacion");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }

    }

    @Transactional
    public void actualizar(MultipartFile archivo, String idUsuario, String nombre, String email, String password, String password2) throws MisExcepciones {
        validar(nombre, email, password, password2);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setRol(Rol.User);
            String idImagen = new String();
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }
            Imagen imagen = ImagenServicio.actualizar(archivo, idImagen);
            usuario.setImagen(imagen);
            usuarioRepositorio.save(usuario);
        }
    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    public List<Usuario> listarUsuario() {
        List<Usuario> listaUsuario = new ArrayList();
        listaUsuario = usuarioRepositorio.findAll();
        return listaUsuario;
    }

    @Transactional
    public void cambiarRol(String id) {
        System.out.println("id "+id);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        System.out.println("respuesta "+respuesta);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.User)) {
                usuario.setRol(Rol.ADMIN);
                usuarioRepositorio.save(usuario);
            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.User);
                usuarioRepositorio.save(usuario);
            }
        }

    }
}
