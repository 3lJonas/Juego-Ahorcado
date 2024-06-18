/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juegoAhorcado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author ASUS GAMER
 */
    public class Resultados {
        private ArrayList<Jugador> jugadores=new ArrayList<>();

        public ArrayList<Jugador> getJugadores() {
            return this.jugadores;
        }

        public void addJugadores(Jugador jugador) {
            this.jugadores.add(jugador);
        }
        
         public void ordenarDecreciente() {
        Collections.sort(this.jugadores, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.getPuntaje(), j1.getPuntaje());
            }
        });
    }
    }
