package com.xcheko51x.adminbibliotecaapp.Clases;

public class Usuario {

    private String idUsuario;
    private String nomUsuario;
    private String contrasena;
    private String estadoUsuario;

    public Usuario() { };

    public Usuario(
            String idUsuario,
            String nomUsuario
    ) {
        this.idUsuario = idUsuario;
        this.nomUsuario = nomUsuario;
    }

    public Usuario(
            String idUsuario,
            String nomUsuario,
            String estadoUsuario
    ) {
        this.idUsuario = idUsuario;
        this.nomUsuario = nomUsuario;
        this.estadoUsuario = estadoUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    @Override
    public String toString() {
        return this.getIdUsuario()+","+this.getNomUsuario();
    }
}
