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
    public Enunciado crearEnunciado(String desc, Dificultad dificultad, boolean disponible, String ruta, ArrayList<UnidadDidactica> unidades, ArrayList<Convocatoria> convocatorias) {

        // Crear objeto Enunciado que será retornado al final
        Enunciado enunciado = null;
        
        // Intentar abrir conexión con la base de datos
        try (Connection conn = dbConnection.getConnection()) {
            
            // 1. Insertar nuevo enunciado en la tabla Enunciado
            String insertEnunciado = "INSERT INTO Enunciado (descripcion, nivel_dificultad, disponible, ruta) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertEnunciado, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, desc);
            ps.setString(2, dificultad.toString().toLowerCase()); // Convierte la dificultad a minúsculas para la base de datos
            ps.setBoolean(3, disponible);
            ps.setString(4, ruta);
            ps.executeUpdate();

            // 2. Obtener el ID del enunciado recién insertado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int enunciadoId = rs.getInt(1);

                // 3. Asociar Unidades Didácticas con el enunciado
                String insertUDEnunciado = "INSERT INTO UD_Enunciado (UD_id, enunciado_id) VALUES (?, ?)";
                for (UnidadDidactica unidad : unidades) {
                    PreparedStatement psUnidad = conn.prepareStatement(insertUDEnunciado);
                    psUnidad.setInt(1, unidad.getId()); // Obtener el ID de la unidad didáctica
                    psUnidad.setInt(2, enunciadoId);
                    psUnidad.executeUpdate();
                }

                // 4. Asociar Convocatorias con el enunciado
                for (Convocatoria convocatoria : convocatorias) {
                    String updateConvocatoria = "UPDATE ConvocatoriaExamen SET enunciado_id = ? WHERE convocatoria = ?";
                    PreparedStatement psConvocatoria = conn.prepareStatement(updateConvocatoria);
                    psConvocatoria.setInt(1, enunciadoId);
                    psConvocatoria.setString(2, convocatoria.getConvocatoria()); // Obtener la convocatoria por nombre
                    psConvocatoria.executeUpdate();
                }
                // 5. Crear el objeto Enunciado con la información disponible
                enunciado = new Enunciado(desc, dificultad, disponible, ruta, unidades, convocatorias);
            }
        } catch (SQLException e) {
            e.printStackTrace();// Imprimir errores de SQL en caso de excepción
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
        
        // Intentar abrir conexión con la base de datos
        try (Connection conn = dbConnection.getConnection()) {
            
            // 1. Obtener lista de Unidades Didácticas
            String query = "SELECT id, acronimo FROM UnidadDidactica";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            // 2. Mostrar la lista de Unidades Didácticas disponibles
            System.out.println("Lista de Unidades Didácticas:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String acronimo = rs.getString("acronimo");
                System.out.println(id + ": " + acronimo); // Mostrar ID y acrónimo
            }
            
            // 3. Solicitar al usuario que introduzca el ID de la Unidad Didáctica deseada
            System.out.println("Introduce el ID de la Unidad Didáctica que quieres seleccionar:");
            int idSeleccionado = Integer.parseInt(Util.introducirCadena()); // Convertir la entrada del usuario a entero

            // 4. Obtener detalles de la Unidad Didáctica seleccionada
            String detalleQuery = "SELECT * FROM UnidadDidactica WHERE id = ?";
            PreparedStatement psDetalle = conn.prepareStatement(detalleQuery);
            psDetalle.setInt(1, idSeleccionado);
            ResultSet rsDetalle = psDetalle.executeQuery();
            
            // 5. Si la Unidad Didáctica existe, crear el objeto UnidadDidactica
            if (rsDetalle.next()) {
                String acronimo = rsDetalle.getString("acronimo");
                String titulo = rsDetalle.getString("titulo");
                String evaluacion = rsDetalle.getString("evaluacion");
                String descripcion = rsDetalle.getString("descripcion");
                ArrayList<Enunciado> enunciados = new ArrayList<>();
                unidadSeleccionada = new UnidadDidactica(idSeleccionado, acronimo, titulo, evaluacion, descripcion, enunciados);
            } else {
                System.out.println("¡Error! Unidad Didáctica no encontrada.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Manejar errores de SQL
        }
        return unidadSeleccionada;
    }

    public Convocatoria consultarConvocatoriaDB() {
        
        Convocatoria convocatoriaSeleccionada = null;
        
        // Intentar abrir conexión con la base de datos
        try (Connection conn = dbConnection.getConnection()) {
            
            // 1. Consultar la lista de Convocatorias
            String query = "SELECT convocatoria FROM ConvocatoriaExamen";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            ArrayList<String> convocatorias = new ArrayList<>();
            System.out.println("Lista de Convocatorias:");

            // 2. Mostrar la lista de Convocatorias disponibles
            int index = 1;
            while (rs.next()) {
                String convocatoria = rs.getString("convocatoria");
                convocatorias.add(convocatoria);
                System.out.println(index + ". " + convocatoria); // Mostrar número y convocatoria
                index++;
            }

            // 3. Solicitar al usuario que seleccione una convocatoria por número
            System.out.println("Introduce el número de la Convocatoria que quieres seleccionar:");
            int seleccion = Util.leerInt(); // Leer la selección del usuario

            // 4. Validar la selección del usuario
            if (seleccion < 1 || seleccion > convocatorias.size()) {
                System.out.println("¡Error! Selección inválida.");
                return null;
            }

            // 5. Obtener la convocatoria seleccionada por el usuario
            String convocatoriaSeleccionadaStr = convocatorias.get(seleccion - 1);

            // 6. Consultar los detalles de la convocatoria seleccionada
            String detalleQuery = "SELECT * FROM ConvocatoriaExamen WHERE convocatoria = ?";
            PreparedStatement psDetalle = conn.prepareStatement(detalleQuery);
            psDetalle.setString(1, convocatoriaSeleccionadaStr);
            ResultSet rsDetalle = psDetalle.executeQuery();
            
            // 7. Si la convocatoria existe, crear el objeto Convocatoria
            if (rsDetalle.next()) {
                String descripcion = rsDetalle.getString("descripcion");
                LocalDate fecha = rsDetalle.getDate("fecha").toLocalDate();
                String curso = rsDetalle.getString("curso");
                int enunciadoId = rsDetalle.getInt("enunciado_id");
                Enunciado enunciado = null;
                convocatoriaSeleccionada = new Convocatoria(convocatoriaSeleccionadaStr, descripcion, fecha, curso, enunciado);
            } else {
                System.out.println("¡Error! Convocatoria no encontrada.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Manejar errores de SQL
        }
        return convocatoriaSeleccionada;
    }

}
