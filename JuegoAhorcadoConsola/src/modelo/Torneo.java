/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import juegoAhorcado.Jugador;
import juegoAhorcado.Palabra;
import vista.Interfaz_Juego;
import vista.Interfaz_Multijugador;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Torneo {

    private int count = 0;
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
        this.palabrasUsadas = new ArrayList<>();
        this.intJuego = intJuego;
        this.intMultijugador = intMultijugador;
        this.letrasUsadas = new HashSet<>();
        this.jugador = jugadores.get(0);
        this.palabraOculta = new StringBuilder();
        cargarPalabras();
        this.prepararPalabra();
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
        if (this.rondas != this.count) {
            if (jugadorActual != (jugadores.size() - 1)) {
                this.jugadorActual++;
                this.letrasUsadas.clear();
                this.jugador = jugadores.get(jugadorActual);

            } else {
                this.jugadorActual = 0;
                this.jugador = jugadores.get(jugadorActual);

            }
            this.count++;
        } else {
            System.out.println("SE ACABO TODO AQUIPONER LA INTERFAZ DE LOS RESULTADOS");
        }

    }

    private void prepararPalabra() {
        this.palabra = seleccionarPalabra();
        for (int i = 0; i < this.palabra.mostrarPalabra().length(); i++) {
            this.palabraOculta.append("*");
        }
    }

    public void jugarRonda() {
        if (this.jugador.getIntentos() > 0) {
            if (this.intJuego.letraJugador1.getText().length() == 1) {
                char letra = this.intJuego.letraJugador1.getText().toUpperCase().charAt(0);
                if (letrasUsadas.contains(letra)) {
                    // Notificar al jugador que la letra ya fue utilizada
                    System.out.println("La letra '" + letra + "' ya ha sido usada. Por favor, elige otra letra.");
                } else {
                    letrasUsadas.add(letra); // Agregar la letra al conjunto de letras usadas
                    if (!palabra.adivinarLetra(letra)) {
                        jugador.decrementarVidas();
                        if (this.jugador.getIntentos() == 0) {
                            System.out.println("Se termino su juego");
                            this.siguiente();
                            this.prepararPalabra();
                            this.actualizarInterfaz(this.jugador);
                        }
                        System.out.println("Letra incorrecta. Vidas restantes: " + jugador.getIntentos());
                        System.out.println("Jugadores: " + jugadores.toString());
                        this.cambiarImagen();
                        this.actualizarInterfaz(this.jugador);
                    } else {
                        this.jugador.setPalabra(new Palabra(this.palabra.mostrarPalabra()));
                        this.actualizarInterfaz(this.jugador);
                    }
                }
            } else {
                System.out.println("Ingrese solo un caracter");
            }
        } else {
            System.out.println("Se termino su juego");
            this.siguiente();
            this.prepararPalabra();
            this.actualizarInterfaz(this.jugador);
        }
        if (palabra.estaCompleta()) {
            this.jugador.sumarPuntaje(120);
            System.out.println(this.jugador.getNombre() + " ha adivinado la palabra!");
            this.siguiente();
            this.prepararPalabra();
            this.actualizarInterfaz(this.jugador);
        }
        this.intJuego.letraJugador1.setText("");
    }

    public Palabra seleccionarPalabra() {
        Random random = new Random();
        Palabra palabra = palabras.get(random.nextInt(palabras.size()));
        palabras.remove(palabra);
        palabrasUsadas.add(palabra);
        return palabra;
    }

    private void cambiarImagen() {
        switch (this.jugador.getIntentos()) {
            case 5:
                this.jugador.setImagen("/Imagenes/Recurso 4.png");
                break;
            case 4:
                this.jugador.setImagen("/Imagenes/Recurso 5.png");
                break;
            case 3:
                this.jugador.setImagen("/Imagenes/Recurso 6.png");
                break;
            case 2:
                this.jugador.setImagen("/Imagenes/Recurso 7.png");
                break;
            case 1:
                this.jugador.setImagen("/Imagenes/Recurso 8.png");
                break;
            case 0:
                this.jugador.setImagen("/Imagenes/Recurso 9.png");
                break;

            default:
                
        }
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

        this.intJuego.jLabelRonda.setText("Ronda: " + (this.count + 1));
        this.intJuego.palabraJugador1.setText(this.palabra.mostrarPalabra());
        this.intJuego.imagenJugador1.setIcon(new javax.swing.ImageIcon(getClass().getResource(jugador.getImagen())));
        this.intJuego.jLabelPuntuacion.setText("Intentos: " + jugador.getIntentos());
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
