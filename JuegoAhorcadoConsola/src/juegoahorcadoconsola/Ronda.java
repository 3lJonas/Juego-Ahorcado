/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoahorcadoconsola;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Ronda {
    Palabra palabra;
    private StringBuilder palabraOculta;
    private Set<Character> letrasUsadas;
    private int intentosRestantes;
    private String[] ahorcado;

    public Ronda(Palabra palabra) {
        this.palabra = palabra;
        this.palabraOculta = new StringBuilder();
        for (int i = 0; i < palabra.getPalabra().length(); i++) {
            this.palabraOculta.append("*");
        }
        this.letrasUsadas = new HashSet<>();
        this.intentosRestantes = 6;
        this.ahorcado = new String[]{
            "+---+\n    |\n    |\n    |\n    |\n    |\n=========",
            "+---+\n    |\n    |\n    |\n    |\n    |\n====o====",
            "+---+\n    |\n    |\n    |\n    |\n    |\n======o===",
            "+---+\n    |\n    |\n    |\n    |\n    |\n=====o====",
            "+---+\n    |\n    |\n    |\n    |\n    |\n=====o====\n    /     ",
            "+---+\n    |\n    |\n    |\n    |\n    |\n=====o====\n    /\\    ",
            "+---+\n    |\n    |\n    |\n    |\n    |\n=====o====\n    /\\    \n   /      ",
            "+---+\n    |\n    |\n    |\n    |\n    |\n=====o====\n    /\\    \n   / \\    "
        };
    }

    public boolean adivinarLetra(char letra) {
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
            intentosRestantes--;
        }

        return adivinado;
    }

    public boolean palabraAdivinada() {
        return !palabraOculta.toString().contains("*");
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    public void mostrarEstado(Jugador jugador) {
        System.out.print("\nPalabra: " + palabraOculta + "\nIntentos restantes: " + intentosRestantes + "\n" + ahorcado[6 - intentosRestantes] + "\n\n");
        System.out.print(jugador.getNombre() + ", adivina una letra: ");
    }

    public boolean juegoTerminado() {
        return intentosRestantes == 0 || palabraAdivinada();
    }

    public int obtenerPuntaje() {
        return intentosRestantes;
    }
}
