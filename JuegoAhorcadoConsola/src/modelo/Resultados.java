/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.table.DefaultTableModel;
import vista.Interfaz_Menu;
import vista.Interfaz_Puntajes;

/**
 *
 * @author ASUS GAMER
 */
public class Resultados {

    private ArrayList<Jugador> jugadores;

    public Resultados(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public ArrayList<Jugador> getJugadores() {
        return this.jugadores;
    }

    public void ordenarDecreciente() {
        Collections.sort(this.jugadores, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.getPuntaje(), j1.getPuntaje());
            }
        });
    }

    public void actualizarHistorial(String archivoHistorial) {
        ArrayList<Jugador> historialJugadores = leerHistorial(archivoHistorial);
        historialJugadores.addAll(this.jugadores);
        ordenarDecreciente(historialJugadores);
        escribirHistorial(archivoHistorial, historialJugadores);
    }

    private ArrayList<Jugador> leerHistorial(String archivoHistorial) {
        ArrayList<Jugador> historialJugadores = new ArrayList<>();
        File archivo = new File(archivoHistorial);
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",");
                    String nombre = datos[0];
                    int puntaje = Integer.parseInt(datos[1]);
                    Jugador jugador=new Jugador(nombre);
                    jugador.setPuntaje(puntaje);
                    historialJugadores.add(jugador);
                    
                }
            } catch (IOException e) {
                
            }
        }
        return historialJugadores;
    }

    private void escribirHistorial(String archivoHistorial, ArrayList<Jugador> jugadores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoHistorial))) {
            for (Jugador jugador : jugadores) {
                bw.write(jugador.getNombre() + "," + jugador.getPuntaje());
                bw.newLine();
            }
        } catch (IOException e) {
           
        }
    }

    private void ordenarDecreciente(ArrayList<Jugador> jugadores) {
        Collections.sort(jugadores, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.getPuntaje(), j1.getPuntaje());
            }
        });
    }
    
      public void actualizarJTable( Interfaz_Puntajes intPuntajes) {
          String archivoHistorial="Historial de Jugadores.txt";
        ArrayList<Jugador> historialJugadores = leerHistorial(archivoHistorial);
        ordenarDecreciente(historialJugadores);

        String[] columnNames = {"Posición", "Nombre", "Puntaje"};
        String[][] data = new String[historialJugadores.size()][3];

        for (int i = 0; i < historialJugadores.size(); i++) {
            Jugador jugador = historialJugadores.get(i);
            data[i][0] = String.valueOf(i + 1);  // Posición
            data[i][1] = jugador.getNombre();   // Nombre
            data[i][2] = String.valueOf(jugador.getPuntaje());  // Puntaje
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        intPuntajes.jTablePuntajes.setModel(model);
    }
}
