/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author 2dam
 */
public class UnidadDidactica {

    private int id;
    private String acronimo;
    private String titulo;
    private String evaluacion;
    private String descripcion;
    private ArrayList<Enunciado> enunciados;

    public UnidadDidactica(int id, String acronimo, String titulo, String evaluacion, String descripcion, ArrayList<Enunciado> enunciados) {
        this.id = id;
        this.acronimo = acronimo;
        this.titulo = titulo;
        this.evaluacion = evaluacion;
        this.descripcion = descripcion;
        this.enunciados = enunciados;
    }

    public UnidadDidactica() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the acronimo
     */
    public String getAcronimo() {
        return acronimo;
    }

    /**
     * @param acronimo the acronimo to set
     */
    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the evaluacion
     */
    public String getEvaluacion() {
        return evaluacion;
    }

    /**
     * @param evaluacion the evaluacion to set
     */
    public void setEvaluacion(String evaluacion) {
        this.evaluacion = evaluacion;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<Enunciado> getEnunciados() {
        return enunciados;
    }

    public void setEnunciados(ArrayList<Enunciado> enunciados) {
        this.enunciados = enunciados;
    }
}
