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
import java.time.LocalDate;
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
                    crearUnidad(controlador);
                    break;
                case 2:
                    crearConvocatoria(controlador);
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

    private static void crearUnidad(ExamController controlador) {

        System.out.println("Introduce el acrónimo de la unidad didáctica:");
        String acronimo = Util.introducirCadena();

        // Validar que el acrónimo no esté vacío y siga el formato correcto (ej. UD01, UD02, ...)
        while (!acronimo.matches("UD\\d{2}")) {
            System.out.println("El acrónimo debe seguir el formato 'UD' seguido de dos dígitos (ej. UD01). Inténtalo de nuevo:");
            acronimo = Util.introducirCadena();
        }

        // Comprobar que la unidad didáctica no se repita
        while (controlador.existeUnidadDidactica(acronimo)) {
            System.out.println("Ya existe una unidad didáctica con el acrónimo '" + acronimo + "'. Intenta con otro:");
            acronimo = Util.introducirCadena();
        }

        System.out.println("Introduce el título de la unidad didáctica:");
        String titulo = Util.introducirCadena();
        while (titulo.isEmpty()) {
            System.out.println("El título no puede estar vacío.");
            titulo = Util.introducirCadena();
        }

        System.out.println("Introduce la evaluación de la unidad didáctica (Primera, segunda o tercera evaluación):");
        String evaluacion = Util.introducirCadena();
        // Validar que la evaluación no esté vacía y sea una de las opciones válidas
        while (evaluacion.isEmpty() || (!evaluacion.equalsIgnoreCase("Primera")
                && !evaluacion.equalsIgnoreCase("Segunda")
                && !evaluacion.equalsIgnoreCase("Tercera"))) {
            if (evaluacion.isEmpty()) {
                System.out.println("La evaluación no puede estar vacía.");
            } else {
                System.out.println("La evaluación debe ser 'Primera', 'Segunda' o 'Tercera'. Inténtalo de nuevo:");
            }
            evaluacion = Util.introducirCadena();
        }

        System.out.println("Introduce una descripción para la unidad didáctica:");
        String descripcion = Util.introducirCadena();
        while (descripcion.isEmpty()) {
            System.out.println("La descripción no puede estar vacía.");
            descripcion = Util.introducirCadena();
        }

        try {
            boolean creada = controlador.crearUnidad(acronimo, titulo, evaluacion, descripcion);

            if (creada) {
                System.out.println("Unidad didáctica creada exitosamente.");
            } else {
                System.out.println("Error al crear la unidad didáctica.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error al crear la unidad didáctica: " + e.getMessage());
        }
    }

    private static void crearConvocatoria(ExamController controlador) {

        System.out.println("Introduce el el nombre de la convocatoria:");
        String convocatoria = Util.introducirCadena();
        while (convocatoria.isEmpty()) {
            System.out.println("El nombre de la convocatoria no puede estar vacío.");
            convocatoria = Util.introducirCadena();
        }

        System.out.println("Introduce la descripción de la convocatoria:");
        String descripcion = Util.introducirCadena();
        while (descripcion.isEmpty()) {
            System.out.println("La descripción no puede estar vacía.");
            descripcion = Util.introducirCadena();
        }

        System.out.println("Introduce la fecha de la convocatoria:");
        LocalDate fecha = Util.leerFechaAMD();

        System.out.println("Introduce el curso:");
        String curso = Util.introducirCadena();
        while (curso.isEmpty()) {
            System.out.println("El curso no puede estar vacío.");
            curso = Util.introducirCadena();
        }
        try {
            boolean creada = controlador.crearConvocatoria(convocatoria, descripcion, fecha, curso);

            if (creada) {
                System.out.println("Convocatoria creada exitosamente.");
            } else {
                System.out.println("Error al crear la convocatoria.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error al crear la convocatoria: " + e.getMessage());
        }
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
