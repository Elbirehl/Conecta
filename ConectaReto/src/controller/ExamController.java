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
    private ResultSet rs;
    private final DBConnection conController = new DBConnection();

    //IRATI
    final String CREARUNIDAD = "INSERT INTO UnidadDidactica(acronimo, titulo, evaluacion, descripcion) VALUES (?,?,?,?)";
    //IRATI
    final String CREARCONVOCATORIA = "INSERT INTO ConvocatoriaExamen (convocatoria, descripcion, fecha, curso) VALUES (?,?,?,?)";
    //Inserta un nuevo enunciado en la tabla Enunciado. att:Elbire
    final String CREARENUNCIADO = "INSERT INTO Enunciado (descripcion, nivel_dificultad, disponible, ruta) VALUES (?, ?, ?, ?)";
    //Inserta una relación entre unidad didáctica y enunciado. att:Elbire
    final String CREARUDENUNCIADO = "INSERT INTO UD_Enunciado (UD_id, enunciado_id) VALUES (?, ?)";
    //Actualiza el enunciado de una convocatoria específica. att:Elbire
    final String ACTUALIZACONVOCATORIA = "UPDATE ConvocatoriaExamen SET enunciado_id = ? WHERE convocatoria = ?";
    //Lista todos los IDs y acrónimos de las unidades didácticas. att:Elbire
    final String LISTARUDESPECIFICA = "SELECT id, acronimo FROM UnidadDidactica";
    //Obtiene detalles de una unidad didáctica específica por ID. att:Elbire
    final String OBTENERUDESPECIFICA = "SELECT * FROM UnidadDidactica WHERE id = ?";
    //Lista todas las convocatorias de examen. att:Elbire
    final String LISTARCONVOCATORIASTRING = "SELECT convocatoria FROM ConvocatoriaExamen";
    //Obtiene los detalles de una convocatoria específica. att:Elbire
    final String DETALLESCONVOCATORIA = "SELECT * FROM ConvocatoriaExamen WHERE convocatoria = ?";
    //Para mostrar los enunciados que pertenecen a una unidad didactica att:Meylin
    final String CONSUTARENUNCIADOCONUDESPECIFICA = "SELECT descripcion FROM ENUNCIADO WHERE Id IN (SELECT ENUNCIADO_ID FROM UD_ENUNCIADO WHERE UD_ID = ?)";
    //Para mostrar todas las unidades didácticas att:Meylin
    final String MOSTRARUNIDADESDIDACTICAS = "SELECT * FROM UnidadDidactica";
    //Saber si hay unidades didacticas y cuantas att:Meylin
    final String CONSULTARCANTIDADUNIDADESDIDACTICAS = "SELECT MAX(id) FROM UnidadDidactica";
    //OLAIA
    //Obtiene todos los detalles de una convocatoria por el id de un enunciado concreto.
    final String CONSULTARCONVOCATORIASPORUNENUNCIADOCONCRETO = "SELECT * FROM ConvocatoriaExamen WHERE enunciado_id = ?";
    //Actualiza las convocatorias con un enunciado asignado.
    final String UPDATECONVOCATORIA = "UPDATE ConvocatoriaExamen SET enunciado_id = ? WHERE convocatoria = ?";
    //Sleciona datos de las convocatorias que no tienen ningun enunciado asignado.
    final String CONVOCATORIASINENUNCIADO = "SELECT convocatoria, descripcion, fecha, curso FROM ConvocatoriaExamen WHERE enunciado_id IS NULL";
    //Seleciona el id y la descripcion de los enunciados.
    final String SELECTENUNCIADO = "SELECT id, descripcion FROM Enunciado";
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

    @Override
    public Enunciado crearEnunciado(String desc, Dificultad dificultad, boolean disponible, String ruta, ArrayList<UnidadDidactica> unidades, ArrayList<Convocatoria> convocatorias) {
        // Crear objeto Enunciado que será retornado al final
        Enunciado enunciado = null;

        try {

            // Intentar abrir conexión con la base de datos
            con = conController.openConnection();
            // 1. Insertar nuevo enunciado en la tabla Enunciado
            PreparedStatement ps = con.prepareStatement(CREARENUNCIADO, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, desc);
            ps.setString(2, dificultad.toString().toLowerCase()); // Convierte la dificultad a minúsculas para la base de datos
            ps.setBoolean(3, disponible);
            ps.setString(4, ruta);
            ps.executeUpdate();

            // 2. Obtener el ID del enunciado recién insertado
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int enunciadoId = rs.getInt(1);

                // 3. Asociar Unidades Didácticas con el enunciado
                for (UnidadDidactica unidad : unidades) {
                    PreparedStatement psUnidad = con.prepareStatement(CREARUDENUNCIADO);
                    psUnidad.setInt(1, unidad.getId()); // Obtener el ID de la unidad didáctica
                    psUnidad.setInt(2, enunciadoId);
                    psUnidad.executeUpdate();
                }

                // 4. Asociar Convocatorias con el enunciado
                for (Convocatoria convocatoria : convocatorias) {
                    PreparedStatement psConvocatoria = con.prepareStatement(ACTUALIZACONVOCATORIA);
                    psConvocatoria.setInt(1, enunciadoId);
                    psConvocatoria.setString(2, convocatoria.getConvocatoria()); // Obtener la convocatoria por nombre
                    psConvocatoria.executeUpdate();
                }
                // 5. Crear el objeto Enunciado con la información disponible
                enunciado = new Enunciado(enunciadoId, desc, dificultad, disponible, ruta, unidades, convocatorias);
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
    //METODOS EXTRA DE ELBIRE
    public UnidadDidactica mostrarUnidadDidactica() {

        UnidadDidactica unidadSeleccionada = null;

        // Intentar abrir conexión con la base de datos
        try {
            con = conController.openConnection();
            // 1. Obtener lista de Unidades Didácticas
            PreparedStatement ps = con.prepareStatement(LISTARUDESPECIFICA);
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
            PreparedStatement psDetalle = con.prepareStatement(OBTENERUDESPECIFICA);
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
        try {

            con = conController.openConnection();
            // 1. Consultar la lista de Convocatorias
            PreparedStatement ps = con.prepareStatement(LISTARCONVOCATORIASTRING);
            rs = ps.executeQuery();
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
            PreparedStatement psDetalle = con.prepareStatement(DETALLESCONVOCATORIA);
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
