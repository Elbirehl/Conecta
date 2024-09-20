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
    final String ASIGNARENUNCIADOACONVOCATORIA = "UPDATE ConvocatoriaExamen SET enunciado_id = ? WHERE convocatoria = ?";

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
    public Convocatoria asignarEnunciado(int convocatoriaId, int enunciadoId) {
        Convocatoria convocatoriaAsignada = null;

        try {
            // Abrimos conexión
            con = conController.openConnection();

            // Preparamos la sentencia
            stmt = con.prepareStatement(ASIGNARENUNCIADOACONVOCATORIA);
            stmt.setInt(1, enunciadoId); // Asignamos el ID del enunciado
            stmt.setInt(2, convocatoriaId); // Especificamos la convocatoria a actualizar

            // Ejecutamos la actualización
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Enunciado asignado correctamente a la convocatoria.");

                // Crear un objeto Enunciado con el ID asignado (puedes agregar más detalles si es necesario)
                Enunciado enunciadoAsignado = new Enunciado();
                enunciadoAsignado.setId(enunciadoId);

                // Crear un objeto Convocatoria representando la convocatoria actualizada
                convocatoriaAsignada = new Convocatoria("Convocatoria " + convocatoriaId, "Descripción actualizada", LocalDate.now(), "Curso", enunciadoAsignado);
            } else {
                System.out.println("No se pudo asignar el enunciado. No se encontró la convocatoria.");
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL al asignar enunciado a la convocatoria.");
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

        return convocatoriaAsignada;
    }
}
