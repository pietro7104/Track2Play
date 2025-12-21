package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void addUser(int id, String username, String password) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("insert into User values(?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, username);
        ps.setString(3, password);
        ps.executeUpdate();
    }

    public User getUserByID(int id) throws SQLException {
            Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from users where ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setID(rs.getInt("ID"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
            else return null;
    }

    public User getUserByUsername(String username) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("select * from user where username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setID(rs.getInt("ID"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }
        else return null;
    }

    public void deleteUserByID(int id) throws SQLException
    {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("delete from user where ID = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
