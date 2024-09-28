/**
 * Clase que representa una Unidad Didáctica, con atributos como el acrónimo, título, evaluación,
 * descripción, y una lista de enunciados relacionados.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Irati, Meylin, Elbire y Olaia
 */
public class UnidadDidactica {

    private int id;
    private String acronimo;
    private String titulo;
    private String evaluacion;
    private String descripcion;
    private ArrayList<Enunciado> enunciados;

    /**
     * Constructor completo para crear una Unidad Didáctica.
     *
     * @param id El identificador único de la unidad.
     * @param acronimo El acrónimo de la unidad.
     * @param titulo El título de la unidad.
     * @param evaluacion El tipo de evaluación de la unidad.
     * @param descripcion La descripción de la unidad.
     * @param enunciados La lista de enunciados asociados a la unidad.
     */
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
