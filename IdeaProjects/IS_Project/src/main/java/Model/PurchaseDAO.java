package Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseDAO {

    public List<Purchase> getUserPurchases(int userId) throws SQLException {
        List<Purchase> purchases = new ArrayList<>();

        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT A.Data_Acquisto, A.IdGioco, G.Titolo, A.Piattaforma, " +
                        "A.Prezzo, A.Moneta_ISO, A.Regalo " +
                        "FROM Acquisto A JOIN Gioco G ON A.IdGioco = G.IdGioco " +
                        "WHERE A.IdUtente = ? " +
                        "ORDER BY A.Data_Acquisto DESC"
        );


        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Purchase p = new Purchase();
            p.setDate(rs.getTimestamp("Data_Acquisto"));
            p.setGameId(rs.getString("IdGioco"));
            p.setGameTitle(rs.getString("Titolo"));
            p.setPlatform(rs.getString("Piattaforma"));
            p.setPrice(rs.getDouble("Prezzo"));
            p.setCurrency(rs.getString("Moneta_ISO"));
            p.setGift(rs.getBoolean("Regalo"));

            purchases.add(p);
        }

        rs.close();
        ps.close();

        return purchases;
    }

    public void makePurchase(int userID, String gameID, java.sql.Timestamp date, String platform, float price, String currency, boolean gift) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO Acquisto VALUES (?, ?, ?, ?, ?, ?, ?)");

        ps.setTimestamp(1, date);
        ps.setInt(2, userID);
        ps.setString(3, gameID);
        ps.setString(4, platform);
        ps.setFloat(5, price);
        ps.setBoolean(6, gift);
        ps.setString(7, currency);

        ps.executeUpdate();
    }
}

