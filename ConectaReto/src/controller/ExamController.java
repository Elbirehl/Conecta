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
import utilidades.Util;
import model.Enunciado;
import model.UnidadDidactica;

/**
 *
 * @author 2dam
 */
public class ExamController implements ManageExams {

    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;
    private final DBConnection conController = new DBConnection();

    //IRATI
    final String CREARUNIDAD = "INSERT INTO UnidadDidactica(acronimo, titulo, evaluacion, descripcion) VALUES (?,?,?,?)";
    //IRATI
    final String CREARCONVOCATORIA = "INSERT INTO ConvocatoriaExamen (convocatoria, descripcion, fecha, curso, enunciado_id) VALUES (?,?,?,?,?)";
    //OLAIA
    final String ASIGNARENUNCIADOACONVOCATORIA = "UPDATE ConvocatoriaExamen SET enunciado_id = ? WHERE convocatoria=?";
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
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                conController.closeConnection(stmt, con);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                conController.closeConnection(stmt, con);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return creado;
    }

    @Override
    public Enunciado crearEnunciado(String desc, Dificultad dificultad, boolean disponible, String ruta, ArrayList<UnidadDidactica> unidades, ArrayList<Convocatoria> convocatorias) {
        // Crear objeto Enunciado que será retornado al final
        Enunciado enunciado = null;

        // Intentar abrir conexión con la base de datos
        try (Connection conn = conController.getConnection()) {

            // 1. Insertar nuevo enunciado en la tabla Enunciado
            String insertEnunciado = "INSERT INTO Enunciado (descripcion, nivel_dificultad, disponible, ruta) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertEnunciado, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, desc);
            ps.setString(2, dificultad.toString().toLowerCase()); // Convierte la dificultad a minúsculas para la base de datos
            ps.setBoolean(3, disponible);
            ps.setString(4, ruta);
            ps.executeUpdate();

            // Obtener el ID del enunciado insertado
            // 2. Obtener el ID del enunciado recién insertado
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int enunciadoId = rs.getInt(1);

                // 5. Asociar Unidades Didácticas con el enunciado
                // 3. Asociar Unidades Didácticas con el enunciado
                String insertUDEnunciado = "INSERT INTO UD_Enunciado (UD_id, enunciado_id) VALUES (?, ?)";
                for (UnidadDidactica unidad : unidades) {
                    PreparedStatement psUnidad = conn.prepareStatement(insertUDEnunciado);
                    psUnidad.setInt(1, unidad.getId()); // Ajusta este método para obtener el ID de la unidad
                    psUnidad.setInt(1, unidad.getId()); // Obtener el ID de la unidad didáctica
                    psUnidad.setInt(2, enunciadoId);
                    psUnidad.executeUpdate();
                }

                // 6. Asociar Convocatoria con el enunciado
                // 4. Asociar Convocatorias con el enunciado
                for (Convocatoria convocatoria : convocatorias) {
                    String updateConvocatoria = "UPDATE ConvocatoriaExamen SET enunciado_id = ? WHERE convocatoria = ?";
                    PreparedStatement psConvocatoria = conn.prepareStatement(updateConvocatoria);
                    psConvocatoria.setInt(1, enunciadoId);
                    psConvocatoria.setString(2, convocatoria.getConvocatoria()); // Ajusta este método para obtener la convocatoria
                    psConvocatoria.setString(2, convocatoria.getConvocatoria()); // Obtener la convocatoria por nombre
                    psConvocatoria.executeUpdate();
                }
                // 5. Crear el objeto Enunciado con la información disponible
                enunciado = new Enunciado(desc, dificultad, disponible, ruta, unidades, convocatorias);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return enunciado;
    }

    @Override
    public ArrayList<String> consultarEnunciado(int id) {
        ArrayList<String> enunciado = new ArrayList();

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
            System.out.println(e.getMessage());
        }
        conController.closeConnection(stmt, con);
        return enunciado;
    }

    //OLAIA
    /**
     *
     * @return
     */
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
            rs = stmt.executeQuery();
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
        conController.closeConnection(stmt, con);
        return enunciados;
    }

    //OLAIA
    /**
     *
     * @return
     */
    @Override
    public Convocatoria asignarEnunciado() {
        return null;
    }

    //METODOS EXTRA DE ELBIRE
    public UnidadDidactica mostrarUnidadDidactica() {

        UnidadDidactica unidadSeleccionada = null;

        // Intentar abrir conexión con la base de datos
        try (Connection conn = conController.getConnection()) {

            // 1. Obtener lista de Unidades Didácticas
            String query = "SELECT id, acronimo FROM UnidadDidactica";
            PreparedStatement ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

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
            System.out.println(e.getMessage());
        }
        return unidadSeleccionada;
    }

    public Convocatoria consultarConvocatoriaDB() {

        Convocatoria convocatoriaSeleccionada = null;

        // Intentar abrir conexión con la base de datos
        try (Connection conn = conController.getConnection()) {

            // 1. Consultar la lista de Convocatorias
            String query = "SELECT convocatoria FROM ConvocatoriaExamen";
            PreparedStatement ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            ArrayList<String> convocatorias = new ArrayList<>();
            System.out.println("Lista de Convocatorias:");
            // 2. Mostrar la lista de Convocatorias disponibles
            int index = 1;
            while (rs.next()) {
                String convocatoria = rs.getString("convocatoria");
                System.out.println(convocatoria);
                convocatorias.add(convocatoria);
                System.out.println(index + ". " + convocatoria); // Mostrar número y convocatoria
                index++;
            }

            System.out.println("Introduce el nombre de la Convocatoria que quieres seleccionar:");
            String convocatoriaSeleccionadaStr = Util.introducirCadena();
            // 3. Solicitar al usuario que seleccione una convocatoria por número
            System.out.println("Introduce el número de la Convocatoria que quieres seleccionar:");
            int seleccion = Util.leerInt(); // Leer la selección del usuario

            // Obtener detalles de la convocatoria seleccionada
            // 4. Validar la selección del usuario
            if (seleccion < 1 || seleccion > convocatorias.size()) {
                System.out.println("¡Error! Selección inválida.");
                return null;
            }
            // 5. Obtener la convocatoria seleccionada por el usuario
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
                Enunciado enunciado = null; // Puedes implementar la obtención de detalles del enunciado si es necesario
                convocatoriaSeleccionada = new Convocatoria(convocatoriaSeleccionadaStr, descripcion, fecha, curso, enunciado);
            } else {
                System.out.println("¡Error! Convocatoria no encontrada.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Manejar errores de SQL
        }
        return convocatoriaSeleccionada;
    }

    //METODOS EXTRA MEYLIN
    public ArrayList<UnidadDidactica> mostrarUnidadesDidacticas() {
        ArrayList<UnidadDidactica> unidadesDidacticas = new ArrayList();
        UnidadDidactica ud;
        con = conController.openConnection();
        try {
            stmt = con.prepareStatement(MOSTRARUNIDADESDIDACTICAS);

            rs = stmt.executeQuery();
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
        conController.closeConnection(stmt, con);
        return unidadesDidacticas;
    }

    public int consultarCantidadUnidadesDidacticas() {
        int cantidad = 0;
        con = conController.openConnection();
        try {
            stmt = con.prepareStatement(CONSULTARCANTIDADUNIDADESDIDACTICAS);
            rs = stmt.executeQuery();
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
        conController.closeConnection(stmt, con);
        return cantidad;
    }

    public int consultarCantidadEnunciados() {
        int cantidad = 0;
        con = conController.openConnection();
        try {
            stmt = con.prepareStatement(CONSULTARCANTIDADENUNCIADOS);
            rs = stmt.executeQuery();
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
        conController.closeConnection(stmt, con);
        return cantidad;
    }

    public boolean existeUnidadDidactica(String acronimo) {
        ArrayList<UnidadDidactica> unidadesExistentes = mostrarUnidadesDidacticas();
        for (UnidadDidactica unidad : unidadesExistentes) {
            if (unidad.getAcronimo().equals(acronimo)) {
                return true; // Retorna verdadero si el acrónimo ya existe
            }
        }
        return false; // Retorna falso si no existe
    }
}
