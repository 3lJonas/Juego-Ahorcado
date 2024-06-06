/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoahorcadoconsola;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.JOptionPane;
import vista.Interfaz_Juego;
import vista.Interfaz_Multijugador;
import vista.Interfaz_UnJugador;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Torneo {

    private ArrayList<Palabra> palabras;
    private ArrayList<Jugador> jugadores;
    private Interfaz_Juego intJuego;
    private int rondas;

    public Torneo(Interfaz_Juego intJuego,ArrayList<Jugador> jugadores,int rondas) {
        this.palabras = new ArrayList<>();
        this.jugadores = jugadores;
        this.rondas=rondas;
        this.intJuego=intJuego;
        this.cargarPalabras();
        this.jugarTorneo();
        
    }

    private void cargarPalabras() {
        palabras.add(new Palabra("JAVA"));
        palabras.add(new Palabra("PROGRAMACION"));
        palabras.add(new Palabra("ALGORITMO"));
        palabras.add(new Palabra("COMPUTADORA"));
        palabras.add(new Palabra("TECNOLOGIA"));
        palabras.add(new Palabra("EDUCACION"));
        palabras.add(new Palabra("HIPOPOTAMO"));
        

        if (palabras.size() < Math.pow(rondas, 2)) {
            while (palabras.size() < Math.pow(rondas, 2)) {
                String nuevaPalabra = JOptionPane.showInputDialog(null, "Segun el numero de rondas es una palabra diferente y hace falta "+(Math.pow(rondas, 2)-this.palabras.size())+" Palabra/s\n Ingrese una nueva palabra: ");

                if (!nuevaPalabra.isEmpty()) {
                    palabras.add(new Palabra(nuevaPalabra));
                }
            }
        }
    }

    public void jugarTorneo() {
        ArrayList<Palabra> palabrasUsadas = new ArrayList<>();
        for (int ronda = 0; ronda < rondas; ronda++) {
            Palabra palabraActual = seleccionarPalabra(palabrasUsadas);
            for (Jugador jugador : jugadores) {
                Ronda rondaActual = new Ronda(palabraActual);
                jugarRonda(rondaActual, jugador);
            }
            palabrasUsadas.add(palabraActual);
        }
        mostrarResultados();
    }

    private Palabra seleccionarPalabra(ArrayList<Palabra> palabrasUsadas) {
        ArrayList<Palabra> palabrasDisponibles = new ArrayList<>(palabras);
        palabrasDisponibles.removeAll(palabrasUsadas);
        Collections.shuffle(palabrasDisponibles);
        return palabrasDisponibles.get(0);
    }

    private void jugarRonda(Ronda ronda, Jugador jugador) {
        
        while (!ronda.juegoTerminado()) {
            ronda.mostrarEstado(jugador);
            String letra = scanner.nextLine().toUpperCase();

            if (letra.isEmpty() || letra.length() > 1) {
                System.out.println("Entrada inválida. Ingresa una sola letra.");
                continue;
            }

            char letraChar = letra.charAt(0);
            boolean adivinado = ronda.adivinarLetra(letraChar);

            if (!adivinado) {
                System.out.println("La letra '" + letraChar + "' no está en la palabra.");
            }
        }

        int puntos = ronda.obtenerPuntaje();
        if (ronda.palabraAdivinada()) {
            System.out.println("\n" + jugador.getNombre() + " adivinó la palabra: " + ronda.palabra.getPalabra());
            jugador.sumarPuntaje(puntos);
        } else {
            System.out.println("\n" + jugador.getNombre() + " no pudo adivinar la palabra: " + ronda.palabra.getPalabra());
        }
    }

    private void mostrarResultados() {
        Collections.sort(jugadores, (j1, j2) -> Integer.compare(j2.getPuntaje(), j1.getPuntaje()));

        System.out.println("\nResultados finales:");
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            System.out.println((i + 1) + ". " + jugador.getNombre() + " - Puntaje: " + jugador.getPuntaje());
        }
    }
}
