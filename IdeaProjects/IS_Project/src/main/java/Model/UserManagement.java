package Model;

import Service.CollectionService;
import Service.UserAccountService;
import Service.WishlistService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserManagement {

    final UserAccountService userAccountService = new UserAccountService();
    final CollectionService collectionService = new CollectionService();
    final WishlistService wishlistService = new WishlistService();

    public void registerUser(String username, String password, String isoCountryCode) throws SQLException, IllegalArgumentException {
        userAccountService.registerUser(username, password, isoCountryCode);
    }

    public User checkCredentialsAndGetUser(String username, String password) throws SQLException, IllegalArgumentException {
        return userAccountService.checkCredentialsAndGetUser(username, password);
    }

    public void deleteUserById(int id) throws SQLException, IllegalArgumentException{
        userAccountService.deleteUserById(id);
    }

    public List<CollectionItem> getUserCollection(int userId) throws SQLException, IllegalArgumentException{
        return collectionService.getUserCollection(userId);
    }

    public void addGameToCollection(int userId, String gameId) throws SQLException, IllegalArgumentException, IllegalStateException{
        collectionService.addGame(userId, gameId);
    }

    public void removeGameFromCollection(int userId, String gameId) throws SQLException, IllegalArgumentException, IllegalStateException{
        collectionService.removeGame(userId, gameId);
    }

    public void setCompleted(int userId, String gameId, boolean completed) throws SQLException, IllegalArgumentException, IllegalStateException{
        collectionService.setCompleted(userId, gameId, completed);
    }

    public void addGameToWishlist(int userId, String gameId, Date date) throws SQLException, IllegalArgumentException{
        wishlistService.addGameToWishlist(userId, gameId, date);
    }

    public void removeFromWishlistById(int userId, String gameId) throws SQLException, IllegalArgumentException {
        wishlistService.removeFromWishlistById(userId, gameId);
    }

    public List<WishlistItem> getWishlistedGamesByUserId(int userId) throws SQLException, IllegalArgumentException {
        return wishlistService.getWishlistedGamesByUserId(userId);
    }

    public List<WishlistItem> getWishlistedGamesByUserUsername(String username) throws SQLException, IllegalArgumentException {
        return wishlistService.getWishlistedGamesByUserUsername(username);
    }
}
