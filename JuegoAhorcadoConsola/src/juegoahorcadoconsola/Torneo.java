/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoahorcadoconsola;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import vista.Interfaz_Juego;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Torneo {

    private ArrayList<Palabra> palabras;
    private ArrayList<Jugador> jugadores;
    private Interfaz_Juego intJuego;
    private int rondas;
    private int jugadorActual;

    public Torneo(Interfaz_Juego intJuego, ArrayList<Jugador> jugadores, int rondas) {
        this.palabras = new ArrayList<>();
        this.jugadores = jugadores;
        this.rondas = rondas;
        this.intJuego = intJuego;
        this.jugadorActual = 0;
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
                String nuevaPalabra = JOptionPane.showInputDialog(null, "Segun el numero de rondas es una palabra diferente y hace falta " + (Math.pow(rondas, 2) - this.palabras.size()) + " Palabra/s\n Ingrese una nueva palabra: ");
                if (!nuevaPalabra.isEmpty()) {
                    palabras.add(new Palabra(nuevaPalabra));
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese una palabra valida");
                }
            }
        }
    }

    public void jugarTorneo() {
        ArrayList<Palabra> palabrasUsadas = new ArrayList<>();
        for (int ronda = 0; ronda < rondas; ronda++) {
            Palabra palabraActual = seleccionarPalabra(palabrasUsadas);
            for (Jugador jugador : jugadores) {
                Ronda rondaActual = new Ronda(palabraActual, intJuego);
                jugarRonda(rondaActual, jugador);
                turnoSiguiente();
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
        while (!ronda.juegoTerminado(jugador)) {
            ronda.mostrarEstado(jugador);

            String letra = obtenerLetraJugadorActual();
            if (letra.isEmpty() || letra.length() > 1) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Ingresa una sola letra.");
                continue;
            }

            char letraChar = letra.charAt(0);
            boolean adivinado = ronda.adivinarLetra(letraChar, jugador);

            if (!adivinado) {
                JOptionPane.showMessageDialog(null, "La letra '" + letraChar + "' no está en la palabra.");
            }
        }

        int puntos = ronda.obtenerPuntaje(jugador);
        if (ronda.palabraAdivinada()) {
            JOptionPane.showMessageDialog(null, "\n" + jugador.getNombre() + " adivinó la palabra: " + ronda.palabra.getPalabra());
            jugador.sumarPuntaje(puntos);
        } else {
            JOptionPane.showMessageDialog(null, "\n" + jugador.getNombre() + " no pudo adivinar la palabra: " + ronda.palabra.getPalabra());
        }
    }

    private String obtenerLetraJugadorActual() {
        switch (jugadorActual) {
            case 0:
                return intJuego.letraJugador1.getText();
            case 1:
                return intJuego.letraJugador2.getText();
            case 2:
                return intJuego.letraJugador3.getText();
            case 3:
                return intJuego.letraJugador4.getText();
            default:
                return "";
        }
    }

    private void turnoSiguiente() {
        jugadorActual = (jugadorActual + 1) % jugadores.size();
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            switch (i) {
                case 0:
                    intJuego.palabraJugador1.setText(jugador.getNombre() + ": " + jugador.getPuntaje() + " puntos");
                    intJuego.letraJugador1.setEnabled(i == jugadorActual);
                    intJuego.adivinarJugador1.setEnabled(i == jugadorActual);
                    break;
                case 1:
                    intJuego.palabraJugador2.setText(jugador.getNombre() + ": " + jugador.getPuntaje() + " puntos");
                    intJuego.letraJugador2.setEnabled(i == jugadorActual);
                    intJuego.adivinarJugador2.setEnabled(i == jugadorActual);
                    break;
                case 2:
                    intJuego.palabraJugador3.setText(jugador.getNombre() + ": " + jugador.getPuntaje() + " puntos");
                    intJuego.letraJugador3.setEnabled(i == jugadorActual);
                    intJuego.adivinarJugador3.setEnabled(i == jugadorActual);
                    break;
                case 3:
                    intJuego.palabraJugador4.setText(jugador.getNombre() + ": " + jugador.getPuntaje() + " puntos");
                    intJuego.letraJugador4.setEnabled(i == jugadorActual);
                    intJuego.adivinarJugador4.setEnabled(i == jugadorActual);
                    break;
            }
        }
        intJuego.jLabelIntentos.setText("Intentos: " + jugadores.get(jugadorActual).getIntentos());
    }

    private void mostrarResultados() {
        Collections.sort(jugadores, (j1, j2) -> Integer.compare(j2.getPuntaje(), j1.getPuntaje()));

        StringBuilder resultados = new StringBuilder("\nResultados finales:\n");
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            resultados.append((i + 1)).append(". ").append(jugador.getNombre()).append(" - Puntaje: ").append(jugador.getPuntaje()).append("\n");
        }
        JOptionPane.showMessageDialog(null, resultados.toString());
    }
}