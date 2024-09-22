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
import java.util.ArrayList;
import model.Convocatoria;
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
    //Para mostrar los enunciados que pertenecen a una unidad didactica att:Meylin
    final String CONSUTARENUNCIADOCONUDESPECIFICA = "SELECT descripcion FROM ENUNCIADO WHERE Id IN (SELECT ENUNCIADO_ID FROM UD_ENUNCIADO WHERE UD_ID = ?)";
    //Para mostrar todas las unidades didácticas att:Meylin
    final String MOSTRARUNIDADESDIDACTICAS = "SELECT * FROM UnidadDidactica";
    //Saber si hay unidades didacticas y cuantas att:Meylin
    final String CONSULTARCANTIDADUNIDADESDIDACTICAS = "SELECT MAX(id) FROM UnidadDidactica";

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
    public ArrayList<String> consultarEnunciado(int id) {
        ArrayList<String> enunciado = new ArrayList();
        ResultSet rs;
        con = conController.openConnection();
        try {
            stmt = con.prepareStatement(CONSUTARENUNCIADOCONUDESPECIFICA);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                enunciado.add(rs.getString("descripcion"));
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conController.closeConnection(stmt, con);
        } catch (SQLException e) {
            System.out.println("Error en el cierre de la Base de Datos");
            e.printStackTrace();
        }
        return enunciado;
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

    public ArrayList<UnidadDidactica> mostrarUnidadesDidacticas() {
        ArrayList<UnidadDidactica> unidadesDidacticas = new ArrayList();
        UnidadDidactica ud = null;
        con = conController.openConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(MOSTRARUNIDADESDIDACTICAS);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ud = new UnidadDidactica();
                ud.setId(rs.getInt("id"));
                ud.setAcronimo(rs.getString("acronimo"));
                ud.setTitulo(rs.getString("titulo"));
                ud.setEvaluacion(rs.getString("evaluacion"));
                ud.setDescripcion(rs.getString("descripcion"));
                unidadesDidacticas.add(ud);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener las unidades didácticas: " + e.getMessage());
        }
        try {
            conController.closeConnection(stmt, con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unidadesDidacticas;
    }

    public int consultarCantidadUnidadesDidacticas() {
        int cantidad = 0;
        con = conController.openConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(CONSULTARCANTIDADUNIDADESDIDACTICAS);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cantidad = rs.getInt(1);
                if (rs.wasNull()) {
                    cantidad = 0;
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener las unidades didácticas: " + e.getMessage());
        }
        try {
            conController.closeConnection(stmt, con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cantidad;
    }
}
