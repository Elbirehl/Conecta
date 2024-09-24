/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author 2dam
 */
public class Convocatoria {
    private String convocatoria;
    private String descripcion;
    private LocalDate fecha;
    private String curso;
    private Enunciado enunciado;

    public Convocatoria(String convocatoria, String descripcion, LocalDate fecha, String curso, Enunciado enunciado) {
        this.convocatoria = convocatoria;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.curso = curso;
        this.enunciado = enunciado;
    }

    public Convocatoria(String string, String string0, LocalDate toLocalDate, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(String convocatoria) {
        this.convocatoria = convocatoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Enunciado getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(Enunciado enunciado) {
        this.enunciado = enunciado;
    }
    
}
