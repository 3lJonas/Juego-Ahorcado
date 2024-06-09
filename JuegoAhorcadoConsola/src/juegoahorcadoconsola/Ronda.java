/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoahorcadoconsola;

import java.util.HashSet;
import java.util.Set;
import vista.Interfaz_Juego;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Ronda {

    Palabra palabra;
    private StringBuilder palabraOculta;
    private Set<Character> letrasUsadas;
    private Interfaz_Juego intJuego;

    public Ronda(Palabra palabra, Interfaz_Juego intJuego) {
        this.palabra = palabra;
        this.palabraOculta = new StringBuilder();
        for (int i = 0; i < palabra.getPalabra().length(); i++) {
            this.palabraOculta.append("*");
        }
        this.letrasUsadas = new HashSet<>();
        this.intJuego = intJuego;
    }

    public boolean adivinarLetra(char letra, Jugador jugador) {
        if (letrasUsadas.contains(letra)) {
            return false;
        }

        letrasUsadas.add(letra);

        boolean adivinado = false;
        for (int i = 0; i < palabra.getPalabra().length(); i++) {
            if (palabra.getPalabra().charAt(i) == letra) {
                palabraOculta.setCharAt(i, letra);
                adivinado = true;
            }
        }

        if (!adivinado) {
            jugador.setIntentos(jugador.getIntentos() - 1);
        }

        return adivinado;
    }

    public boolean palabraAdivinada() {
        return !palabraOculta.toString().contains("*");
    }

    public void mostrarEstado(Jugador jugador) {
        switch (jugador.getNombre()) {
            case "Jugador 1":
                intJuego.palabraJugador1.setText(palabraOculta.toString());
                break;
            case "Jugador 2":
                intJuego.palabraJugador2.setText(palabraOculta.toString());
                break;
            case "Jugador 3":
                intJuego.palabraJugador3.setText(palabraOculta.toString());
                break;
            case "Jugador 4":
                intJuego.palabraJugador4.setText(palabraOculta.toString());
                break;
        }
        intJuego.jLabelIntentos.setText("Intentos: " + jugador.getIntentos());
    }

    public boolean juegoTerminado(Jugador jugador) {
        return jugador.getIntentos() == 0 || palabraAdivinada();
    }

    public int obtenerPuntaje(Jugador jugador) {
        int puntos = palabraAdivinada() ? 120 : 0;
        jugador.sumarPuntaje(puntos);
        return jugador.getPuntaje();
    }
}