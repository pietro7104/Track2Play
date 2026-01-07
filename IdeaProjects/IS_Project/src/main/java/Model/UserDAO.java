package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void addUser(String username, String password) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("insert into Utente (Username, Password) values(?,?)");
        ps.setString(1, username);
        ps.setString(2, password);
        ps.executeUpdate();
    }

    public User getUserByID(int id) throws SQLException {
            Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from Utente where IdUtente = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setID(rs.getInt("IdUtente"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                return user;
            }
            else return null;
    }

    public User getUserByUsername(String username) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("select * from Utente where Username = '?'");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setID(rs.getInt("IdUtente"));
            user.setUsername(rs.getString("Username"));
            user.setPassword(rs.getString("Password"));
            return user;
        }
        else return null;
    }

    // La delete dell'utente dovrebbe anche cancellare i suoi pagamenti, la sua collezione e i suoi completamenti
    /*
    public void deleteUserByID(int id) throws SQLException
    {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("delete from user where ID = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
     */
}
