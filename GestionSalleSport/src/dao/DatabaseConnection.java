/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ike
 */
public class DatabaseConnection {
    private static final String url="jdbc:mysql://localhost:3306/gestion_salle_sport?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String username="root";
    private static final String password="ikekeith";
    private DatabaseConnection() {}

    
    public static synchronized Connection getConnection() throws DBException, SQLException{
        return DriverManager.getConnection(url, username, password);
    }
}
