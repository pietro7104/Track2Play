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
        return dao.getCollectionItemsByUserId(userId);
    }

    public void addGame(int userId, String gameId) throws SQLException{
        dao.addGameToCollection(userId, gameId);
    }

    public void removeGame(int userId, String gameId) throws SQLException{
        dao.removeGameFromCollection(userId, gameId);
    }

    public void setCompleted(int userId, String gameId, boolean completed) throws SQLException{
        dao.updateCompletionStatus(userId, gameId, completed);
    }
}
