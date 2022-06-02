package com.xcheko51x.adminbibliotecaapp.Clases;

public class Libro {

    private String isbn;
    private String portada;
    private String nom_libro;
    private String autor;
    private String descripcion;
    private String editorial;
    private String anio_publicacion;
    private String edicion;
    private int existencias;
    private String categoria;

    public Libro() { }

    public Libro(
            String isbn,
            String portada,
            String nom_libro,
            String autor,
            String descripcion,
            String editorial,
            String anio_publicacion,
            String edicion,
            int existencias,
            String categoria) {
        this.isbn = isbn;
        this.portada = portada;
        this.nom_libro = nom_libro;
        this.autor = autor;
        this.descripcion = descripcion;
        this.editorial = editorial;
        this.anio_publicacion = anio_publicacion;
        this.edicion = edicion;
        this.existencias = existencias;
        this.categoria = categoria;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getNom_libro() {
        return nom_libro;
    }

    public void setNom_libro(String nom_libro) {
        this.nom_libro = nom_libro;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getAnio_publicacion() {
        return anio_publicacion;
    }

    public void setAnio_publicacion(String anio_publicacion) {
        this.anio_publicacion = anio_publicacion;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
