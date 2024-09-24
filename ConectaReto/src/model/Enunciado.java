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
public class Enunciado {

    private int id;
    private String descripcion;
    private Dificultad nivel;
    private boolean disponible;
    private String ruta;
    private ArrayList<UnidadDidactica> unidades;
    private ArrayList<Convocatoria> convocatorias;

    public Enunciado(String descripcion, Dificultad nivel, boolean disponible, String ruta, ArrayList<UnidadDidactica> unidades, ArrayList<Convocatoria> convocatorias) {
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.disponible = disponible;
        this.ruta = ruta;
        this.unidades = unidades;
        this.convocatorias = convocatorias;
    }

    public Enunciado() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Dificultad getNivel() {
        return nivel;
    }

    public void setNivel(Dificultad nivel) {
        this.nivel = nivel;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public ArrayList<UnidadDidactica> getUnidades() {
        return unidades;
    }

    public void setUnidades(ArrayList<UnidadDidactica> unidades) {
        this.unidades = unidades;
    }

    public ArrayList<Convocatoria> getConvocatorias() {
        return convocatorias;
    }

    public void setConvocatorias(ArrayList<Convocatoria> convocatorias) {
        this.convocatorias = convocatorias;
    }
}
