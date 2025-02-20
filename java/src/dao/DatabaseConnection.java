// DatabaseConnection.java
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url="jdbc:mysql://localhost:3306/gestion_salle_sport?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String username="root";
    private static final String password="";
    private DatabaseConnection() {}


    public static synchronized Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, username, password);
    }
}
