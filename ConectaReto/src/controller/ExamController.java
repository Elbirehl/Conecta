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
import model.Dificultad;
import model.Enunciado;
import model.UnidadDidactica;
import utilidades.Util;

/**
 *
 * @author 2dam
 */
public class ExamController implements ManageExams {
    
    private final DBConnection dbConnection = new DBConnection();

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
        Boolean disponible=false;
        Boolean terminado=false;
        ArrayList<UnidadDidactica> unidades = new ArrayList<UnidadDidactica>();
        ArrayList<Convocatoria> convocatorias = new ArrayList<Convocatoria>();

        System.out.println("*********Enunciado*********");
        System.out.println("Introduce la descripcion del enunciado");
        String desc = Util.introducirCadena();
        Dificultad dificultad = Dificultad.BAJA.validarDificultad();
        System.out.println("Introduce la ruta del enunciado");
        String ruta = Util.introducirCadena();
        System.out.println("¿Está disponible? Si/No ");
        String disponibleS = Util.introducirCadena();
	while(!disponibleS.equalsIgnoreCase("no")&&!disponibleS.equalsIgnoreCase("si")) {
            System.out.println("¡ERROR! Introduce una opcion valida");
            System.out.println("¿Está disponible? Si/No");
            disponibleS=Util.introducirCadena();
	}
	if(Util.introducirCadena().equalsIgnoreCase("si")) {
            disponible=true;
	}
        // Selección de Unidades Didácticas
        System.out.println("Selecciona las Unidades Didácticas para el enunciado.");
        do {
            UnidadDidactica unidad = mostrarUnidadDidactica();
            if (unidad != null) {
                unidades.add(unidad);
            }
            System.out.println("¿Quieres añadir otra Unidad Didáctica? (Si/No):");
            String otraUnidad = Util.introducirCadena();
            while (!otraUnidad.equalsIgnoreCase("no") && !otraUnidad.equalsIgnoreCase("si")) {
                System.out.println("¡ERROR! Introduce una opción válida.");
                System.out.println("¿Quieres añadir otra Unidad Didáctica? (Si/No):");
                otraUnidad = Util.introducirCadena();
            }
            if (otraUnidad.equalsIgnoreCase("no")) {
                terminado = true;
            }
        } while (!terminado);
        terminado=false;
        // Selección de Convocatorias
        System.out.println("Selecciona las Convocatorias para el enunciado.");
        do {
            Convocatoria convocatoria = consultarConvocatoriaDB();
            if (convocatoria != null) {
                convocatorias.add(convocatoria);
            }
            System.out.println("¿Quieres añadir otra Convocatoria? (Si/No):");
            String otraConvocatoria = Util.introducirCadena();
            while (!otraConvocatoria.equalsIgnoreCase("no") && !otraConvocatoria.equalsIgnoreCase("si")) {
                System.out.println("¡ERROR! Introduce una opción válida.");
                System.out.println("¿Quieres añadir otra Convocatoria? (Si/No):");
                otraConvocatoria = Util.introducirCadena();
            }
            if (otraConvocatoria.equalsIgnoreCase("no")) {
                terminado = true;
            }
        } while (!terminado);
        
        
        // 4. Insertar enunciado en la base de datos
        Enunciado enunciado = null;
        try (Connection conn = dbConnection.getConnection()) {
            String insertEnunciado = "INSERT INTO Enunciado (descripcion, nivel_dificultad, disponible, ruta) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertEnunciado, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, desc);
            ps.setString(2, dificultad.toString().toLowerCase()); // Convierte la dificultad a minúsculas para la base de datos
            ps.setBoolean(3, disponible);
            ps.setString(4, ruta);
            ps.executeUpdate();

            // Obtener el ID del enunciado insertado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int enunciadoId = rs.getInt(1);

                // 5. Asociar Unidades Didácticas con el enunciado
                String insertUDEnunciado = "INSERT INTO UD_Enunciado (UD_id, enunciado_id) VALUES (?, ?)";
                for (UnidadDidactica unidad : unidades) {
                    PreparedStatement psUnidad = conn.prepareStatement(insertUDEnunciado);
                    psUnidad.setInt(1, unidad.getId()); // Ajusta este método para obtener el ID de la unidad
                    psUnidad.setInt(2, enunciadoId);
                    psUnidad.executeUpdate();
                }

                // 6. Asociar Convocatoria con el enunciado
                for (Convocatoria convocatoria : convocatorias) {
                    String updateConvocatoria = "UPDATE ConvocatoriaExamen SET enunciado_id = ? WHERE convocatoria = ?";
                    PreparedStatement psConvocatoria = conn.prepareStatement(updateConvocatoria);
                    psConvocatoria.setInt(1, enunciadoId);
                    psConvocatoria.setString(2, convocatoria.getConvocatoria()); // Ajusta este método para obtener la convocatoria
                    psConvocatoria.executeUpdate();
                }

                enunciado = new Enunciado(desc, dificultad, disponible, ruta, unidades, convocatorias);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enunciado;
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
    
    public UnidadDidactica mostrarUnidadDidactica() {
        UnidadDidactica unidadSeleccionada = null;
        try (Connection conn = dbConnection.getConnection()) {
            String query = "SELECT id, acronimo FROM UnidadDidactica";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("Lista de Unidades Didácticas:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String acronimo = rs.getString("acronimo");
                System.out.println(id + ": " + acronimo);
            }

            System.out.println("Introduce el ID de la Unidad Didáctica que quieres seleccionar:");
            int idSeleccionado = Integer.parseInt(Util.introducirCadena());

            // Obtener detalles de la unidad seleccionada
            String detalleQuery = "SELECT * FROM UnidadDidactica WHERE id = ?";
            PreparedStatement psDetalle = conn.prepareStatement(detalleQuery);
            psDetalle.setInt(1, idSeleccionado);
            ResultSet rsDetalle = psDetalle.executeQuery();

            if (rsDetalle.next()) {
                String acronimo = rsDetalle.getString("acronimo");
                String titulo = rsDetalle.getString("titulo");
                String evaluacion = rsDetalle.getString("evaluacion");
                String descripcion = rsDetalle.getString("descripcion");
                ArrayList<Enunciado> enunciados = new ArrayList<>(); // Puede que necesites llenar esta lista si es necesario
                unidadSeleccionada = new UnidadDidactica(idSeleccionado, acronimo, titulo, evaluacion, descripcion, enunciados);
            } else {
                System.out.println("¡Error! Unidad Didáctica no encontrada.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unidadSeleccionada;
    }
    public Convocatoria consultarConvocatoriaDB() {
        Convocatoria convocatoriaSeleccionada = null;
        try (Connection conn = dbConnection.getConnection()) {
            String query = "SELECT convocatoria FROM ConvocatoriaExamen";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("Lista de Convocatorias:");
            while (rs.next()) {
                String convocatoria = rs.getString("convocatoria");
                System.out.println(convocatoria);
            }

            System.out.println("Introduce el nombre de la Convocatoria que quieres seleccionar:");
            String convocatoriaSeleccionadaStr = Util.introducirCadena();

            // Obtener detalles de la convocatoria seleccionada
            String detalleQuery = "SELECT * FROM ConvocatoriaExamen WHERE convocatoria = ?";
            PreparedStatement psDetalle = conn.prepareStatement(detalleQuery);
            psDetalle.setString(1, convocatoriaSeleccionadaStr);
            ResultSet rsDetalle = psDetalle.executeQuery();

            if (rsDetalle.next()) {
                String descripcion = rsDetalle.getString("descripcion");
                LocalDate fecha = rsDetalle.getDate("fecha").toLocalDate();
                String curso = rsDetalle.getString("curso");
                int enunciadoId = rsDetalle.getInt("enunciado_id");
                Enunciado enunciado = null; // Puedes implementar la obtención de detalles del enunciado si es necesario
                convocatoriaSeleccionada = new Convocatoria(convocatoriaSeleccionadaStr, descripcion, fecha, curso, enunciado);
            } else {
                System.out.println("¡Error! Convocatoria no encontrada.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return convocatoriaSeleccionada;
    }

}
