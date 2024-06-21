/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Palabra {
    private String palabra;
    private ArrayList<Character> letrasAdivinadas;

    public Palabra(String palabra) {
        this.palabra = palabra;
        this.letrasAdivinadas = new ArrayList<>();
        for (int i = 0; i < palabra.length(); i++) {
            letrasAdivinadas.add('*');
        }
    }

    public boolean adivinarLetra(char letra) {
        boolean acierto = false;
        for (int i = 0; i < palabra.length(); i++) {
            if (palabra.charAt(i) == letra) {       
                letrasAdivinadas.set(i, letra);
                acierto = true;
            }
        }
        return acierto;
    }

    public String mostrarPalabra() {
        StringBuilder resultado = new StringBuilder();
        for (Character c : letrasAdivinadas) {
            resultado.append(c);
        }
        return resultado.toString();
    }

    public boolean estaCompleta() {
        return !letrasAdivinadas.contains('*');
    }
}
