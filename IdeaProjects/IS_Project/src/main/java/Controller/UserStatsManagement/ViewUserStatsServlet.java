package Controller.UserStatsManagement;
import Controller.Utility;
import Model.Purchase;
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

        try {
            UserStatsService statsService = new UserStatsService();
            PurchaseService purchaseService = new PurchaseService();

            UserStats stats = statsService.getStats(loggedUser.getID());
            List<Purchase> purchases = purchaseService.getUserPurchases(loggedUser.getID());

            request.setAttribute("stats", stats);
            request.setAttribute("purchases", purchases);

            RequestDispatcher rd = request.getRequestDispatcher("Stats.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            Utility.addError(request, "Errore nel caricamento delle statistiche");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }
}



