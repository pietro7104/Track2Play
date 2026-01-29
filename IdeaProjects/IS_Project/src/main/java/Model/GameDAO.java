package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameDAO {

    // Metodo per aggiungere un gioco alla tabella Gioco
    public void addGame(Game game) throws SQLException {
        Connection con = Database.getConnection();
        // Inserisce un gioco se non esiste già
        if (!doesGameExist(game.getIsThereAnyDealID())) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Gioco (IdGioco, Titolo, Copertina) VALUES (?, ?, ?)"
            );
            ps.setString(1, game.getIsThereAnyDealID());
            ps.setString(2, game.getTitle());
            ps.setString(3, game.getAssets().get("banner"));
            ps.executeUpdate();
            ps.close();
        }
        con.close();
    }

    // Metodo per verificare se un gioco esiste già nella tabella Gioco
    public boolean doesGameExist(String gameId) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM Gioco WHERE IdGioco = ?"
        );
        ps.setString(1, gameId);
        ResultSet rs = ps.executeQuery();

        boolean exists = false;
        if (rs.next() && rs.getInt(1) > 0) {
            exists = true;
        }

        rs.close();
        ps.close();
        con.close();
        return exists;
    }

    // Metodo per recuperare un gioco dalla tabella Gioco
    public Game getGameById(String gameId) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT IdGioco, Titolo, Copertina FROM Gioco WHERE IdGioco = ?"
        );
        ps.setString(1, gameId);
        ResultSet rs = ps.executeQuery();

        Game game = null;
        if (rs.next()) {
            game = new Game(
                    rs.getString("IdGioco"),
                    rs.getString("Titolo"),
                    rs.getString("Copertina")
            );
        }

        rs.close();
        ps.close();
        con.close();
        return game;
    }
}

