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

import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.SQLException;
import java.util.Random;

@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String codiceISO = request.getParameter("codiceISO");

        UserDAO userDAO = new UserDAO();
        User u = null;

        try{
            u = userDAO.getUserByUsername(username);
        }
        catch (SQLException e){
            Utility.addError(request, "Errore nella registrazione");
            RequestDispatcher rd = request.getRequestDispatcher("Register.jsp");
            rd.forward(request, response);
            return;
        }

        if (u != null){
            System.out.println("this username is already taken");
            //HANDLE EXCEPTION
            return;
        }

        String hashedPassword = Utility.toHash(password);

        try {
            userDAO.addUser(username, hashedPassword, codiceISO);
        }
        catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            //HANDLE EXCEPTION
            return;
        }


        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
