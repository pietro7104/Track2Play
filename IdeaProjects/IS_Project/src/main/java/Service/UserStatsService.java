package Service;
import Model.UserStats;
import Model.UserStatsDAO;

import java.sql.SQLException;

public class UserStatsService {

    private static final UserStatsDAO USER_STATS_DAO = new UserStatsDAO();

    public UserStatsService() {}

    public UserStats getStats(int userId) throws SQLException, IllegalArgumentException {
        if (!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Id utente non valido");

        return USER_STATS_DAO.getUserStats(userId);
    }
}

