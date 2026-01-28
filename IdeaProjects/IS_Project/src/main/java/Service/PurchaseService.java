package Service;

import Model.Purchase;
import Model.PurchaseDAO;

import java.sql.SQLException;
import java.util.List;

public class PurchaseService {

    private final PurchaseDAO dao;

    public PurchaseService() {
        dao = new PurchaseDAO();
    }

    public List<Purchase> getUserPurchases(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Id utente non valido");
        }
        return dao.getUserPurchases(userId);
    }
}
