/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import model.Convocatoria;
import model.Dificultad;
import controller.ExamController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
                    crearEnunciado(controlador);
                    break;
                case 4:
                    ConsultarEnunciado(controlador);
                    break;
                case 5:
                    controlador.consultarConvocatoria();
                    break;
                case 6:
                    cvisualizarDocEnunciado(controlador);
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

    private static void crearEnunciado(ExamController controlador) {

        Boolean disponible = false;
        Boolean terminado = false;
        ArrayList<UnidadDidactica> unidades = new ArrayList<>();
        ArrayList<Convocatoria> convocatorias = new ArrayList<>();

        // Solicita al usuario la descripción del enunciado
        System.out.println("*********Enunciado*********");
        System.out.println("Introduce la descripcion del enunciado");
        String desc = Util.introducirCadena();

        // Valida la dificultad seleccionada
        Dificultad dificultad = Dificultad.BAJA.validarDificultad();

        // Solicita la ruta del enunciado
        System.out.println("Introduce la ruta del enunciado");
        String ruta = Util.introducirCadena();

        // Pregunta si el enunciado está disponible
        System.out.println("¿Está disponible? Si/No ");
        String disponibleS = Util.introducirCadena();

        // Validación de la entrada de disponible
        while (!disponibleS.equalsIgnoreCase("no") && !disponibleS.equalsIgnoreCase("si")) {
            System.out.println("¡ERROR! Introduce una opcion valida");
            System.out.println("¿Está disponible? Si/No");
            disponibleS = Util.introducirCadena();
        }
        if (disponibleS.equalsIgnoreCase("si")) {
            disponible = true;
        }
        // Selección de Unidades Didácticas
        System.out.println("Selecciona las Unidades Didácticas para el enunciado.");
        do {
            UnidadDidactica unidad = controlador.mostrarUnidadDidactica();
            if (unidad != null) {
                unidades.add(unidad); // Añade la unidad seleccionada a la lista
            }
            System.out.println("¿Quieres añadir otra Unidad Didáctica? (Si/No):");
            String otraUnidad = Util.introducirCadena();
            // Validación de la entrada
            while (!otraUnidad.equalsIgnoreCase("no") && !otraUnidad.equalsIgnoreCase("si")) {
                System.out.println("¡ERROR! Introduce una opción válida.");
                System.out.println("¿Quieres añadir otra Unidad Didáctica? (Si/No):");
                otraUnidad = Util.introducirCadena();
            }
            if (otraUnidad.equalsIgnoreCase("no")) {
                terminado = true; // Finaliza la selección si el usuario no desea añadir más
            }
        } while (!terminado);

        terminado = false; // Reinicia el estado de la variable

        // Selección de Convocatorias
        System.out.println("Selecciona las Convocatorias para el enunciado.");
        do {
            Convocatoria convocatoria = controlador.consultarConvocatoriaDB();
            if (convocatoria != null) {
                convocatorias.add(convocatoria); // Añade la convocatoria seleccionada a la lista
            }
            System.out.println("¿Quieres añadir otra Convocatoria? (Si/No):");
            String otraConvocatoria = Util.introducirCadena();

            // Validación de la entrada
            while (!otraConvocatoria.equalsIgnoreCase("no") && !otraConvocatoria.equalsIgnoreCase("si")) {
                System.out.println("¡ERROR! Introduce una opción válida.");
                System.out.println("¿Quieres añadir otra Convocatoria? (Si/No):");
                otraConvocatoria = Util.introducirCadena();
            }
            if (otraConvocatoria.equalsIgnoreCase("no")) {
                terminado = true; // Finaliza la selección si el usuario no desea añadir más
            }
        } while (!terminado);

        // Crea el enunciado en la base de datos
        controlador.crearEnunciado(desc, dificultad, true, ruta, unidades, convocatorias);
    }

    private static void ConsultarEnunciado(ExamController controlador) {
        int id;
        int cant = 1;
        int cantUnidadesDidacticas = controlador.consultarCantidadUnidadesDidacticas();
        ArrayList<String> enunciados;
        ArrayList<UnidadDidactica> unidadesDidacticas;
        if (cantUnidadesDidacticas == 0) {
            System.out.println("Actualmente no hay unidades didacticas");
        } else {
            unidadesDidacticas = controlador.mostrarUnidadesDidacticas();
            System.out.println("\t\tUNIDADES DIDÁCTICAS:");
            //for (UnidadDidactica ud : unidadesDidacticas
            unidadesDidacticas.forEach((ud) -> {
                System.out.println("ID: " + ud.getId()
                        + "\nACRÓNIMO: " + ud.getAcronimo()
                        + "\nTÍTULO: " + ud.getTitulo()
                        + "\nEVALUACION: " + ud.getEvaluacion()
                        + "\nDESCRIPCIÓN: " + ud.getDescripcion()
                );
            });
            System.out.println("Introduce el ID de la unidad didáctica: ");
            id = Util.leerInt(1, cantUnidadesDidacticas);
            enunciados = controlador.consultarEnunciado(id);
            if (enunciados.isEmpty()) {
                System.out.println("Esta unidad didactica no cuenta con enunciados actualmente.\n");
            } else {
                System.out.println("Los enunciados que usan temas de la unidad didáctica " + id + " son:");
                for (String e : enunciados) {
                    System.out.println("\t" + cant + ". " + e);
                    cant++;
                }
            }
        }

    }

    private static void cvisualizarDocEnunciado(ExamController controlador) {
        int id;
        boolean encontrado = false;
        String filePath;
        ArrayList<Enunciado> enunciados;
        int cantidadEnunciados = controlador.consultarCantidadUnidadesDidacticas();
        if (cantidadEnunciados == 0) {
            System.out.println("Actualmente no hay unidades didacticas");
        } else {
            enunciados = controlador.visualizarDocEnunciado();
            System.out.println("\t\tENUNCIADOS:");
            //for (Enunciado enunciado : enunciados)
            enunciados.forEach((enunciado) -> {
                System.out.println("ID: " + enunciado.getId()
                        + "\nDESCRIPCION: " + enunciado.getDescripcion());
            });
            System.out.println("\nIntroduce el ID del enunciado que desea visualizar: ");
            id = Util.leerInt(1, cantidadEnunciados);
            for (int i = 0; i < enunciados.size() && !encontrado; i++) {
                if (enunciados.get(i).getId() == id) {
                    encontrado = true;
                    String directorioTrabajo = System.getProperty("user.dir");
                    filePath = enunciados.get(i).getRuta();
                    File file = new File(directorioTrabajo + "\\" + filePath);
                    if (!file.exists()) {
                        System.out.println("El archivo no existe en la ruta especificada.");
                    }
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            // Comprobar si la operación de abrir está soportada
                            if (desktop.isSupported(Desktop.Action.OPEN)) {
                                desktop.open(file); // Abre el archivo
                            } else {
                                System.out.println("La acción de abrir no está soportada en este entorno.");
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("La clase Desktop no es soportada en este entorno.");
                    }
                }
            }
        }
    }

    private static void asignarEnunciado(ExamController controlador) {
    controlador.asignarEnunciado();
    }
}
