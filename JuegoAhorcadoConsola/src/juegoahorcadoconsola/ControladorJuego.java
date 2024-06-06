/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juegoahorcadoconsola;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import vista.Interfaz_Juego;
import vista.Interfaz_Multijugador;

/**
 *
 * @author ASUS GAMER
 */
public class ControladorJuego implements ActionListener, MouseListener {

    private Interfaz_Multijugador interfazAnterior;
    private Interfaz_Juego interfaz;
    private Torneo torneo;

    public ControladorJuego(Interfaz_Juego interfaz, Interfaz_Multijugador interfazAnterior, ArrayList<Jugador> jugadores) {
        this.interfaz = interfaz;
        this.interfazAnterior = interfazAnterior;
        this.interfaz.jLabelRegresar.addMouseListener(this);
        this.interfaz.palabraJugador1.addActionListener(this);
        this.interfaz.letraJugador1.addActionListener(this);
        this.interfaz.adivinarJugador1.addActionListener(this);
        this.interfaz.palabraJugador2.addActionListener(this);
        this.interfaz.letraJugador2.addActionListener(this);
        this.interfaz.adivinarJugador2.addActionListener(this);
        this.interfaz.palabraJugador3.addActionListener(this);
        this.interfaz.letraJugador3.addActionListener(this);
        this.interfaz.adivinarJugador3.addActionListener(this);
        this.interfaz.palabraJugador4.addActionListener(this);
        this.interfaz.letraJugador4.addActionListener(this);
        this.interfaz.adivinarJugador4.addActionListener(this);
      //  this.torneo = new Torneo(interfaz, jugadores, this.interfazAnterior.jSpinnerRondas.getComponentCount());
        //Aqui iniciar el torneo
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.interfaz.adivinarJugador1 == e.getSource()) {
            System.out.println("adivino el jugador1");
        }
        if (this.interfaz.adivinarJugador2 == e.getSource()) {
            System.out.println("adivino el jugador1");
        }
        if (this.interfaz.adivinarJugador3 == e.getSource()) {
            System.out.println("adivino el jugador1");
        }
        if (this.interfaz.adivinarJugador4 == e.getSource()) {
            System.out.println("adivino el jugador1");
        }
    }

    public Interfaz_Juego getInterfaz() {
        return this.interfaz;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String[] salir = {"Si", "No"};
        int regresar = JOptionPane.showOptionDialog(null, "Al regresar se cancela la partida y pierde el progreso\n ¿Quiere regresar?", "Regresar", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, salir, salir[0]);

        if (regresar == 0) {
            this.interfazAnterior.setVisible(true);
            this.interfaz.dispose();

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
