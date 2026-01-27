package Service;
import Model.CollectionItem;
import Model.CollectionItemDAO;

import java.sql.SQLException;
import java.util.List;

public class CollectionService {
    private final CollectionItemDAO dao;

    public CollectionService() {
        dao = new CollectionItemDAO();
    }

    public List<CollectionItem> getUserCollection(int userId) throws SQLException{
        if (userId <= 0) {
            throw new IllegalArgumentException("Id utente non valido");
        }
        return dao.getCollectionItemsByUserId(userId);
    }

    public void addGame(int userId, String gameId) throws SQLException{
        validateUserAndGame(userId, gameId);
        if (existsInCollection(userId, gameId)) {
            throw new IllegalStateException("Gioco già presente nella collezione");
        }
        dao.addGameToCollection(userId, gameId);
    }

    public void removeGame(int userId, String gameId) throws SQLException{
        validateUserAndGame(userId, gameId);
        if (!existsInCollection(userId, gameId)) {
            throw new IllegalStateException("Gioco non presente nella collezione");
        }
        dao.removeGameFromCollection(userId, gameId);
    }

    public void setCompleted(int userId, String gameId, boolean completed) throws SQLException{
        validateUserAndGame(userId, gameId);
        if (!existsInCollection(userId, gameId)) {
            throw new IllegalStateException("Impossibile aggiornare: gioco non presente");
        }
        dao.updateCompletionStatus(userId, gameId, completed);
    }

    private void validateUserAndGame(int userId, String gameId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Id utente non valido");
        }
        if (gameId == null || gameId.isBlank()) {
            throw new IllegalArgumentException("Id gioco non valido");
        }
    }

    private boolean existsInCollection(int userId, String gameId) throws SQLException {
        for (CollectionItem item : dao.getCollectionItemsByUserId(userId)) {
            if (gameId.equals(item.getGameId())) {
                return true; // trovato
            }
        }
        return false; // non trovato
    }
}
