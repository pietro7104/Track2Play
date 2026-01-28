package Service;

import Controller.Utility;
import Model.User;
import Model.UserDAO;

import java.sql.SQLException;

public class UserAccountService {

    public final static UserDAO USER_DAO = new UserDAO();

    public UserAccountService() { }

    public User getUserByID(int id) throws SQLException, IllegalArgumentException {
        if(!checkUserId(id))
            throw new IllegalArgumentException("Id non valido.");

        return USER_DAO.getUserByID(id);
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
        if (USER_DAO.getUserByUsername(username) != null){
            throw new RuntimeException("Username già in uso");
        }

        // Hashing della password
        String hashedPassword = Utility.toHash(password);

        // Salvataggio dell'utente nel database
        USER_DAO.addUser(username, hashedPassword, codiceISO);
    }

    public User checkCredentialsAndGetUser(String username, String password) throws SQLException, IllegalArgumentException {
        // Controlli sull'username, password
        if(!checkUserUsername(username))
            throw new IllegalArgumentException("Username non valido.");

        if(!checkUserPassword(password))
            throw new IllegalArgumentException("Password non valida.");

        User foundUser;

        // Cerco nel database un utente con quell'username
        foundUser = USER_DAO.getUserByUsername(username);

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

    public void modifyUserPasswordById(int id, String newPassword) throws SQLException, IllegalArgumentException {
        if(!checkUserId(id))
            throw new IllegalArgumentException("Id non valido.");

        if(!checkUserPassword(newPassword))
            throw new IllegalArgumentException("Password non valida.");

        // Hashing della password
        String hashedPassword = Utility.toHash(newPassword);

        // Salvataggio dell'utente nel database
        USER_DAO.modifyUserPasswordById(id, hashedPassword);
    }

    public void modifyISOCodeById(int id, String isoCode) throws SQLException, IllegalArgumentException {
        if(!checkUserId(id))
            throw new IllegalArgumentException("Id non valido.");

        if(!checkUserCodiceISO(isoCode))
            throw new IllegalArgumentException("Codice ISO non valido.");

        USER_DAO.modifyISOCodeById(id, isoCode);
    }

    public void deleteUserById(int id) throws SQLException, IllegalArgumentException {
        if(!checkUserId(id))
            throw new IllegalArgumentException("Id non valido.");

        USER_DAO.deleteUserByID(id);
    }

    protected static boolean checkUserId(int id) {
        return id > 0;
    }

    protected static boolean checkUserUsername(String username) {
        return username.length() <= 30 && !username.isBlank() && !username.isEmpty();
    }

    protected static boolean checkUserPassword(String password) {
        return password.length() <= 15 && !password.isBlank() && !password.isEmpty();
    }

    protected static boolean checkUserCodiceISO(String codiceISO) {
        return codiceISO.length() <= 2 && !codiceISO.isBlank() && !codiceISO.isEmpty();
    }

}
