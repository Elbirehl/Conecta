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
import model.Convocatoria;
import model.Dificultad;
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
        // TODO code application logic here
        crearEnunciado();
    }
    
    public static void crearEnunciado(){
        
        ExamController controlador = new ExamController();
        Boolean disponible=false;
        Boolean terminado=false;
        ArrayList<UnidadDidactica> unidades = new ArrayList<UnidadDidactica>();
        ArrayList<Convocatoria> convocatorias = new ArrayList<Convocatoria>();
        
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
	while(!disponibleS.equalsIgnoreCase("no")&&!disponibleS.equalsIgnoreCase("si")) {
            System.out.println("¡ERROR! Introduce una opcion valida");
            System.out.println("¿Está disponible? Si/No");
            disponibleS=Util.introducirCadena();
	}
	if(disponibleS.equalsIgnoreCase("si")) {
            disponible=true;
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
        
        terminado=false; // Reinicia el estado de la variable
        
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
    
}
