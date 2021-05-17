package nhapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSQL {
    private final String url = "jdbc:postgresql://localhost:5678/studs";
    private final String user = "postgres";
    private final String password = "postgres";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
        Connection conn = null;
        Statement stmt;

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");

            stmt = conn.createStatement();
            String sql = "INSERT INTO my_user (username, password) " +
                    "VALUES ('abc', '123');";
            stmt.executeUpdate(sql);

            stmt.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PostgreSQL app = new PostgreSQL();
        app.connect();
    }
}
