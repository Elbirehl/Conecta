/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
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

    private Connection con;
    private PreparedStatement stmt;
    private DBConnection conController = new DBConnection();
    //AQUI PONER LAS SENTENCIAS SQL
    final String CONSULTARCONVOCATORIASPORUNENUNCIADOCONCRETO = "SELECT * FROM ConvocatoriaExamen WHERE enunciado_id = ?";
    final String UPDATECONVOCATORIA = "UPDATE ConvocatoriaExamen SET enunciado_id = ? WHERE convocatoria = ?";
    final String CONVOCATORIASINENUNCIADO = "SELECT convocatoria, descripcion, fecha, curso FROM ConvocatoriaExamen WHERE enunciado_id IS NULL";
    final String SELECTENUNCIADO = "SELECT id, descripcion FROM Enunciado";

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enunciado consultarEnunciado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public ArrayList<Convocatoria> consultarConvocatoria(int enunciadoId) {
        ArrayList<Convocatoria> convocatorias = new ArrayList<>();

        try {
            con = conController.openConnection();
            stmt = con.prepareStatement(CONSULTARCONVOCATORIASPORUNENUNCIADOCONCRETO);
            stmt.setInt(1, enunciadoId);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    String convocatoriaNombre = resultSet.getString("convocatoria");
                    String descripcion = resultSet.getString("descripcion");
                    LocalDate fecha = resultSet.getDate("fecha").toLocalDate();
                    String curso = resultSet.getString("curso");

                    // Crear el objeto Convocatoria
                    Convocatoria convocatoria = new Convocatoria(convocatoriaNombre, descripcion, fecha, curso, null);
                    convocatorias.add(convocatoria);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL al consultar las convocatorias.");
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                conController.closeConnection(stmt, con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return convocatorias;
    }

    @Override
    public Enunciado visualizarDocEnunciado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void asignarEnunciado(int seleccionConvocatoria, int enunciadoId) {
        ArrayList<Convocatoria> convocatoriasSinEnunciado = new ArrayList<>();

        try {
            // Abrir conexión
            con = conController.openConnection();

            // 1. Consultar convocatorias sin enunciado asignado
            String consultarConvocatoriasSinEnunciado = CONVOCATORIASINENUNCIADO;
            stmt = con.prepareStatement(consultarConvocatoriasSinEnunciado);
            ResultSet resultSet = stmt.executeQuery();

            // Añadir convocatorias sin enunciado a la lista
            while (resultSet.next()) {
                String convocatoriaNombre = resultSet.getString("convocatoria");
                String descripcion = resultSet.getString("descripcion");
                LocalDate fecha = resultSet.getDate("fecha").toLocalDate();
                String curso = resultSet.getString("curso");

                Convocatoria convocatoria = new Convocatoria(convocatoriaNombre, descripcion, fecha, curso, null);
                convocatoriasSinEnunciado.add(convocatoria);
            }

            if (convocatoriasSinEnunciado.isEmpty()) {
                return; // No hay convocatorias disponibles, salir.
            }

            // Obtener la convocatoria seleccionada por el usuario
            Convocatoria convocatoriaSeleccionada = convocatoriasSinEnunciado.get(seleccionConvocatoria - 1);
            String convocatoriaNombre = convocatoriaSeleccionada.getConvocatoria();

            // 2. Consultar enunciados disponibles
            stmt = con.prepareStatement(SELECTENUNCIADO);
            resultSet = stmt.executeQuery();
            ArrayList<Integer> enunciados = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                enunciados.add(id);
            }

            if (enunciados.isEmpty()) {
                return; // No hay enunciados disponibles, salir.
            }

            // Validar si el enunciado seleccionado es válido
            if (!enunciados.contains(enunciadoId)) {
                return; // El ID del enunciado no es válido, salir.
            }

            // 3. Asignar el enunciado a la convocatoria
            stmt = con.prepareStatement(UPDATECONVOCATORIA);
            stmt.setInt(1, enunciadoId); // Asignar enunciado_id
            stmt.setString(2, convocatoriaNombre);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Actualizar el objeto convocatoria seleccionada
                Enunciado enunciadoAsignado = new Enunciado(enunciadoId, "Descripción del enunciado", Dificultad.MEDIA, true, "ruta");
                convocatoriaSeleccionada.setEnunciado(enunciadoAsignado);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de error
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                conController.closeConnection(stmt, con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Convocatoria> obtenerConvocatoriasSinEnunciado() {
        ArrayList<Convocatoria> convocatoriasSinEnunciado = new ArrayList<>();

        try {
            // Abrir conexión
            con = conController.openConnection();

            // Consulta para obtener las convocatorias sin enunciado asignado
            String consultarConvocatoriasSinEnunciado = CONVOCATORIASINENUNCIADO;
            stmt = con.prepareStatement(consultarConvocatoriasSinEnunciado);
            ResultSet resultSet = stmt.executeQuery();

            // Agregar las convocatorias a la lista
            while (resultSet.next()) {
                String convocatoriaNombre = resultSet.getString("convocatoria");
                String descripcion = resultSet.getString("descripcion");
                LocalDate fecha = resultSet.getDate("fecha").toLocalDate();
                String curso = resultSet.getString("curso");

                Convocatoria convocatoria = new Convocatoria(convocatoriaNombre, descripcion, fecha, curso, null);
                convocatoriasSinEnunciado.add(convocatoria);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de error
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                conController.closeConnection(stmt, con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return convocatoriasSinEnunciado; // Devolver la lista de convocatorias
    }

    public ArrayList<Enunciado> obtenerEnunciadosDisponibles() {
    ArrayList<Enunciado> enunciadosDisponibles = new ArrayList<>();

    try {
        // Abrir conexión
        con = conController.openConnection();

        // Consulta para obtener los enunciados disponibles
        String consultarEnunciadosDisponibles = SELECTENUNCIADO;
        stmt = con.prepareStatement(consultarEnunciadosDisponibles);
        ResultSet resultSet = stmt.executeQuery();

        // Agregar los enunciados a la lista
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String descripcion = resultSet.getString("descripcion");

            // Crear objeto Enunciado con los datos de la BD
            Enunciado enunciado = new Enunciado(id, descripcion, Dificultad.MEDIA, true, "ruta");  // Verificar que este constructor exista
            enunciadosDisponibles.add(enunciado);
        }

    } catch (SQLException e) {
        e.printStackTrace(); // Manejo de error
    } finally {
        try {
            if (stmt != null) {
                stmt.close();
            }
            conController.closeConnection(stmt, con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return enunciadosDisponibles; // Devolver la lista de enunciados
}  
}
