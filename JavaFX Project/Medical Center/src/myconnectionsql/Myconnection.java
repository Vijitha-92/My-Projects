package myconnectionsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Myconnection {
    static public Connection connection;
    static public Connection doconnect() {
        String database = "medicalcenter";
        try {
            connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost/medicalcenter", "root", "");
        } catch (SQLException e) {
            System.out.println("NOT COnnected" + e.toString());
        }
        return connection;
    }
}
