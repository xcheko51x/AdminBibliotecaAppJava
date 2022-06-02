package com.xcheko51x.adminbibliotecaapp.Clases;

public class Categoria {

    private String idCategoria;
    private String nomCategoria;

    public Categoria() { }

    public Categoria(String idCategoria, String nomCategoria) {
        this.idCategoria = idCategoria;
        this.nomCategoria = nomCategoria;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomCategoria() {
        return nomCategoria;
    }

    public void setNomCategoria(String nomCategoria) {
        this.nomCategoria = nomCategoria;
    }

    @Override
    public String toString() {
        return this.idCategoria+","+this.nomCategoria;
    }
}
