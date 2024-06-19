/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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
import vista.Interfaz_Resultados;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Torneo {

    private int jugadorActual;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Jugador> jugadoresCompletados;
    private ArrayList<Palabra> palabras;
    private int totRondas;
    private int ronda = 1;
    private Interfaz_Juego intJuego;
    private Interfaz_Multijugador intMultijugador;
    private Map<Jugador, Palabra> palabrasJugador;
    private Map<Jugador, StringBuilder> palabrasOcultas;
    private Map<Jugador, Set<Character>> letrasUsadasJugador;
    private Timer delayTimer;

    public Torneo(ArrayList<Jugador> jugadores, int totRondas, Interfaz_Juego intJuego, Interfaz_Multijugador intMultijugador) {
        this.jugadorActual = 0;
        this.jugadores = jugadores;
        this.jugadoresCompletados = new ArrayList<>();
        this.palabras = new ArrayList<>();
        this.totRondas = totRondas;
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

        if (palabras.size() < (totRondas * this.jugadores.size())) {
            while (palabras.size() < (totRondas * this.jugadores.size())) {
                String nuevaPalabra = JOptionPane.showInputDialog(null, "Según el número de rondas, se necesita una palabra diferente y hacen falta " + ((totRondas * this.jugadores.size()) - this.palabras.size()) + " palabra/s\nIngrese una nueva palabra: ");
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
    // Si no hay jugadores, no hacer nada
    if (jugadores.isEmpty()) {
        System.out.println("jug: " + Arrays.toString(this.jugadores.toArray()));
        System.out.println("comp: " + Arrays.toString(this.jugadoresCompletados.toArray()));
        return;
    }

    // Verificar si todos los jugadores han completado o agotado sus intentos
    boolean todosCompletadosOAgotados = true;
    for (Jugador jugador : jugadores) {
        if (!jugador.getAdivino() && jugador.getIntentos() > 0) {
            todosCompletadosOAgotados = false;
            break;
        }
    }

    // Si todos los jugadores han completado o agotado sus intentos, no hacer nada
    if (todosCompletadosOAgotados) {
        
        return;
    }

    do {
        System.out.println("do while");
        this.jugadorActual = (this.jugadorActual + 1) % jugadores.size();
    } while (jugadores.get(this.jugadorActual).getAdivino() || jugadores.get(this.jugadorActual).getIntentos() == 0);

    this.actualizarInterfaz();
}


    public void jugarRonda() {
        if (this.ronda <= this.totRondas) {
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
                            jugador.restarPuntaje(5); // Penalización por letra incorrecta
                            JOptionPane.showMessageDialog(null, "Letra incorrecta. Vidas restantes: " + jugador.getIntentos());
                            this.cambiarImagen(jugador);
                        } else {
                            jugador.sumarPuntaje(10); // Puntos por letra correcta
                            actualizarPalabraOculta(palabra, palabraOculta, letra);
                        }

                        this.actualizarInterfaz();
                        if (!palabra.estaCompleta()) {
                            delayTimer.restart(); // Iniciar el timer para esperar antes de pasar al siguiente jugador
                        } else {
                            jugador.setAdivino(true);
                            jugador.sumarPuntaje(50); // Bonificación por completar la palabra
                            jugador.sumarPuntaje(jugador.getIntentos() * 10); // Puntos por intentos restantes
                            actualizarInterfaz();
                           JOptionPane.showMessageDialog(null,"adivino");
                            delayTimer.restart();
                            siguiente(); // Si ha completado la palabra, pasar automáticamente al siguiente jugador
                         
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese solo un carácter.");
                }
            }

            if (jugador.getIntentos() == 0) {
                JOptionPane.showMessageDialog(null, "Se terminó el juego para " + jugador.getNombre());
                siguiente();
               
            }

            this.intJuego.letraJugador1.setText("");

            // Verificar si todos los jugadores han adivinado o agotado sus intentos
            boolean todosTerminaron = true;
            for (Jugador j : jugadores) {
                if (!j.getAdivino() && j.getIntentos() > 0) {
                    todosTerminaron = false;
                    break;
                }
            }

            if (todosTerminaron) {
                this.ronda++;
                if (this.ronda <= this.totRondas) {
                    // Reiniciar los estados de los jugadores para la siguiente ronda, si es necesario
                    for (Jugador j : jugadores) {
                        j.resetearVidas();
                        j.setAdivino(false);
                    }
                    this.prepararPalabrasJugadores();
                    this.jugadorActual = 0; // Reiniciar al primer jugador
                    this.actualizarInterfaz();
                } else {
                    // Fin del torneo, mostrar resultados
                    this.intJuego.dispose();
                    Interfaz_Resultados r = new Interfaz_Resultados();
                    r.setVisible(true);
                }
            }
        }
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
        this.intJuego.jLabelRonda.setText("Ronda: " + this.ronda + "/" + this.totRondas);
        this.intJuego.palabraJugador1.setText(palabrasOcultas.get(jugador).toString());
        this.intJuego.imagenJugador1.setIcon(new javax.swing.ImageIcon(getClass().getResource(jugador.getImagen())));
        this.intJuego.jLabelIntentos.setText("Intentos: " + jugador.getIntentos());
        this.intJuego.jLabelJugador.setText("Turno de " + jugador.getNombre());
        this.intJuego.jLabelPuntuacion.setText("Puntuación: " + jugador.getPuntaje());
    }

    public ArrayList<Jugador> getJugadores() {
        return this.jugadores;
    }
}
