/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author 2dam
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /*
        // Instancia de DBConnection
        DBConnection dbConnection = new DBConnection();
        
        // Inicializamos los objetos que vamos a utilizar
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            // Intentamos abrir la conexión
            con = dbConnection.openConnection();

            // Comprobamos si la conexión fue exitosa
            if (con != null) {
                System.out.println("Conexión establecida correctamente.");
            } else {
                System.out.println("Error al establecer la conexión.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                // Cerramos la conexión al terminar
                dbConnection.closeConnection(stmt, con);
            } catch (SQLException e) {
                System.out.println("Error al intentar cerrar la conexión: " + e.getMessage());
            }
        }*/
    }
    
}
