package Controller.CollectionManagement;

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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@WebServlet("/Purchase")
public class PurchaseServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gameId = request.getParameter("gameId");

        float price = Float.parseFloat(request.getParameter("price"));
        String currency = request.getParameter("currency_code");
        String platform = request.getParameter("platform");
        Boolean gift = Boolean.parseBoolean(request.getParameter("gift"));

        String dateString = request.getParameter("buy-date");
        String timeString = request.getParameter("buy-time");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        String fullTime = dateString + " " + timeString + " " + "UTC+06:00";


        Instant instant = Instant.from(formatter.parse(fullTime));
        Date date = Date.from(instant);



        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per registrare l'acquisto");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        UserManagement userManagement = new UserManagement();

        try {
            userManagement.makePurchase(loggedUser.getID(), gameId, date, platform, price, currency, gift);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Utility.addError(request, "Errore nella registrazione dell'acquisto");
            RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);
            return;
        }

        response.sendRedirect("OpenGamePage?itadid="+gameId);

    }

}
