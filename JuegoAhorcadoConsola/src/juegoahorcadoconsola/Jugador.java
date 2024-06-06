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
    private int puntaje;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void sumarPuntaje(int puntos) {
        puntaje += puntos;
    }
}
