package Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStatsDAO {
    public UserStats getUserStats(int userId) throws SQLException {
        UserStats stats = new UserStats();

        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT " +
                        "C.Num_Tot, C.Num_Completati, C.Num_NonCompletati, " +

                        "(SELECT COUNT(DISTINCT A.IdGioco) " +
                        " FROM Acquisto A " +
                        " WHERE A.IdUtente = C.IdUtente AND A.Regalo = false) AS Giochi_Comprati, " +

                        "(SELECT COUNT(DISTINCT A.IdGioco) " +
                        " FROM Acquisto A " +
                        " WHERE A.IdUtente = C.IdUtente AND A.Regalo = true) AS Giochi_Regalo, " +

                        "(SELECT COALESCE(SUM(A.Prezzo), 0) " +
                        " FROM Acquisto A " +
                        " WHERE A.IdUtente = C.IdUtente " +
                        " AND A.Regalo = false   AS Spesa_Totale, " +

                        "(SELECT Codice_ISO FROM Utente WHERE IdUtente = C.IdUtente) AS Valuta " +

                        "FROM Collezione C WHERE C.IdUtente = ?"
        );

        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            stats.setTotalGames(rs.getInt("Num_Tot"));
            stats.setCompletedGames(rs.getInt("Num_Completati"));
            stats.setNotCompletedGames(rs.getInt("Num_NonCompletati"));

            stats.setPurchasedGames(rs.getInt("Giochi_Comprati"));
            stats.setGiftedGames(rs.getInt("Giochi_Regalo"));

            stats.setTotalSpent(rs.getDouble("Spesa_Totale"));
            stats.setCurrency(rs.getString("Valuta"));
        }

        rs.close();
        ps.close();
        return stats;
    }


}
