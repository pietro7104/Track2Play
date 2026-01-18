package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class WishlistDAO {

    public void addItemToWishlist(String username, WishlistItem item) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO wishlist (username, name, addedDate) VALUES (?, ?, ?)"
        );
        ps.setString(1, username);
        ps.setString(2, item.getName());
        ps.setTimestamp(3, new Timestamp(item.getAddedDate().getTime()));
        ps.executeUpdate();
        ps.close();
    }

    public List<WishlistItem> getWishlistItems(String username) throws SQLException {
        List<WishlistItem> items = new ArrayList<>();
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM wishlist WHERE username = ?"
        );
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            WishlistItem item = new WishlistItem();
            item.setUsername(rs.getString("username"));
            item.setName(rs.getString("name"));
            item.setAddedDate(rs.getTimestamp("addedDate"));
            items.add(item);
        }
        rs.close();
        ps.close();
        return items;
    }


    public void removeItemFromWishlist(String username, String name) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "DELETE FROM wishlist WHERE username = ? AND name = ?"
        );
        ps.setString(1, username);
        ps.setString(2, name);
        ps.executeUpdate();
        ps.close();
    }
}

