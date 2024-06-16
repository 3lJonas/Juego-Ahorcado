/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import juegoAhorcado.Jugador;
import juegoAhorcado.Palabra;
import vista.Interfaz_Juego;
import vista.Interfaz_Multijugador;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Torneo {

    private int jugadorActual;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Jugador> jugadoresCompletados;
    private ArrayList<Palabra> palabras;
    private int rondas;
    private Interfaz_Juego intJuego;
    private Interfaz_Multijugador intMultijugador;
    private Map<Jugador, Palabra> palabrasJugador;
    private Map<Jugador, StringBuilder> palabrasOcultas;
    private Map<Jugador, Set<Character>> letrasUsadasJugador;
    private Timer delayTimer;

    public Torneo(ArrayList<Jugador> jugadores, int rondas, Interfaz_Juego intJuego, Interfaz_Multijugador intMultijugador) {
        this.jugadorActual = 0;
        this.jugadores = jugadores;
        this.jugadoresCompletados = new ArrayList<>();
        this.palabras = new ArrayList<>();
        this.rondas = rondas;
        this.intJuego = intJuego;
        this.intMultijugador = intMultijugador;
        this.palabrasJugador = new HashMap<>();
        this.palabrasOcultas = new HashMap<>();
        this.letrasUsadasJugador = new HashMap<>();
        this.delayTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siguiente();
                delayTimer.stop(); // Detener el timer después de ejecutar la acción
            }
        });

        cargarPalabras();
        prepararPalabrasJugadores();
    }

    private void cargarPalabras() {
        palabras.add(new Palabra("JAVA"));

        if (palabras.size() < (rondas * this.jugadores.size())) {
            while (palabras.size() < (rondas * this.jugadores.size())) {
                String nuevaPalabra = JOptionPane.showInputDialog(null, "Según el número de rondas, se necesita una palabra diferente y hacen falta " + ((rondas * this.jugadores.size()) - this.palabras.size()) + " palabra/s\nIngrese una nueva palabra: ");
                if (!nuevaPalabra.isEmpty()) {
                    palabras.add(new Palabra(nuevaPalabra.toUpperCase()));
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese una palabra válida");
                }
            }
        }
    }

    private void prepararPalabrasJugadores() {
        Random random = new Random();
        for (Jugador jugador : jugadores) {
            Palabra palabra = palabras.get(random.nextInt(palabras.size()));
            palabras.remove(palabra);
            palabrasJugador.put(jugador, palabra);
            StringBuilder palabraOculta = new StringBuilder();
            for (int i = 0; i < palabra.mostrarPalabra().length(); i++) {
                palabraOculta.append("*");
            }
            palabrasOcultas.put(jugador, palabraOculta);
            letrasUsadasJugador.put(jugador, new HashSet<>());
        }
    }

    public void iniciarTorneo() {
        this.actualizarInterfaz();
    }

    private void siguiente() {
        if (this.jugadores.isEmpty()) {
            //aqui va a ir el controlador de la pagina de resultados
        }
        // Verificar si el jugador actual ha completado la palabra
        Jugador jugadorActual = jugadores.get(this.jugadorActual);
        if (jugadorActual.getAdivino()) {
            jugadoresCompletados.add(jugadorActual);
            jugadores.remove(jugadorActual); // Mover jugador a la lista de completados
            // No incrementar jugadorActual aquí para evitar desfases si se eliminan varios jugadores
        } else {
            this.jugadorActual = (this.jugadorActual + 1) % jugadores.size();
        }
        this.actualizarInterfaz();
    }

    public void jugarRonda() {
        Jugador jugador = jugadores.get(jugadorActual);
        Palabra palabra = palabrasJugador.get(jugador);
        StringBuilder palabraOculta = palabrasOcultas.get(jugador);
        Set<Character> letrasUsadas = letrasUsadasJugador.get(jugador);

        if (jugador.getIntentos() > 0) {
            if (this.intJuego.letraJugador1.getText().length() == 1) {
                char letra = this.intJuego.letraJugador1.getText().toUpperCase().charAt(0);
                if (letrasUsadas.contains(letra)) {
                    JOptionPane.showMessageDialog(null, "La letra '" + letra + "' ya ha sido usada. Por favor, elige otra letra.");
                } else {
                    letrasUsadas.add(letra);
                    if (!palabra.adivinarLetra(letra)) {
                        jugador.decrementarVidas();
                        JOptionPane.showMessageDialog(null, "Letra incorrecta. Vidas restantes: " + jugador.getIntentos());
                        this.cambiarImagen(jugador);
                    } else {
                        actualizarPalabraOculta(palabra, palabraOculta, letra);
                    }

                    this.actualizarInterfaz();
                    // Verificar si el jugador ha adivinado la palabra después de actualizar la interfaz
                    if (!palabra.estaCompleta()) {
                        delayTimer.restart(); // Iniciar el timer para esperar antes de pasar al siguiente jugador
                    } else {
                        jugador.setAdivino(true);
                        siguiente(); // Si ha completado la palabra, pasar automáticamente al siguiente jugador
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese solo un carácter.");
            }
        }

        if (jugador.getIntentos() == 0) {
            JOptionPane.showMessageDialog(null, "Se terminó el juego para " + jugador.getNombre());
        }

        this.intJuego.letraJugador1.setText("");
    }

    private void actualizarPalabraOculta(Palabra palabra, StringBuilder palabraOculta, char letra) {
        for (int i = 0; i < palabra.mostrarPalabra().length(); i++) {
            if (palabra.mostrarPalabra().charAt(i) == letra) {
                palabraOculta.setCharAt(i, letra);
            }
        }
    }

    private void cambiarImagen(Jugador jugador) {
        switch (jugador.getIntentos()) {
            case 5:
                jugador.setImagen("/Imagenes/Recurso 4.png");
                break;
            case 4:
                jugador.setImagen("/Imagenes/Recurso 5.png");
                break;
            case 3:
                jugador.setImagen("/Imagenes/Recurso 6.png");
                break;
            case 2:
                jugador.setImagen("/Imagenes/Recurso 7.png");
                break;
            case 1:
                jugador.setImagen("/Imagenes/Recurso 8.png");
                break;
            case 0:
                jugador.setImagen("/Imagenes/Recurso 9.png");
                break;
            default:
                break;
        }
    }

    private void actualizarInterfaz() {
        Jugador jugador = jugadores.get(jugadorActual);
        this.intJuego.jLabelRonda.setText("Ronda: " + (this.jugadorActual + 1));
        this.intJuego.palabraJugador1.setText(palabrasOcultas.get(jugador).toString());
        this.intJuego.imagenJugador1.setIcon(new javax.swing.ImageIcon(getClass().getResource(jugador.getImagen())));
        this.intJuego.jLabelPuntuacion.setText("Intentos: " + jugador.getIntentos());
        this.intJuego.jLabelJugador.setText("Turno de " + jugador.getNombre());
    }

    public ArrayList<Jugador> getJugadores() {
        return this.jugadores;
    }
}