package nhapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test_db {

    private static String url = "jdbc:postgresql://localhost:5678/studs";
    private static String user = "postgres";
    private static String password = "postgres";

    Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to PostgreSQL!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    public static void main(String[] args) {
        Test_db db = new Test_db();
        db.getConnection();
    }
}
