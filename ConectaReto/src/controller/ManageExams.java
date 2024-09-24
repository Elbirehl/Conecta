/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import model.Convocatoria;
import model.Dificultad;
import model.Enunciado;

/**
 *
 * @author 2dam
 */
public interface ManageExams {

    public boolean crearUnidad(String acronimo, String titulo, String evaluacion, String descripcion);

    public boolean crearConvocatoria(String convocatoria, String descripcion, LocalDate fecha, String curso, int enunciadoId);

    public Enunciado crearEnunciado(String desc, Dificultad dificultad, boolean disponible, String ruta, ArrayList<UnidadDidactica> unidades, ArrayList<Convocatoria> convocatorias);

    public ArrayList<String> consultarEnunciado(int id);

    public Convocatoria consultarConvocatoria();

    public ArrayList<Enunciado> visualizarDocEnunciado();

    public Convocatoria asignarEnunciado();

}
