/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class DBConnection {

    /**
     * Abre una conexion con la base de datos.
     *
     * @return
     */
    public Connection openConnection() {
        // TODO Auto-generated method stub
        Connection con = null;
        try {

            String url = "jdbc:mysql://localhost:3306/examendb?serverTimezone=Europe/Madrid&useSSL=false";
            con = DriverManager.getConnection(url, "root", "abcd*1234");

        } catch (SQLException e) {
            //Logger.getLogger("DBConnection").severe(e.getLocalizedMessage());
            System.out.println("Error al intentar abrir la BD: " + e.getMessage());
            System.exit(1);
        }
        return con;
    }

    /**
     * Cierra la conexion con la base de datos.
     *
     * @param stmt
     * @param con
     */
    public void closeConnection(PreparedStatement stmt, Connection con) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
