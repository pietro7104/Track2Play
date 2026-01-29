package Service;

import Model.Game;
import Model.GameDAO;

import java.sql.SQLException;

public class GameService {

    private static final GameDAO GAME_DAODAO = new GameDAO();

    public GameService() {}

    // Aggiunge un gioco se non esiste già
    protected void addGame(Game game) throws SQLException {
        if(!checkGameID(game.getIsThereAnyDealID()))
            throw new IllegalArgumentException("Id del gioco non valido.");

        if (!doesGameAlreadyExists(game)) {
            GAME_DAODAO.addGame(game);
        }
    }

    // Recupera un gioco tramite ID
    public Game getGameById(String gameId) throws SQLException, IllegalArgumentException {
        if (!checkGameID(gameId))
            throw new IllegalArgumentException("ID gioco non valido");

        return GAME_DAODAO.getGameById(gameId);
    }

    protected static boolean doesGameAlreadyExists(Game game) throws SQLException {
        return GAME_DAODAO.doesGameExist(game.getIsThereAnyDealID());
    }

    protected static boolean checkGameID(String gameId) {
        return gameId != null && gameId.length() <= 36 && !gameId.isBlank() && !gameId.isEmpty();
    }
}

