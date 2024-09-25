package main;

import controller.DBConnection;
import controller.ExamController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Convocatoria;
import model.Enunciado;
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
                    consultarConvocatoria(controlador);
                    break;
                case 6:
                    controlador.visualizarDocEnunciado();
                    break;
                case 7:
                    asignarEnunciado(controlador);
                    break;
                case 8:
                    System.out.println("Gracias por usar nuestro programa.\nSaliendo...");
                    break;

            }
        } while (menu != 8);
    }
    
    private static void consultarConvocatoria(ExamController controlador) {
        System.out.println("Introduce el ID del enunciado para consultar sus convocatorias:");
        int enunciadoId = Util.leerInt();

        // Call the consultarConvocatoria method
        ArrayList<Convocatoria> convocatorias = controlador.consultarConvocatoria(enunciadoId);
        
        // Check if any convocatorias were found
        if (convocatorias.isEmpty()) {
            System.out.println("No se encontraron convocatorias para el enunciado con ID: " + enunciadoId);
        } else {
            System.out.println("Convocatorias encontradas:");
            for (Convocatoria convocatoria : convocatorias) {
                System.out.println("Nombre: " + convocatoria.getConvocatoria() + 
                                   ", Descripción: " + convocatoria.getDescripcion() + 
                                   ", Fecha: " + convocatoria.getFecha() + 
                                   ", Curso: " + convocatoria.getCurso());
            }
        }
    }
    
  private static void asignarEnunciado(ExamController controlador) {
    ArrayList<Convocatoria> convocatoriasSinEnunciado = controlador.obtenerConvocatoriasSinEnunciado();
    
    if (convocatoriasSinEnunciado.isEmpty()) {
        System.out.println("No hay convocatorias sin enunciado.");
        return;
    }

    // 1. Mostrar las convocatorias al usuario
    System.out.println("Selecciona el número de la convocatoria a la que quieres asignar un enunciado:");
    for (int i = 0; i < convocatoriasSinEnunciado.size(); i++) {
        System.out.println((i + 1) + ". " + convocatoriasSinEnunciado.get(i).getConvocatoria());
    }
    int seleccionConvocatoria = Util.leerInt(1, convocatoriasSinEnunciado.size());

    // 2. Mostrar los enunciados disponibles al usuario
    ArrayList<Enunciado> enunciadosDisponibles = controlador.obtenerEnunciadosDisponibles();
    
    if (enunciadosDisponibles.isEmpty()) {
        System.out.println("No hay enunciados disponibles para asignar.");
        return;
    }
    
    System.out.println("Enunciados disponibles:");
    for (Enunciado enunciado : enunciadosDisponibles) {
        System.out.println("ID: " + enunciado.getId() + " - Descripción: " + enunciado.getDescripcion());
    }
    
    System.out.println("Introduce el ID del enunciado que deseas asignar:");
    int enunciadoId = Util.leerInt();

    // 3. Asignar el enunciado seleccionado a la convocatoria
    controlador.asignarEnunciado(seleccionConvocatoria, enunciadoId);
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