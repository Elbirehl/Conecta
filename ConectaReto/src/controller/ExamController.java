/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Convocatoria;
import model.Dificultad;
import model.Enunciado;
import model.UnidadDidactica;
import utilidades.Util;

/**
 *
 * @author 2dam
 */
public class ExamController implements ManageExams {
    
    private final DBConnection dbConnection = new DBConnection();

    // Método para mostrar Unidades Didácticas y seleccionar una
    public UnidadDidactica mostrarUnidadDidactica() {

    }

    @Override
    public UnidadDidactica crearUnidad(String acronimo, String titulo, String evaluacion, String descripcion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Convocatoria crearConvocatoria(String convocatoria, String descripcion, LocalDate fecha, String curso) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enunciado crearEnunciado() {
        Boolean disponible=false;
        Boolean terminado=false;
        ArrayList<UnidadDidactica> unidades = new ArrayList<UnidadDidactica>();
        ArrayList<Convocatoria> convocatoria = new ArrayList<Convocatoria>();

        System.out.println("*********Enunciado*********");
        System.out.println("Introduce la descripcion del enunciado");
        String desc = Util.introducirCadena();
        Dificultad dificultad = Dificultad.BAJA.validarDificultad();
        System.out.println("Introduce la ruta del enunciado");
        String ruta = Util.introducirCadena();
        System.out.println("¿Está disponible? Si/No ");
        String disponibleS = Util.introducirCadena();
	while(!disponibleS.equalsIgnoreCase("no")&&!disponibleS.equalsIgnoreCase("si")) {
            System.out.println("¡ERROR! Introduce una opcion valida");
            System.out.println("¿Está disponible? Si/No");
            disponibleS=Util.introducirCadena();
	}
	if(Util.introducirCadena().equalsIgnoreCase("si")) {
            disponible=true;
	}
        do{
            System.out.println("Introduce una Unidad Didactica");
            System.out.println("Lista de Unidades Didacticas disponibles:");
        }while(!terminado);
        do{
            System.out.println("Introduce una Convocatoria");
            System.out.println("Lista de Convocatorias disponibles:");
        }while(!terminado);
        Enunciado enunciado = new Enunciado(desc,dificultad,disponible,ruta,unidades,convocatoria);
        return enunciado;
    }

    @Override
    public Enunciado consultarEnunciado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Convocatoria consultarConvocatoria() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enunciado visualizarDocEnunciado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Convocatoria asignarEnunciado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
