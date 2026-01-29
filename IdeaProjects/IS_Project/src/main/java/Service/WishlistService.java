package Service;

import Model.Game;
import Model.WishlistItem;
import Model.WishlistItemDAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class WishlistService {

    public final static WishlistItemDAO WISHLIST_ITEM_DAO = new WishlistItemDAO();

    public WishlistService() { }

    public void addGameToWishlist(int userId, Game gameToAdd, Date date) throws SQLException, IllegalArgumentException {
        if(!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Utente non valido");

        // Aggiunta del gioco nel sistema
        GameService gameService = new GameService();
        gameService.addGame(gameToAdd);

        WISHLIST_ITEM_DAO.addItemToWishlist(userId, gameToAdd.getIsThereAnyDealID(), date);
    }

    public void removeFromWishlistById(int userId, String gameId) throws SQLException, IllegalArgumentException {
        if(!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Utente non valido");

        if(!GameService.checkGameID(gameId))
            throw new IllegalArgumentException("Id del gioco non valido");

        WISHLIST_ITEM_DAO.removeItemFromWishlistByGameId(userId, gameId);
    }

    public List<WishlistItem> getWishlistedGamesByUserId(int userId) throws SQLException, IllegalArgumentException {
        if(!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Utente non valido");

        return WISHLIST_ITEM_DAO.getWishlistItemsByUserId(userId);
    }

    public List<WishlistItem> getWishlistedGamesByUserUsername(String username) throws SQLException, IllegalArgumentException {
        if(!UserAccountService.checkUserUsername(username))
            throw new IllegalArgumentException("Utente non valido");

        return WISHLIST_ITEM_DAO.getWishlistItemsByUsername(username);
    }
}
