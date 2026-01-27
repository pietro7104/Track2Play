package Service;

import Controller.Utility;
import Model.User;
import Model.UserDAO;

import java.sql.SQLException;

public class UserAccountService {

    public final UserDAO dao;

    public UserAccountService() {
        this.dao = new UserDAO();
    }

    public void registerUser(String username, String password, String codiceISO) throws IllegalArgumentException, SQLException {
        // Controlli sull'username, password, codiceISO
        if(!checkUserUsername(username))
            throw new IllegalArgumentException("Username non valido.");

        if(!checkUserPassword(password))
            throw new IllegalArgumentException("Password non valida.");

        if(!checkUserCodiceISO(codiceISO))
            throw new IllegalArgumentException("Codice ISO non valido.");


        // Controllo se è già presente un utente con l'username inserito
        if (dao.getUserByUsername(username) != null){
            throw new RuntimeException("Username già in uso");
        }

        // Hashing della password
        String hashedPassword = Utility.toHash(password);

        // Salvataggio dell'utente nel database
        dao.addUser(username, hashedPassword, codiceISO);
    }

    public User checkCredentialsAndGetUser(String username, String password) throws SQLException, IllegalArgumentException {
        // Controlli sull'username, password
        if(!checkUserUsername(username))
            throw new IllegalArgumentException("Username non valido.");

        if(!checkUserPassword(password))
            throw new IllegalArgumentException("Password non valida.");

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

    public void deleteUserById(int id) throws SQLException, IllegalArgumentException {
        if(!checkUserId(id))
            throw new IllegalArgumentException("Id non valido.");

        dao.deleteUserByID(id);
    }

    protected boolean checkUserId(int id) {
        if(id <= 0)
            return false;
        return true;
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

    protected boolean checkUserCodiceISO(String codiceISO) {
        if(codiceISO.length() > 2 || codiceISO.isBlank() || codiceISO.isEmpty())
            return false;
        return true;
    }

}
