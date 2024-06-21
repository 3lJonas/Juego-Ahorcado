/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import vista.Interfaz_Juego;
import vista.Interfaz_Menu;
import vista.Interfaz_Multijugador;
import vista.Interfaz_Resultados;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Torneo {

    private MediaPlayer mediaPlayerAcciones;
    private Interfaz_Resultados intResultados;
    private int jugadorActual;
    private ArrayList<Jugador> jugadores;
    private Resultados resultado;
    private ArrayList<Palabra> palabras;
    private int totRondas;
    private int ronda = 1;
    private Interfaz_Juego intJuego;
    private Interfaz_Multijugador intMultijugador;
    private Interfaz_Menu intMenu;
    private Map<Jugador, Palabra> palabrasJugador;
    private Map<Jugador, StringBuilder> palabrasOcultas;
    private Map<Jugador, Set<Character>> letrasUsadasJugador;
    private Timer delayTimer;
    private boolean isPlayingMusic = false;  // Para rastrear si se está reproduciendo música
    private Queue<Runnable> musicQueue = new LinkedList<>();  // Cola de acciones a ejecutar después de la música

    public Torneo(ArrayList<Jugador> jugadores, int totRondas, Interfaz_Juego intJuego, Interfaz_Multijugador intMultijugador, Interfaz_Resultados intResultados, Interfaz_Menu intMenu) {
        this.intMenu = intMenu;
        this.jugadorActual = 0;
        this.jugadores = jugadores;
        this.intResultados = intResultados;
        new JFXPanel(); // Inicializar JavaFX en una aplicación Swing
        this.palabras = new ArrayList<>();
        this.totRondas = totRondas;
        this.intJuego = intJuego;
        this.intMultijugador = intMultijugador;
        this.palabrasJugador = new HashMap<>();
        this.palabrasOcultas = new HashMap<>();
        this.letrasUsadasJugador = new HashMap<>();
        this.delayTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siguiente();
                delayTimer.stop(); // Detener el timer después de ejecutar la acción
            }
        });

        cargarPalabras();

    }

    private void cargarPalabras() {
        palabras.add(new Palabra("HIPOPOTAMO"));
        palabras.add(new Palabra("ORNITORRINCO"));
        Icon icono = UIManager.getIcon("OptionPane.questionIcon");

        while (palabras.size() < (totRondas * this.jugadores.size())) {
            String nuevaPalabra = JOptionPane.showInputDialog(null, "Según el número de rondas, se necesita una palabra diferente y hacen falta " + ((totRondas * this.jugadores.size()) - this.palabras.size()) + " palabra/s\nIngrese una nueva palabra (mínimo 3 caracteres): ", "Ingreso de nuevas palabras", JOptionPane.QUESTION_MESSAGE);

            // Verificar si el usuario cancela o cierra el cuadro de diálogo
            if (nuevaPalabra == null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea cancelar la entrada de nuevas palabras? ", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, icono);
                if (respuesta == JOptionPane.YES_OPTION) {
                    return;
                } else {
                    continue; // Continuar solicitando una nueva palabra si el usuario elige no cancelar
                }
            }

            if (!nuevaPalabra.isEmpty() && nuevaPalabra.length() >= 3) {
                palabras.add(new Palabra(nuevaPalabra.toUpperCase()));
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese una palabra válida con al menos 3 caracteres.", "Entrada inválida", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Una vez que hay suficientes palabras, continuar
        if (palabras.size() == (totRondas * this.jugadores.size())) {
            this.prepararPalabrasJugadores();
            this.actualizarInterfaz();
            this.intMultijugador.setVisible(false);
            this.intJuego.setVisible(true);
            this.actualizarInterfaz();
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

    private void siguiente() {
        // Si no hay jugadores, no hacer nada
        if (jugadores.isEmpty()) {
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
            this.intJuego.letraJugador1.setEnabled(true);
            this.intJuego.adivinarJugador1.setEnabled(true);
            this.jugadorActual = (this.jugadorActual + 1) % jugadores.size();
            this.intJuego.jLabelJugadorMensaje.setText("");
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
                            this.intJuego.letraJugador1.setEnabled(false);
                            this.intJuego.adivinarJugador1.setEnabled(false);
                            jugador.decrementarVidas();
                            jugador.restarPuntaje(5); // Penalización por letra incorrecta
                            this.intJuego.jLabelJugadorMensaje.setText("Letra incorrecta. ");

                            this.cambiarImagen(jugador);
                        } else {
                            this.intJuego.letraJugador1.setEnabled(false);
                            this.intJuego.adivinarJugador1.setEnabled(false);
                            this.intJuego.jLabelJugadorMensaje.setText("Letra correcta. ");
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

                            this.intJuego.jLabelJugadorMensaje.setText("¡Ha adivinado la palabra!!!");
                            reproducirMusica("recursos/victoria.mp3", this::siguiente);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese solo un carácter.");
                }
            }

            if (jugador.getIntentos() == 0) {
                this.intJuego.jLabelJugadorMensaje.setText("Perdiste tus oportunidades");
                reproducirMusica("recursos/muerte.mp3", this::siguiente);
                
                this.actualizarInterfaz();
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
                    this.intJuego.jLabelJugadorMensaje.setText("¡Va a empezar la ronda N°" + this.ronda + "!!");
                    delayTimer.restart();
                    // Reiniciar los estados de los jugadores para la siguiente ronda, si es necesario
                    for (Jugador j : jugadores) {
                        j.resetearVidas();
                        j.setAdivino(false);
                    }
                    this.prepararPalabrasJugadores();
                    this.jugadorActual = 0; // Reiniciar al primer jugador

                    this.actualizarInterfaz();
                    this.siguiente();
                } else {

                    cargarResultados();
                }
            }
        }
    }

// Método para reproducir música con cola
    private void reproducirMusica(String ruta, Runnable callback) {
        musicQueue.add(() -> {
            isPlayingMusic = true;
            File audioJuego = new File(ruta);
            Media media = new Media(audioJuego.toURI().toString());
            mediaPlayerAcciones = new MediaPlayer(media);

            mediaPlayerAcciones.setOnEndOfMedia(() -> {
                mediaPlayerAcciones.stop();
                isPlayingMusic = false;
                if (callback != null) {
                    callback.run();
                }
                playNextInQueue();  // Reproduce la siguiente acción en la cola
            });

            MediaPlayer mainMediaPlayer = this.intMenu.getMediaPlayer();
            if (mainMediaPlayer != null) {
                mainMediaPlayer.pause();
            }

            mediaPlayerAcciones.play();

            mediaPlayerAcciones.setOnStopped(() -> {
                isPlayingMusic = false;
                if (mainMediaPlayer != null) {
                    mainMediaPlayer.play();
                }
                playNextInQueue();  // Reproduce la siguiente acción en la cola
            });
        });

        if (!isPlayingMusic) {
            playNextInQueue();
        }
    }

// Método para reproducir la siguiente acción en la cola
    private void playNextInQueue() {
        if (!isPlayingMusic && !musicQueue.isEmpty()) {
            Runnable nextAction = musicQueue.poll();
            if (nextAction != null) {
                nextAction.run();
            }
        }
    }

    private void cargarResultados() {
        this.resultado = new Resultados(this.jugadores);
        this.intJuego.dispose();
        this.resultado.ordenarDecreciente();
        this.resultado.actualizarHistorial("Historial de Jugadores.txt");
        this.jugadores = this.resultado.getJugadores(); // aquí ya está ordenada la colección de jugadores

        this.intResultados.setLocationRelativeTo(null);

        // Actualizar los JLabels con los nombres y puntuaciones de los jugadores
        if (jugadores.size() > 0) {
            this.intResultados.jLabelPrimerL.setText(jugadores.get(0).getNombre());
            this.intResultados.jLabelPrimerLP.setText(jugadores.get(0).getPuntaje() + " puntos");
        } else {
            this.intResultados.jLabelPrimerL.setText("");
        }

        if (jugadores.size() > 1) {
            this.intResultados.jLabelSegundoL.setText(jugadores.get(1).getNombre());
            this.intResultados.jLabelSegundoLP.setText(jugadores.get(1).getPuntaje() + " puntos");

        } else {
            this.intResultados.jLabelSegundoL.setText("");
        }

        if (jugadores.size() > 2) {
            this.intResultados.jLabelTercerL.setText(jugadores.get(2).getNombre());
            this.intResultados.jLabelTercerL1.setText(jugadores.get(2).getNombre() + " - " + jugadores.get(2).getPuntaje() + " puntos");
        } else {
            this.intResultados.jLabelTercerL.setText("");
        }

        this.intResultados.setVisible(true);
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
