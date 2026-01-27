package Controller.AccountManagement;

import Controller.Utility;
import Model.User;
import Model.UserDAO;
import Service.UserAccountService;
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
        User toDelete = (User) session.getAttribute("user");

        UserAccountService service = new UserAccountService();

        try{
            service.deleteUserById(toDelete.getID());
        } catch (Exception e){
            System.out.println(e.getMessage());
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nella cancellazione account.");
            } else {
                Utility.addError(request, e.getMessage());
            }

            RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);
        }
    }
}
