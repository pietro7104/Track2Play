package Service;

import Model.Purchase;
import Model.PurchaseDAO;

import java.sql.SQLException;
import java.util.List;

public class PurchaseService {

    private final static PurchaseDAO PURCHASE_DAO = new PurchaseDAO();

    public PurchaseService() { }

    public List<Purchase> getUserPurchases(int userId) throws SQLException, IllegalArgumentException {
        if (!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Id utente non valido");

        return PURCHASE_DAO.getUserPurchases(userId);
    }

    public void makePuchase(int userId, String gameID, java.util.Date date, String platform, float price, String currency, boolean gift) throws SQLException {
        if (!UserAccountService.checkUserId(userId))
            throw new IllegalArgumentException("Id utente non valido");
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        PURCHASE_DAO.makePurchase(userId, gameID, sqlDate, platform, price, currency, gift);
    }

}
