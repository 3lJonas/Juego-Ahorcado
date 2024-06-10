/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoAhorcado;

import controlador.ControladorMenu;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class JuegoAhorcadoConsola {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // Torneo torneo = new Torneo();
       // torneo.jugarTorneo();
       ControladorMenu cm=new ControladorMenu();
       cm.getIntMenu().setVisible(true);
       cm.getIntMenu().setLocationRelativeTo(null);
    }
}
