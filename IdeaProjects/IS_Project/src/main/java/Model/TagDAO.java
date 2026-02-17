package Model;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TagDAO {

    public void addTagAppearence(int userId, String tag, int n)  {
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM UtenteTag WHERE IdUtente = ? AND tag_name = ?");
            ps.setInt(1, userId);
            ps.setString(2, tag);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int appearences = rs.getInt("appearences");
                appearences += n;
                ps = conn.prepareStatement("UPDATE UtenteTag SET appearences =  ? WHERE IdUtente = ? AND tag_name = ?");
                ps.setInt(1, appearences);
                ps.setInt(2, userId);
                ps.setString(3, tag);
                ps.executeUpdate();
            }
            else{
                ps = conn.prepareStatement("INSERT INTO UtenteTag (IdUtente, tag_name, appearences) VALUES (?, ?, ?)");
                ps.setInt(1, userId);
                ps.setString(2, tag);
                ps.setInt(3, n);
                ps.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void removeTagAppearence(int userId, String tag, int n)  {
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM UtenteTag WHERE IdUtente = ? AND tag_name = ?");
            ps.setInt(1, userId);
            ps.setString(2, tag);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int appearences = rs.getInt("appearences");
                appearences -= n;
                if (appearences <= 0){
                    ps = conn.prepareStatement("DELETE FROM UtenteTag WHERE IdUtente = ? AND tag_name = ?");
                    ps.setInt(1, userId);
                    ps.setString(2, tag);
                    ps.executeUpdate();
                }
                else{
                    ps = conn.prepareStatement("UPDATE UtenteTag SET appearences =  ? WHERE IdUtente = ? AND tag_name = ?");
                    ps.setInt(1, appearences);
                    ps.setInt(2, userId);
                    ps.setString(3, tag);
                    ps.executeUpdate();
                }
            }
            else{
                System.out.println("Tag non trovato");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
