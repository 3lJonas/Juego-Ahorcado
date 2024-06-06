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
public class Palabra {
     private String palabra;

    public Palabra(String palabra) {
        this.palabra = palabra.toUpperCase();
    }

    public String getPalabra() {
        return palabra;
    }
}
