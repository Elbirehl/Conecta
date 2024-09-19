/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.ExamController;
import java.time.LocalDate;

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

        ExamController examController = new ExamController();

        // Prueba de crear una Unidad Didáctica
        String acronimo = "MATH101";
        String titulo = "Matemáticas Avanzadas";
        String evaluacion = "Final";
        String descripcion = "Unidad sobre cálculo avanzado y álgebra.";

        boolean unidadCreada = examController.crearUnidad(acronimo, titulo, evaluacion, descripcion);
        if (unidadCreada) {
            System.out.println("Unidad didáctica creada con éxito.");
        } else {
            System.out.println("Error al crear la unidad didáctica.");
        }

        // Prueba de crear una Convocatoria
        String convocatoria = "Junio 2024";
        String descripcionConvocatoria = "Convocatoria para exámenes finales de junio.";
        LocalDate fecha = LocalDate.of(2024, 6, 15);
        String curso = "2023/2024";

        int enunciadoId = 1;  // O el ID del enunciado que deseas asociar con la convocatoria
        boolean convocatoriaCreada = examController.crearConvocatoria(convocatoria, descripcionConvocatoria, fecha, curso, enunciadoId);

        if (convocatoriaCreada) {
            System.out.println("Convocatoria creada con éxito.");
        } else {
            System.out.println("Error al crear la convocatoria.");
        }
    }
}
