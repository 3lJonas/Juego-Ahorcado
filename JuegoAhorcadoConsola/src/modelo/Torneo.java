/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import juegoAhorcado.Jugador;
import juegoAhorcado.Palabra;
import vista.Interfaz_Juego;
import vista.Interfaz_Menu;
import vista.Interfaz_Multijugador;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Torneo {

    private int jugadorActual;
    private Jugador jugador;
    private Palabra palabra;
    private ArrayList<Palabra> palabras;
    private int rondas;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Palabra> palabrasUsadas;
    private StringBuilder palabraOculta;
    private Set<Character> letrasUsadas;
    private Interfaz_Juego intJuego;
    private Interfaz_Multijugador intMultijugador;

    public Torneo(ArrayList<Jugador> jugadores, int rondas, Interfaz_Juego intJuego, Interfaz_Multijugador intMultijugador) {
        this.jugadorActual = 0;
        this.jugadores = jugadores;
        this.palabras = new ArrayList<>();
        this.rondas = rondas;
        this.jugadores = new ArrayList<>();
        this.palabrasUsadas = new ArrayList<>();
        this.intJuego = intJuego;
        this.intMultijugador = intMultijugador;
        this.letrasUsadas = new HashSet<>();
        this.jugador = jugadores.get(0);
        this.palabraOculta = new StringBuilder();
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

    public void iniciarTorneo() {
        this.cargarPalabras();
        this.actualizarInterfaz(this.jugador);
    }

    private void siguiente() {

        if (jugadorActual != (jugadores.size() - 1)) {
            this.jugador = jugadores.get(jugadorActual);
            this.jugadorActual++;
        } else {
            this.jugadorActual = 0;
            jugador = jugadores.get(jugadorActual);

        }

    }

    private void prepararPalabra() {
        this.palabra = seleccionarPalabra();
        for (int i = 0; i < this.palabra.mostrarPalabra().length(); i++) {
            this.palabraOculta.append("*");
        }
    }

    public void jugarRonda() {

        if (!palabra.estaCompleta() && this.jugador.getIntentos() > 0) {
            if (this.intJuego.letraJugador1.getText().length() == 1) {
                char letra = this.intJuego.letraJugador1.getText().toUpperCase().charAt(0);
                if (!palabra.adivinarLetra(letra)) {
                    jugador.decrementarVidas();
                    System.out.println("Letra incorrecta. Vidas restantes: " + jugador.getIntentos());

                } else {
                    System.out.println("Si : "+this.palabra.mostrarPalabra());
                    this.jugador.setPalabra(new Palabra(this.palabra.mostrarPalabra()));
                    this.actualizarInterfaz(this.jugador);
                }
            } else {
                System.out.println("Ingrese solo un caracter");
            }

        } else {
            System.out.println("Se termino su juego");
            this.siguiente();
            this.actualizarInterfaz(this.jugador);
        }
        if (palabra.estaCompleta()) {
            this.jugador.sumarPuntaje(120);
            System.out.println(this.jugador.getNombre() + " ha adivinado la palabra!");
        }

    }

    public Palabra seleccionarPalabra() {
        Random random = new Random();
        Palabra palabra = palabras.get(random.nextInt(palabras.size()));
        palabras.remove(palabra);
        palabrasUsadas.add(palabra);
        return palabra;
    }

    //
//    public boolean adivinarLetra(char letra, Jugador jugador) {
//        if (letrasUsadas.contains(letra)) {
//            return false;
//        }
//
//        letrasUsadas.add(letra);
//
//        boolean adivinado = false;
//        for (int i = 0; i < palabra.getPalabra().length(); i++) {
//            if (palabra.getPalabra().charAt(i) == letra) {
//                palabraOculta.setCharAt(i, letra);
//                adivinado = true;
//            }
//        }
//
//        if (!adivinado) {
//            jugador.setIntentos(jugador.getIntentos() - 1);
//        }
//
//        return adivinado;
//    }
//    public boolean palabraAdivinada() {
//        return !palabraOculta.toString().contains("*");
//    }
//    public boolean juegoTerminado(Jugador jugador) {
//        return jugador.getIntentos() == 0 || palabraAdivinada();
//    }
//    public int obtenerPuntaje(Jugador jugador) {
//        int puntos = palabraAdivinada() ? 120 : 0;
//        jugador.sumarPuntaje(puntos);
//        return jugador.getPuntaje();
//    }
    public StringBuilder getPalabraOculta() {
        return this.palabraOculta;
    }
    //

    private void actualizarInterfaz(Jugador jugador) {
        this.prepararPalabra();
        System.out.println(this.jugador.getPalabra().mostrarPalabra());
        this.intJuego.palabraJugador1.setText(this.palabra.mostrarPalabra());
        this.intJuego.imagenJugador1.setIcon(new javax.swing.ImageIcon(getClass().getResource(jugador.getImagen())));
        this.intJuego.jLabelIntentos.setText("Intentos: " + jugador.getIntentos());
        this.intJuego.jLabelJugador.setText("Turno de " + jugador.getNombre());
    }

    /*   public void mostrarResultados() {
        Collections.sort(jugadores, (j1, j2) -> j2.getPuntuacion() - j1.getPuntuacion());
        System.out.println("Resultados del torneo:");
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            System.out.println((i + 1) + ". " + jugador.getNombre() + " - Puntos: " + jugador.getPuntuacion());
        }
    }*/
    public ArrayList<Jugador> getJugadores() {
        return this.jugadores;
    }

}
