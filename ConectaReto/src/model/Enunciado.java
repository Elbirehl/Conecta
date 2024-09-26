/**
 * La clase Enunciado representa un enunciado de un ejercicio o examen.
 * Contiene la descripción del enunciado, el nivel de dificultad,
 * la disponibilidad, la ruta donde se encuentra almacenado, y las unidades didácticas
 * y convocatorias asociadas.
 *
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

    /**
     * Constructor con todos los parámetros.
     *
     * @param descripcion Descripción del enunciado.
     * @param nivel Nivel de dificultad del enunciado.
     * @param disponible Indica si el enunciado está disponible.
     * @param ruta Ruta del archivo del enunciado.
     * @param unidades Lista de unidades didácticas relacionadas con el
     * enunciado.
     * @param convocatorias Lista de convocatorias en las que el enunciado ha
     * sido utilizado.
     */
    public Enunciado(int enunciadoId, String descripcion, Dificultad nivel, boolean disponible, String ruta, ArrayList<UnidadDidactica> unidades, ArrayList<Convocatoria> convocatorias) {
        this.id = enunciadoId;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.disponible = disponible;
        this.ruta = ruta;
        this.unidades = unidades;
        this.convocatorias = convocatorias;
    }

    /**
     * Constructor vacío que inicializa el objeto Enunciado sin datos.
     */
    public Enunciado() {
      }


    public Enunciado(int id, String descripcion, Dificultad nivel, boolean disponible, String ruta) {
        this.id = id;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.disponible = disponible;
        this.ruta = ruta;
        this.unidades = new ArrayList<>(); // Inicializar las listas vacías
        this.convocatorias = new ArrayList<>();
    }
    
    public Enunciado(int enunciadoId, String descripción_del_enunciado, Dificultad dificultad, boolean b, String ruta) {
        this.id = enunciadoId;
        this.descripcion = descripción_del_enunciado;
        this.nivel = dificultad;
        this.disponible = b;
        this.ruta = ruta;
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return nivel
     */
    public Dificultad getNivel() {
        return nivel;
    }

    /**
     * @param nivel
     */
    public void setNivel(Dificultad nivel) {
        this.nivel = nivel;
    }

    /**
     * @return disponible
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     *
     * @param disponible
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * @return ruta
     */
    public String getRuta() {
        return ruta;
    }

    /**
     *
     * @param ruta
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    /**
     * @return unidades
     */
    public ArrayList<UnidadDidactica> getUnidades() {
        return unidades;
    }

    /**
     *
     * @param unidades
     */
    public void setUnidades(ArrayList<UnidadDidactica> unidades) {
        this.unidades = unidades;
    }

    /**
     * @return convocatorias
     */
    public ArrayList<Convocatoria> getConvocatorias() {
        return convocatorias;
    }

    /**
     *
     * @param convocatorias
     */
    public void setConvocatorias(ArrayList<Convocatoria> convocatorias) {
        this.convocatorias = convocatorias;
    }
}
