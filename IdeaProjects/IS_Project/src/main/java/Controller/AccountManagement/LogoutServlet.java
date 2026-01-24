package Controller.AccountManagement;

import Controller.HomePageManagement.OpenHomePageServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();

        OpenHomePageServlet openHomePageServlet = new OpenHomePageServlet();
        openHomePageServlet.doGet(request, response);
        return;
        /*RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
        rd.forward(request, response);*/
    }
}
