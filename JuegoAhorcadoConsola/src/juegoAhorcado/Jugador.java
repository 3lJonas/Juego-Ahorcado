/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoAhorcado;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Jugador {

    private String nombre;
    private int intentos;
    private int puntaje;
    private String imagen;
    private Palabra palabra;
    private boolean adivino;

    public Jugador(String nombre) {

        this.nombre = nombre;
        this.puntaje = 0;
        this.intentos = 6;
        this.imagen = "/Imagenes/Recurso 1.png";
        this.palabra = new Palabra("");
        this.adivino = false;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIntentos() {
        return intentos;
    }

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getPuntaje() {
        return this.puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public void sumarPuntaje(int puntos) {
        puntaje += puntos;
    }

    public void restarPuntaje(int puntos) {
        this.puntaje -= puntos;
    }

    public Palabra getPalabra() {
        return this.palabra;
    }

    public void setPalabra(Palabra palabra) {
        this.palabra = palabra;
    }

    public void decrementarVidas() {
        this.intentos--;
    }

    public boolean getAdivino() {
        return this.adivino;
    }

    public void setAdivino(boolean adivino) {
        this.adivino = adivino;
    }

    public void resetearVidas() {
        this.adivino = false;
        this.intentos = 6;
    }

}
