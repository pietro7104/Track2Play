package Controller.AccountManagement;

import Controller.Utility;
import Model.User;
import Model.UserManagement;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/ModifyPassword")
public class ModifyPasswordServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newPassword = request.getParameter("newPassword");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        // Controllo se l'utente è loggato
        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per eseguire questa funzione.");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        UserManagement userManagement = new UserManagement();

        try{
            userManagement.modifyUserPasswordById(loggedUser.getID(), newPassword);

            // Aggiornamento dell'utente in sessione
            session.setAttribute("user", userManagement.getUserByID(loggedUser.getID()));

        } catch (Exception e){
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nella modifica della password");
            } else {
                Utility.addError(request, e.getMessage());
            }

        } finally {
            RequestDispatcher rd = request.getRequestDispatcher("Settings Page.jsp");
            rd.forward(request, response);
        }
    }
}
