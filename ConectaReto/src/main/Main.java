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
import java.util.ArrayList;
import model.UnidadDidactica;
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
                    //ConsultarEnunciado(controlador);
                    break;
                case 5:
                    //controlador.consultarConvocatoria();
                    break;
                case 6:
                    controlador.visualizarDocEnunciado();
                    break;
                case 7:
                    //controlador.asignarEnunciado();
                    break;
                case 8:
                    System.out.println("Gracias por usar nuestro programa.\nSaliendo...");
                    break;

            }
        } while (menu != 8);
    }

    /*private static void ConsultarEnunciado(ExamController controlador) {
        int id;
        int cant = 1;
        ArrayList<String> enunciados;
        ArrayList<UnidadDidactica> unidadesDidacticas;
        unidadesDidacticas = controlador.mostrarUnidadesDidacticas();
        if (unidadesDidacticas.isEmpty()) {
            System.out.println("Error. No hay unidades domesticas");
        } else {
            System.out.println("\t\tUNIDADES DIDÁCTICAS:");
            for (UnidadDidactica ud : unidadesDidacticas) {
                System.out.println("ID: " + ud.getId()
                        + "\nACRÓNIMO: " + ud.getAcronimo()
                        + "\nTÍTULO: " + ud.getTitulo()
                        + "\nEVALUACION: " + ud.getEvaluacion()
                        + "\nDESCRIPCIÓN: " + ud.getDescripcion()
                );
            }
            System.out.println("Introduce el ID de la unidad didáctica: ");
            id = Util.leerInt();
            enunciados = controlador.consultarEnunciado(id);
            if (enunciados.isEmpty()) {
                System.err.println("Esta unidad didactica no cuenta con enunciados actualmente.");
            } else {
                System.out.println("Los enunciados que usan temas de la unidad didáctica " + id + " son:");
                for (String e : enunciados) {
                    System.out.println("\t" + cant + ". " + e);
                    cant++;
                }
            }
        }

    }*/

}