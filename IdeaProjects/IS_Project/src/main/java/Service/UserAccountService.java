package Service;

import Controller.HomePageManagement.OpenHomePageServlet;
import Controller.Utility;
import Model.User;
import Model.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

public class UserAccountService {

    public final UserDAO dao;

    public UserAccountService() {
        this.dao = new UserDAO();
    }

    public void registerUser(String username, String password, String codiceISO) throws IllegalAccessException, SQLException {
        // Controlli sull'username, password, codiceISO
        if(!checkUserUsername(username))
            throw new IllegalAccessException("Username non valido.");

        if(!checkUserPassword(password))
            throw new IllegalAccessException("Password non valida.");

        if(!checkCodiceISO(codiceISO))
            throw new IllegalAccessException("Codice ISO non valido.");


        // Controllo se è già presente un utente con l'username inserito
        if (dao.getUserByUsername(username) != null){
            throw new RuntimeException("Username già in uso");
        }

        // Hashing della password
        String hashedPassword = Utility.toHash(password);

        // Salvataggio dell'utente nel database
        dao.addUser(username, hashedPassword, codiceISO);
    }

    public User checkCredentialsAndGetUser(String username, String password) throws SQLException, IllegalAccessException {
        // Controlli sull'username, password
        if(!checkUserUsername(username))
            throw new IllegalAccessException("Username non valido.");

        if(!checkUserPassword(password))
            throw new IllegalAccessException("Password non valida.");

        User foundUser;

        // Cerco nel database un utente con quell'username
        foundUser = dao.getUserByUsername(username);

        // Se nessun utente con quell'username è trovato
        if (foundUser == null)
            throw new RuntimeException("Nessun utente trovato con l'username inserito.");

        // Se è stato trovato un utente con l'username inserito controlliamo se gli hash delle password coincidono
        if (Utility.toHash(password).equals(foundUser.getPassword())){
            return foundUser;
        } else {
            throw new RuntimeException("Password incorretta.");
        }
    }

    protected boolean checkUserUsername(String username) {
        if(username.length() > 30 || username.isBlank() || username.isEmpty())
            return false;
        return true;
    }

    protected boolean checkUserPassword(String password) {
        if(password.length() > 15 || password.isBlank() || password.isEmpty())
            return false;
        return true;
    }

    protected boolean checkCodiceISO(String codiceISO) {
        if(codiceISO.length() > 2 || codiceISO.isBlank() || codiceISO.isEmpty())
            return false;
        return true;
    }

}
