package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WishlistItemDAO {

    public void addItemToWishlist(int userId, String gameId, Date date) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Wishlist (IdUtente, IdGioco, Data_Aggiunta) VALUES (?, ?, ?)"
        );
        ps.setInt(1, userId);
        ps.setString(2, gameId);
        ps.setDate(3, new java.sql.Date(date.getTime()));
        ps.executeUpdate();
        ps.close();
    }

    public List<WishlistItem> getWishlistItemsByUserId(int userId) throws SQLException {
        List<WishlistItem> items = new ArrayList<>();
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT G.IdGioco, G.Copertina, G.Titolo, W.Data_Aggiunta " +
                        "FROM Wishlist W JOIN Gioco G ON G.IdGioco = W.IdGioco " +
                        "WHERE W.IdUtente = ?"
        );
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            WishlistItem item = new WishlistItem();
            // Set dei valori

            item.setGameId(rs.getString("IdGioco"));
            //item.setGameId(rs.getString("G.IdGioco"));

            item.setGameImageURL(rs.getString("Copertina"));
            item.setGameTitle(rs.getString("Titolo"));
            item.setAddedDate(rs.getDate("Data_Aggiunta"));

            items.add(item);
        }
        rs.close();
        ps.close();
        return items;
    }

    public List<WishlistItem> getWishlistItemsByUsername(String username) throws SQLException {
        List<WishlistItem> items = new ArrayList<>();
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT G.IdGioco, G.Copertina, G.Titolo, W.Data_Aggiunta " +
                        "FROM Utente U JOIN  Wishlist W ON U.IdUtente = W.IdUtente " +
                        "JOIN Gioco G ON G.IdGioco = W.IdGioco " +
                        "WHERE U.Username = ?"
        );
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            WishlistItem item = new WishlistItem();
            // Set dei valori

            item.setGameId(rs.getString("IdGioco"));
            //item.setGameId(rs.getString("G.IdGioco"));

            item.setGameImageURL(rs.getString("Copertina"));
            item.setGameTitle(rs.getString("Titolo"));
            item.setAddedDate(rs.getDate("Data_Aggiunta"));

            items.add(item);
        }
        rs.close();
        ps.close();
        return items;
    }

    public void removeItemFromWishlistByGameId(int userId, String gameId) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "DELETE FROM wishlist WHERE IdGioco = ? AND IdUtente = ?"
        );
        ps.setString(1, gameId);
        ps.setInt(2, userId);
        ps.executeUpdate();
        ps.close();
    }
}

