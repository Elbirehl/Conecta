/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.time.LocalDate;
import model.Convocatoria;
import model.Enunciado;

/**
 *
 * @author 2dam
 */
public interface ManageExams {
    public boolean crearUnidad(String acronimo, String titulo, String evaluacion, String descripcion);
    public boolean crearConvocatoria (String convocatoria, String descripcion, LocalDate fecha, String curso);
    public Enunciado crearEnunciado();
    public Enunciado consultarEnunciado();
    public Convocatoria consultarConvocatoria();
    public Enunciado visualizarDocEnunciado();
    public Convocatoria asignarEnunciado();
    
}
