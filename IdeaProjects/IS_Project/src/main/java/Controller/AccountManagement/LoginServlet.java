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

import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        User u;
        try {
            u = userDAO.getUserByUsername(username);
        }
        catch (SQLException e){
            Utility.addError(request, "Errore nel login");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        if (u == null){
            Utility.addError(request, "Username e/o password incorretti");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            System.out.println("User not found");
            return;
        }

        if (Utility.toHash(password).equals(u.getPassword())){
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            //session.setMaxInactiveInterval(60); //??

            RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);
            return;
        }
        else {
            Utility.addError(request, "Username e/o password incorretti");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

    }
}
