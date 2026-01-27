package Model.Service;

import Model.WishlistItem;
import Model.WishlistItemDAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class WishlistService {

    public final WishlistItemDAO dao;

    public WishlistService() {
        this.dao = new WishlistItemDAO();
    }

    public void addGameToWishlist(int userId, String gameId, Date date) throws SQLException {
        dao.addItemToWishlist(userId, gameId, date);
    }

    public void removeFromWishlistById(int userId, String gameId) throws SQLException {
        dao.removeItemFromWishlistByGameId(userId, gameId);
    }

    public List<WishlistItem> getWishlistedGamesByUserId(int userId) throws SQLException {
        return dao.getWishlistItemsByUserId(userId);
    }

    public List<WishlistItem> getWishlistedGamesByUserUsername(String username) throws SQLException {
        return dao.getWishlistItemsByUsername(username);
    }
}
