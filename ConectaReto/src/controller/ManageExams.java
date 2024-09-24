/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Convocatoria;
import model.Enunciado;
import model.UnidadDidactica;

/**
 *
 * @author 2dam
 */
public interface ManageExams {
    public UnidadDidactica crearUnidad(String acronimo, String titulo, String evaluacion, String descripcion);
    public Convocatoria crearConvocatoria (String convocatoria, String descripcion, LocalDate fecha, String curso);
    public Enunciado crearEnunciado();
    public Enunciado consultarEnunciado();
    public ArrayList<Convocatoria> consultarConvocatoria( int enunciadoId);
    public Enunciado visualizarDocEnunciado();
    public void asignarEnunciado( );
    
}
