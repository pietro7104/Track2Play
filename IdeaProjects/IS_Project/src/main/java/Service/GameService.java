package Service;

import Model.Game;
import Model.GameDAO;

import java.sql.SQLException;

public class GameService {

    private static final GameDAO GAME_DAODAO = new GameDAO();

    public GameService() {}

    // Aggiunge un gioco se non esiste già
    public void addGame(Game game) throws SQLException {
        if (!GAME_DAODAO.doesGameExist(game.getIsThereAnyDealID())) {
            GAME_DAODAO.addGame(game);
        }
    }


    // Recupera un gioco tramite ID
    public Game getGameById(String gameId) throws SQLException {
        if (gameId == null || gameId.isBlank()) {
            throw new IllegalArgumentException("ID gioco non valido");
        }
        return GAME_DAODAO.getGameById(gameId);
    }
}

