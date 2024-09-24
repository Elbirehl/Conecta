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
  
    final String CREARUNIDAD = "INSERT INTO UnidadDidactica(acronimo, titulo, evaluacion, descripcion) VALUES (?,?,?,?)";
    final String CREARCONVOCATORIA = "INSERT INTO ConvocatoriaExamen (convocatoria, descripcion, fecha, curso, enunciado_id) VALUES (?,?,?,?,NULL)";
    final String CONSULTARCONVOCATORIA = "SELECT * FROM ConvocatoriaExamen WHERE enunciado_id = ?";
 //Para mostrar los enunciados que pertenecen a una unidad didactica att:Meylin
    final String CONSUTARENUNCIADOCONUDESPECIFICA = "SELECT descripcion FROM ENUNCIADO WHERE Id IN (SELECT ENUNCIADO_ID FROM UD_ENUNCIADO WHERE UD_ID = ?)";
    //Para mostrar todas las unidades didácticas att:Meylin
    final String MOSTRARUNIDADESDIDACTICAS = "SELECT * FROM UnidadDidactica";
    //Saber si hay unidades didacticas y cuantas att:Meylin
    final String CONSULTARCANTIDADUNIDADESDIDACTICAS = "SELECT MAX(id) FROM UnidadDidactica";
    //Mostrar todas las descripciones de los enunciados att:Meylin
    final String MOSTRARTODOSLOSENUNCIADOS = "SELECT id,descripcion,ruta FROM Enunciado;";
    //Saber si hay enunciados y cuantos att:Meylin
    final String CONSULTARCANTIDADENUNCIADOS = "SELECT MAX(id) FROM Enunciado";
  
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
        }finally {
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
    public ArrayList<Enunciado> visualizarDocEnunciado() {
        ArrayList<Enunciado> enunciados = new ArrayList();
        Enunciado enunciado;
        con = conController.openConnection();
        try {
            stmt = con.prepareStatement(MOSTRARTODOSLOSENUNCIADOS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                enunciado = new Enunciado();
                enunciado.setId(rs.getInt("id"));
                enunciado.setDescripcion(rs.getString("descripcion"));
                enunciado.setRuta(rs.getString("ruta"));
                enunciados.add(enunciado);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener los Enunciados: " + e.getMessage());
        }
        try {
            conController.closeConnection(stmt, con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enunciados;
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
            stmt = con.prepareStatement(MOSTRARUNIDADESDIDACTICAS);

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
            stmt = con.prepareStatement(CONSULTARCANTIDADUNIDADESDIDACTICAS);
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

    public int consultarCantidadEnunciados() {
        int cantidad = 0;
        con = conController.openConnection();
        try {
            stmt = con.prepareStatement(CONSULTARCANTIDADENUNCIADOS);
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
            System.out.println("Error al obtener los enunciados " + e.getMessage());
        }
        try {
            conController.closeConnection(stmt, con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cantidad;
    }

}

