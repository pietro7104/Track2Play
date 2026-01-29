package Controller.CollectionManagement;
import Controller.Utility;
import Model.Game;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@WebServlet("/CollectionAdd")
public class AddCollectionItemServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameId = request.getParameter("gameId");
        String gameTitle = request.getParameter("gameTitle");
        String gameBanner = request.getParameter("gameBanner");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per aggiungere giochi alla collezione");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        Game gameToAdd = new Game(gameId, gameTitle, gameBanner);
        UserManagement userManagement = new UserManagement();

        try {
            userManagement.addGameToCollection(loggedUser.getID(), gameToAdd);

            response.sendRedirect("CollectionView");
        }catch (Exception e){
            System.out.println(e.getMessage());
            Utility.addError(request, "Errore nell'aggiunta del gioco alla collezione");

            ViewCollectionServlet viewCollectionServlet = new ViewCollectionServlet();
            viewCollectionServlet.doGet(request, response);
        }
    }
}

