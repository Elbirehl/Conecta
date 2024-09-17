/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.DBConnection;
import controller.ExamController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utilidades.Util;

/**
 *
 * @author 2dam
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int menu;
        ExamController controlador = new ExamController();
        do {
            System.out.println("\t\t\tBIENVENID@\n\tMENÚ:"
                    + "\n\t1.Crear unidad didáctica."
                    + "\n\t2.Crear convocatoria."
                    + "\n\t3.Crear enunciado."
                    + "\n\t4.Consultar enunciado por unidad uidáctica."
                    + "\n\t5.Consultar convocatoria por enunciado."
                    + "\n\t6.Visualizar la descripcion de un enunciado."
                    + "\n\t7.Asignar un enunciado a una convocatoria."
                    + "\n\t8.Salir.");
            menu = Util.leerInt(1, 8);
            switch (menu) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    controlador.crearEnunciado();
                    break;
                case 4:
                    ConsultarEnunciado(controlador);
                    break;
                case 5:
                    controlador.consultarConvocatoria();
                    break;
                case 6:
                    controlador.visualizarDocEnunciado();
                    break;
                case 7:
                    controlador.asignarEnunciado();
                    break;
                case 8:
                    System.out.println("Gracias por usar nuestro programa.\nSaliendo...");
                    break;

            }
        } while (menu != 8);
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

    private static void ConsultarEnunciado(ExamController controlador) {
        int id;
        System.out.println("Introduce el ID de la unidad didáctica: ");
        id = Util.leerInt();
        controlador.consultarEnunciado(id);

    }

}
