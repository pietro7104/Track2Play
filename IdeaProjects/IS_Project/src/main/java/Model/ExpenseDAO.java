package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseDAO {

    public void addExpense(int userID, String gameID, float price){
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM PrezzoGiocoAggiunto P WHERE P.IdUtente=? AND P.IdGioco=?");
            ps.setInt(1, userID);
            ps.setString(2, gameID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ps = conn.prepareStatement("UPDATE PrezzoGiocoAggiunto SET prezzo=? WHERE IdUtente=? AND IdGioco=?");
                ps.setFloat(1, price);
                ps.setInt(2, userID);
                ps.setString(3, gameID);
                ps.executeUpdate();
            }
            else{
                ps = conn.prepareStatement("insert into PrezzoGiocoAggiunto values(?,?,?)");
                ps.setInt(1, userID);
                ps.setString(2, gameID);
                ps.setFloat(3, price);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteExpense(int userID, String gameID){
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM PrezzoGiocoAggiunto WHERE IdUtente=? AND IdGioco=?");
            ps.setInt(1, userID);
            ps.setString(2, gameID);
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
