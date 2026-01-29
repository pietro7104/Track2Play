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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@WebServlet("/CollectionAdd")
public class AddCollectionItemServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameId = request.getParameter("gameId");

        float price = Float.parseFloat(request.getParameter("price"));
        String currency = request.getParameter("currency_code");
        String platform = request.getParameter("platform");
        Boolean gift = Boolean.parseBoolean(request.getParameter("gift"));

        String dateString = request.getParameter("buy-date");
        String timeString = request.getParameter("buy-time");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String fullTime = dateString + " " + timeString;

        Date date = java.sql.Date.valueOf(LocalDate.parse(fullTime, formatter));

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per aggiungere giochi alla collezione");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        UserManagement userManagement = new UserManagement();

        try {
            userManagement.addGameToCollection(loggedUser.getID(), gameId);
            userManagement.makePurchase(loggedUser.getID(), gameId, date, platform, price, currency, gift);

            response.sendRedirect("CollectionView");
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nell'aggiunta del gioco alla collezione.");
            } else {
                Utility.addError(request, e.getMessage());
            }

            ViewCollectionServlet viewCollectionServlet = new ViewCollectionServlet();
            viewCollectionServlet.doGet(request, response);
        }
    }
}

