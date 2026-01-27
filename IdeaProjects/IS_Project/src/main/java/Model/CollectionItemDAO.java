package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionItemDAO {
    // Visualizza Collezione
    public List<CollectionItem> getCollectionItemsByUserId(int userId) throws SQLException {
        List<CollectionItem> items = new ArrayList<>();
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT A.IdGioco, G.Copertina, G.Titolo, A.Data_Aggiunta, C.Stato " +
                "FROM Aggiunto A " +
                "JOIN Gioco G ON G.IdGioco = A.IdGioco " +
                "LEFT JOIN Completamento C ON C.IdUtente = A.IdUtente AND C.IdGioco = A.IdGioco " +
                "WHERE A.IdUtente = ?"
        );

        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            CollectionItem item = new CollectionItem();

            item.setGameId(rs.getString("IdGioco"));
            item.setCover(rs.getString("Copertina"));
            item.setGameTitle(rs.getString("Titolo"));
            item.setDateAdded(rs.getDate("Data_Aggiunta"));
            item.setCompleted(rs.getBoolean("Stato"));

            items.add(item);
        }

        rs.close();
        ps.close();
        return items;
    }

    // Aggiungi gioco alla collezione
    public void addGameToCollection(int userId, String gameId) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Aggiunto VALUES (?, ?, NOW())"
        );

        ps.setInt(1, userId);
        ps.setString(2, gameId);
        ps.executeUpdate();
        ps.close();
    }

    // Rimuovi gioco dalla collezione
    // Il delete dalla tabella "Aggiunto" dovrebbe aggiornare gli attributi derivabili nella tabella "Collezione"
    public void removeGameFromCollection(int userId, String gameId) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "DELETE FROM Aggiunto WHERE IdUtente=? AND IdGioco=?"
        );

        ps.setInt(1, userId);
        ps.setString(2, gameId);
        ps.executeUpdate();
        ps.close();
    }

    // Aggiorna stato completamento
    public void updateCompletionStatus(int userId, String gameId, boolean completed) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Completamento SET Stato=? WHERE IdUtente=? AND IdGioco=?"
        );

        ps.setBoolean(1, completed);
        ps.setInt(2, userId);
        ps.setString(3, gameId);
        ps.executeUpdate();
        ps.close();
    }
}
