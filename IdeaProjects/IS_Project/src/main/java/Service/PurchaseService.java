package Service;

import Model.Purchase;
import Model.PurchaseDAO;

import java.sql.SQLException;
import java.util.List;

public class PurchaseService {

    private final static PurchaseDAO PURCHASE_DAO = new PurchaseDAO();

    public PurchaseService() { }

    public List<Purchase> getUserPurchases(int userId) throws SQLException {
        if (!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Id utente non valido");

        return PURCHASE_DAO.getUserPurchases(userId);
    }
}
