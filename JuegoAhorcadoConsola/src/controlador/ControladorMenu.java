/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import modelo.Jugador;
import vista.Interfaz_Juego;
import vista.Interfaz_Menu;
import vista.Interfaz_Multijugador;
import vista.Interfaz_Puntajes;

/**
 *
 * @author ASUS GAMER
 */
public class ControladorMenu implements ActionListener, MouseListener {
    
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private int jugador = 1;
    private boolean verificar;
    private Interfaz_Menu intMenu;
    private Interfaz_Puntajes intPuntajes;
    private Interfaz_Multijugador pesMultiJugador;
    private Interfaz_Juego intJuego;
    private ControladorJuego contrJuego;
    
    public ControladorMenu() {
        this.intMenu = new Interfaz_Menu();
        this.intPuntajes = new Interfaz_Puntajes();
        this.pesMultiJugador = new Interfaz_Multijugador();
        this.intJuego = new Interfaz_Juego();
        
        this.intMenu.jButtonJugar.addActionListener(this);
        this.intMenu.jButtonPuntajes.addActionListener(this);
        this.intPuntajes.jLabelRegresar.addMouseListener(this);
        this.pesMultiJugador.jLabelRegresar.addMouseListener(this);
        this.pesMultiJugador.jButtonAñadir.addActionListener(this);
        this.pesMultiJugador.jButtonQuitar.addActionListener(this);
        this.pesMultiJugador.jButtonIniciar.addActionListener(this);
        this.pesMultiJugador.jButtonListo1.addActionListener(this);
        this.pesMultiJugador.jButtonListo2.addActionListener(this);
        this.pesMultiJugador.jButtonListo3.addActionListener(this);
        this.pesMultiJugador.jButtonListo4.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.jugador !=0) {
            
            this.pesMultiJugador.jButtonQuitar.setEnabled(true);
            if (this.jugador == 3) {
                this.pesMultiJugador.jButtonAñadir.setEnabled(false);
            }
        }
        //Para el menu de ver puntaje
        if (this.intMenu.jButtonPuntajes == e.getSource()) {
            this.intMenu.setVisible(false);
            this.intPuntajes.setLocationRelativeTo(null);
            this.intPuntajes.setVisible(true);
        }
        // Para el menu de jugar 
        if (this.intMenu.jButtonJugar == e.getSource()) {
            SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 7, 1);
            this.intMenu.setVisible(false);
            this.pesMultiJugador.setVisible(true);
            this.pesMultiJugador.jSpinnerRondas.setModel(model);
            this.pesMultiJugador.setLocationRelativeTo(null);
        }
        // Para iniciar la partida
        if (this.pesMultiJugador.jButtonIniciar == e.getSource()) {
            if (this.verificarListo()) {
                // Eliminar la instancia anterior de ControladorJuego si existe
                if (this.contrJuego != null) {
                    this.contrJuego.dispose();
                }
                // Crear una nueva instancia de ControladorJuego
                this.contrJuego = new ControladorJuego(intJuego, pesMultiJugador, this.jugadores, this.intMenu);
                
                this.contrJuego.getInterfaz().setLocationRelativeTo(null);
                
            } else {
                JOptionPane.showMessageDialog(null, "Todos los jugadores deben tener nombres válidos y estar listos!!!");
                this.jugadores.clear();
            }
        }
        if (this.pesMultiJugador.jButtonListo1 == e.getSource()) {
            this.cambiarListo(this.pesMultiJugador.jButtonListo1);
        }
        if (this.pesMultiJugador.jButtonListo2 == e.getSource()) {
            this.cambiarListo(this.pesMultiJugador.jButtonListo2);
        }
        if (this.pesMultiJugador.jButtonListo3 == e.getSource()) {
            this.cambiarListo(this.pesMultiJugador.jButtonListo3);
        }
        if (this.pesMultiJugador.jButtonListo4 == e.getSource()) {
            this.cambiarListo(this.pesMultiJugador.jButtonListo4);
        }
        // Añadir jugadores máximo 4
        if (this.pesMultiJugador.jButtonAñadir == e.getSource()) {
            if (this.jugador < 4) {
                this.jugador++;
                if (this.jugador == 2) {
                    this.pesMultiJugador.jButtonListo2.setEnabled(true);
                    this.pesMultiJugador.jTextFieldNombreJugador2.setEnabled(true);
                }
                if (this.jugador == 3) {
                    this.pesMultiJugador.jButtonListo3.setEnabled(true);
                    this.pesMultiJugador.jTextFieldNombreJugador3.setEnabled(true);
                }
                if (this.jugador == 4) {
                    this.pesMultiJugador.jButtonListo4.setEnabled(true);
                    this.pesMultiJugador.jTextFieldNombreJugador4.setEnabled(true);
                }
            }
        }
        // Quitar jugadores
        if (this.pesMultiJugador.jButtonQuitar == e.getSource()) {
            if (this.jugador > 1) {
                if (this.jugador == 2) {
                    this.pesMultiJugador.jButtonListo2.setEnabled(false);
                    this.pesMultiJugador.jButtonListo2.setText("No listo");
                    this.pesMultiJugador.jTextFieldNombreJugador2.setText("");
                    this.pesMultiJugador.jTextFieldNombreJugador2.setEnabled(false);
                }
                if (this.jugador == 3) {
                    this.pesMultiJugador.jButtonListo3.setEnabled(false);
                    this.pesMultiJugador.jButtonListo3.setText("No listo");
                    this.pesMultiJugador.jTextFieldNombreJugador3.setText("");
                    this.pesMultiJugador.jTextFieldNombreJugador3.setEnabled(false);
                }
                if (this.jugador == 4) {
                    this.pesMultiJugador.jButtonListo4.setEnabled(false);
                    this.pesMultiJugador.jButtonListo4.setText("No listo");
                    this.pesMultiJugador.jTextFieldNombreJugador4.setText("");
                    this.pesMultiJugador.jTextFieldNombreJugador4.setEnabled(false);
                }
                this.jugador--;
            }
        }
        if (this.jugador != 4) {
            this.pesMultiJugador.jButtonAñadir.setEnabled(true);
            if (this.jugador == 1) {
                this.pesMultiJugador.jButtonQuitar.setEnabled(false);
            }
        }
    }


public void cambiarListo(JButton boton) {
        if (boton.getText().equals("No listo")) {
            boton.setText("Listo");
        } else {
            boton.setText("No listo");
        }
    }

    public boolean verificarListo() {
        this.jugadores.clear();
        this.verificar = true;
        if (this.jugador >= 1) {
            this.jugadores.add(new Jugador(this.pesMultiJugador.jTextFieldNombreJugador1.getText()));
            this.verificar &= this.pesMultiJugador.jButtonListo1.getText().equals("Listo") && (!this.pesMultiJugador.jTextFieldNombreJugador1.getText().equals(""));
        }
        if (this.jugador >= 2) {
            this.jugadores.add(new Jugador(this.pesMultiJugador.jTextFieldNombreJugador2.getText()));
            this.verificar &= this.pesMultiJugador.jButtonListo2.getText().equals("Listo") && (!this.pesMultiJugador.jTextFieldNombreJugador2.getText().equals(""));
        }
        if (this.jugador >= 3) {
            this.jugadores.add(new Jugador(this.pesMultiJugador.jTextFieldNombreJugador3.getText()));
            this.verificar &= this.pesMultiJugador.jButtonListo3.getText().equals("Listo") && (!this.pesMultiJugador.jTextFieldNombreJugador3.getText().equals(""));
        }
        if (this.jugador == 4) {
            this.jugadores.add(new Jugador(this.pesMultiJugador.jTextFieldNombreJugador4.getText()));
            this.verificar &= this.pesMultiJugador.jButtonListo4.getText().equals("Listo") && (!this.pesMultiJugador.jTextFieldNombreJugador4.getText().equals(""));
        }
        return this.verificar;
    }

    @Override
public void mouseClicked(MouseEvent e) {
        if (this.pesMultiJugador.jLabelRegresar == e.getSource()) {
            this.pesMultiJugador.setVisible(false);
            this.intMenu.setVisible(true);
            this.resetMultiJugador();
        }
         if (this.intPuntajes.jLabelRegresar == e.getSource()) {
            this.intPuntajes.dispose();
            this.intMenu.setVisible(true);
            
        }
    }

    private void resetMultiJugador() {
        this.jugador = 1;
        this.pesMultiJugador.jButtonListo2.setEnabled(false);
        this.pesMultiJugador.jTextFieldNombreJugador2.setEnabled(false);
        this.pesMultiJugador.jButtonListo3.setEnabled(false);
        this.pesMultiJugador.jTextFieldNombreJugador3.setEnabled(false);
        this.pesMultiJugador.jButtonListo4.setEnabled(false);
        this.pesMultiJugador.jTextFieldNombreJugador4.setEnabled(false);
        this.pesMultiJugador.jButtonListo1.setText("No listo");
        this.pesMultiJugador.jTextFieldNombreJugador1.setText("");
        this.pesMultiJugador.jButtonListo2.setText("No listo");
        this.pesMultiJugador.jTextFieldNombreJugador2.setText("");
        this.pesMultiJugador.jButtonListo3.setText("No listo");
        this.pesMultiJugador.jTextFieldNombreJugador3.setText("");
        this.pesMultiJugador.jButtonListo4.setText("No listo");
        this.pesMultiJugador.jTextFieldNombreJugador4.setText("");
        this.pesMultiJugador.jButtonAñadir.setEnabled(true);
        this.pesMultiJugador.jButtonQuitar.setEnabled(false);
    }

    public Interfaz_Menu getIntMenu() {
        return intMenu;
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
