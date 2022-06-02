package com.xcheko51x.adminbibliotecaapp.Clases;

public class Editorial {

    private String idEditorial;
    private String nomEditorial;

    public Editorial() { }

    public Editorial(String idEditorial, String nomEditorial) {
        this.idEditorial = idEditorial;
        this.nomEditorial = nomEditorial;
    }

    public String getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(String idEditorial) {
        this.idEditorial = idEditorial;
    }

    public String getNomEditorial() {
        return nomEditorial;
    }

    public void setNomEditorial(String nomEditorial) {
        this.nomEditorial = nomEditorial;
    }


    @Override
    public String toString() {
        return this.idEditorial+","+this.nomEditorial;
    }
}
