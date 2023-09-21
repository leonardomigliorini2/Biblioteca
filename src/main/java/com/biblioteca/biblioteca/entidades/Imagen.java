
package com.biblioteca.biblioteca.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Imagen {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name ="uuid", strategy ="uuid2")
    private String id;
    private String mime; //atributo que asigna el formato del archivo
    private String nombre; 
    
    @Lob @Basic(fetch=FetchType.LAZY) //@Basic se define que el tipo de carga va a ser perezozo//@Lob le informa a spring que el dato puede ser grande
    private byte [] contenido;

    public Imagen() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
    
    
    
    
}//los tres primeros atributos se traen apenas llamamos al objeto, el archivo contenido se carga solo cuando es llamado a traves de un GET.
