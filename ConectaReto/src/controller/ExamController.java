/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import model.Convocatoria;
import model.Enunciado;

/**
 *
 * @author 2dam
 */
public class ExamController implements ManageExams {

    private Connection con;
    private PreparedStatement stmt;
    private DBConnection conController = new DBConnection();
    final String CREARUNIDAD = "INSERT INTO UnidadDidactica(acronimo, titulo, evaluacion, descripcion) VALUES (?,?,?,?)";
    final String CREARCONVOCATORIA ="INSERT INTO ConvocatoriaExamen (convocatoria, descripcion, fecha, curso) VALUES (?,?,?,?)";

    @Override
    public boolean crearUnidad(String acronimo, String titulo, String evaluacion, String descripcion) {
        boolean creado = false;

        try {
            con = conController.openConnection();
            stmt = con.prepareStatement(CREARUNIDAD);
            stmt.setString(1, acronimo);
            stmt.setString(2, titulo);
            stmt.setString(3, evaluacion);
            stmt.setString(4, descripcion);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                creado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL al crear la unidad didáctica.");
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

        return creado;
    }

    @Override
    public boolean crearConvocatoria(String convocatoria, String descripcion, LocalDate fecha, String curso) {
       boolean creado = false;

        try {
            con = conController.openConnection();
            stmt = con.prepareStatement(CREARCONVOCATORIA);
            stmt.setString(1, convocatoria);
            stmt.setString(2, descripcion);
            stmt.setDate(3, java.sql.Date.valueOf(fecha)); 
            stmt.setString(4, curso);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                creado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL al crear la convocatoria de examen");
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
        return creado;
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
    public Convocatoria consultarConvocatoria() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enunciado visualizarDocEnunciado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Convocatoria asignarEnunciado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
