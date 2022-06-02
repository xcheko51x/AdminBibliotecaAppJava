package com.xcheko51x.adminbibliotecaapp.Clases;

public class Autor {

    private String idAutor;
    private String nomAutor;

    public Autor() {
    }

    public Autor(String idAutor, String nomAutor) {
        this.idAutor = idAutor;
        this.nomAutor = nomAutor;
    }

    public String getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(String idAutor) {
        this.idAutor = idAutor;
    }

    public String getNomAutor() {
        return nomAutor;
    }

    public void setNomAutor(String nomAutor) {
        this.nomAutor = nomAutor;
    }

    @Override
    public String toString() {
        return this.getIdAutor()+","+this.getNomAutor();
    }
}
