package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Connection conn = null;
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");

                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/track2play", "root", "Ema23456@");
                return conn;
            }
            catch (SQLException e)
            {
                System.out.println("Connessione fallita");
                e.printStackTrace();
                throw e;
            }
            catch (ClassNotFoundException e){
                throw new SQLException("Driver non trovato");
            }
        }
        return conn;
    }

}
