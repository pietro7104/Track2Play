package Controller.AccountManagement;

import Controller.HomePageManagement.OpenHomePageServlet;
import Controller.Utility;
import Model.User;
import Model.UserDAO;
import Model.UserManagement;
import Service.UserAccountService;
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

        UserManagement userManagement = new UserManagement();
        User foundUser;

        try {
            foundUser = userManagement.checkCredentialsAndGetUser(username, password);
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nel login");
            } else {
                Utility.addError(request, e.getMessage());
            }

            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", foundUser);
        //session.setMaxInactiveInterval(60); //??

        OpenHomePageServlet openHomePageServlet = new OpenHomePageServlet();
        openHomePageServlet.doGet(request, response);
        return;
        /*RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);*/
    }
}
