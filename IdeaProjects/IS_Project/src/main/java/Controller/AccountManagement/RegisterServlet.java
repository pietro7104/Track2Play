package Controller.AccountManagement;

import Controller.Utility;
import Model.UserManagement;
import Service.UserAccountService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String codiceISO = request.getParameter("codiceISO");


        UserManagement userManagement = new UserManagement();

        try{
            userManagement.registerUser(username, password, codiceISO);
        } catch (Exception e){
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nella registrazione");
            } else {
                Utility.addError(request, e.getMessage());
            }

            RequestDispatcher rd = request.getRequestDispatcher("Register.jsp");
            rd.forward(request, response);
            return;
        }


        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
