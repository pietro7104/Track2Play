package Controller.UserStatsManagement;
import Controller.Utility;
import Model.Purchase;
import Model.UserManagement;
import Model.UserStats;
import Service.PurchaseService;
import Service.UserStatsService;
import Model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
@WebServlet("/ViewStats")
public class ViewUserStatsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        UserManagement userManagement = new UserManagement();
        UserStats stats = null;
        List<Purchase> purchases = null;

        try {
            stats = userManagement.getStats(loggedUser.getID());
            purchases = userManagement.getUserPurchases(loggedUser.getID());

        } catch (Exception e){
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nella registrazione");
            } else {
                Utility.addError(request, e.getMessage());
            }

            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        request.setAttribute("stats", stats);
        request.setAttribute("purchases", purchases);

        RequestDispatcher rd = request.getRequestDispatcher("Stats.jsp");
        rd.forward(request, response);
    }
}



