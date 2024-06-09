/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoahorcadoconsola;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Jugador {
    private String nombre;
    private int intentos;
    private int puntaje;
    private String imagen;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
        this.intentos=6;
        this.imagen="/Imagenes/Recurso 1.png";
    }

    public String getNombre() {
        return nombre;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    
    public int getPuntaje() {
        return puntaje;
    }

    public void sumarPuntaje(int puntos) {
        puntaje += puntos;
    }
}
