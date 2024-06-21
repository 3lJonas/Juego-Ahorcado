/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.ControladorMenu;

/**
 *
 * @author ASUS VIVOBOOK
 */
public class Main{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
       ControladorMenu cm=new ControladorMenu();
       cm.getIntMenu().setVisible(true);
       cm.getIntMenu().setLocationRelativeTo(null);
    }
}
