package Controller.AccountManagement;

import Controller.Utility;
import Model.User;
import Model.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jboss.weld.context.http.Http;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/DeleteAccount")
public class DeleteAccountServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");
        UserDAO userDAO = new UserDAO();

        User toDelete;
        try{
            toDelete = userDAO.getUserByUsername(username);
            userDAO.deleteUserByID(toDelete.getID());
        }
        catch (SQLException e){
            Utility.addError(request, "Errore nella cancellazione dell'account");
            RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
        rd.forward(request, response);
        return;
    }
}
