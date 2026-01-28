package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void addUser(String username, String password, String codiceISO) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("insert into Utente (Username, Password, Codice_ISO) values(?,?,?)");
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, codiceISO);
        ps.executeUpdate();
    }

    public User getUserByID(int id) throws SQLException {
            Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement("Select * from Utente where IdUtente = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setID(rs.getInt("IdUtente"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setCountryISO(rs.getString("Codice_ISO"));
                return user;
            }
            else return null;
    }

    public User getUserByUsername(String username) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("select * from Utente where Username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setID(rs.getInt("IdUtente"));
            user.setUsername(rs.getString("Username"));
            user.setPassword(rs.getString("Password"));
            user.setCountryISO(rs.getString("Codice_ISO"));
            return user;
        }
        else return null;
    }

    public void modifyUserPasswordById(int userId, String newPassword) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE utente SET Password = ? WHERE IdUtente = ?");
        ps.setString(1, newPassword);
        ps.setInt(2, userId);
        ps.executeUpdate();
    }

    public void modifyISOCodeById(int userId, String isoCode) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE utente SET Codice_ISO = ? WHERE IdUtente = ?");
        ps.setString(1, isoCode);
        ps.setInt(2, userId);
        ps.executeUpdate();
    }

    // La delete dell'utente dovrebbe anche cancellare i suoi pagamenti, la sua collezione e i suoi completamenti
    public void deleteUserByID(int id) throws SQLException
    {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("delete from user where ID = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
