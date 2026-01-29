package Service;
import Model.CollectionItem;
import Model.CollectionItemDAO;

import java.sql.SQLException;
import java.util.List;

public class CollectionService {
    private final static CollectionItemDAO COLLECTION_ITEM_DAO = new CollectionItemDAO();

    public CollectionService() { }

    public List<CollectionItem> getUserCollection(int userId) throws SQLException, IllegalArgumentException{
        if(!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Utente non valido");

        return COLLECTION_ITEM_DAO.getCollectionItemsByUserId(userId);
    }

    public void addGame(int userId, String gameId) throws SQLException, IllegalArgumentException, IllegalStateException{
        if(!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Utente non valido");

        if(!GameService.checkGameID(gameId))
            throw new IllegalArgumentException("Id del gioco non valido");

        if (existsInCollection(userId, gameId)) {
            throw new IllegalStateException("Gioco già presente nella collezione");
        }
        COLLECTION_ITEM_DAO.addGameToCollection(userId, gameId);
    }

    public void removeGame(int userId, String gameId) throws SQLException, IllegalArgumentException, IllegalStateException{
        if(!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Utente non valido");

        if(!GameService.checkGameID(gameId))
            throw new IllegalArgumentException("Id del gioco non valido");

        if (!existsInCollection(userId, gameId)) {
            throw new IllegalStateException("Gioco non presente nella collezione");
        }
        COLLECTION_ITEM_DAO.removeGameFromCollection(userId, gameId);
    }

    public void setCompleted(int userId, String gameId, boolean completed) throws SQLException, IllegalArgumentException, IllegalStateException{
        if(!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Utente non valido");

        if(!GameService.checkGameID(gameId))
            throw new IllegalArgumentException("Id del gioco non valido");

        if (!existsInCollection(userId, gameId)) {
            throw new IllegalStateException("Impossibile aggiornare: gioco non presente");
        }
        COLLECTION_ITEM_DAO.updateCompletionStatus(userId, gameId, completed);
    }

    private boolean existsInCollection(int userId, String gameId) throws SQLException {
        for (CollectionItem item : COLLECTION_ITEM_DAO.getCollectionItemsByUserId(userId)) {
            if (gameId.equals(item.getGameId())) {
                return true; // trovato
            }
        }
        return false; // non trovato
    }
}
