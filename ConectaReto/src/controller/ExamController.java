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
    final String UPDATEConvocatoria = "UPDATE ConvocatoriaExamen SET id_Enunciado = ? WHERE convocatoria = ?";

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
public void asignarEnunciado() {
    ArrayList<Convocatoria> convocatoriasSinEnunciado = new ArrayList<>();

    try {
        // Abrir conexión
        con = conController.openConnection();

        // 1. Consultar convocatorias sin enunciado asignado
        String consultarConvocatoriasSinEnunciado = "SELECT convocatoria, descripcion, fecha, curso FROM ConvocatoriaExamen WHERE enunciado_id IS NULL";
        stmt = con.prepareStatement(consultarConvocatoriasSinEnunciado);
        ResultSet resultSet = stmt.executeQuery();

        // Mostrar convocatorias sin enunciado asignado
        while (resultSet.next()) {
            String convocatoriaNombre = resultSet.getString("convocatoria");
            String descripcion = resultSet.getString("descripcion");
            LocalDate fecha = resultSet.getDate("fecha").toLocalDate();
            String curso = resultSet.getString("curso");

            // Crear objeto Convocatoria y añadir a la lista
            Convocatoria convocatoria = new Convocatoria(convocatoriaNombre, descripcion, fecha, curso, null);
            convocatoriasSinEnunciado.add(convocatoria);
            System.out.println("Convocatoria: " + convocatoriaNombre);
        }

        if (convocatoriasSinEnunciado.isEmpty()) {
            System.out.println("No hay convocatorias sin enunciado.");
            return; // Salir si no hay convocatorias disponibles
        }

        // 2. Preguntar al usuario por la convocatoria a modificar
        System.out.println("Selecciona el número de la convocatoria a la que quieres asignar un enunciado:");
        for (int i = 0; i < convocatoriasSinEnunciado.size(); i++) {
            System.out.println((i + 1) + ". " + convocatoriasSinEnunciado.get(i).getConvocatoria());
        }
        
        int seleccion = Util.leerInt(1, convocatoriasSinEnunciado.size());
        Convocatoria convocatoriaSeleccionada = convocatoriasSinEnunciado.get(seleccion - 1);
        String convocatoriaNombre = convocatoriaSeleccionada.getConvocatoria();

        // 3. Consultar enunciados disponibles
        stmt = con.prepareStatement("SELECT id, descripcion FROM Enunciado");
        resultSet = stmt.executeQuery();
        ArrayList<Integer> enunciados = new ArrayList<>();

        System.out.println("Enunciados disponibles:");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String descripcion = resultSet.getString("descripcion");
            enunciados.add(id);
            System.out.println("ID: " + id + " - Descripción: " + descripcion);
        }

        if (enunciados.isEmpty()) {
            System.out.println("No hay enunciados disponibles para asignar.");
            return; // Salir si no hay enunciados disponibles
        }

        // 4. Preguntar al usuario por el ID del enunciado a asignar
        System.out.println("Introduce el ID del enunciado que deseas asignar: ");
        int enunciadoId = Util.leerInt(); 
        if (!enunciados.contains(enunciadoId)) {
            System.out.println("El ID de enunciado introducido no es válido.");
            return; // Salir si el ID no es válido
        }

        // 5. Asignar el enunciado a la convocatoria
        stmt = con.prepareStatement("UPDATE ConvocatoriaExamen SET enunciado_id = ? WHERE convocatoria = ?");
        stmt.setInt(1, enunciadoId); // Cambiado a enunciado_id
        stmt.setString(2, convocatoriaNombre);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Enunciado asignado correctamente a la convocatoria " + convocatoriaNombre);
            // Actualizar el objeto convocatoria seleccionada
            Enunciado enunciadoAsignado = new Enunciado(enunciadoId, "Descripción del enunciado", Dificultad.MEDIA, true, "ruta");
            convocatoriaSeleccionada.setEnunciado(enunciadoAsignado);
        } else {
            System.out.println("No se pudo asignar el enunciado a la convocatoria.");
        }

    } catch (SQLException e) {
        System.out.println("Error al asignar enunciado a la convocatoria.");
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


}
