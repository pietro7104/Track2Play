package Controller.WishlistManagement;

import Controller.Utility;
import Model.*;
import Model.APIControl.APIExceptions.APIException;
import Model.APIControl.APIInterface;
import Service.AddGameInfoService;
import Service.CollectionService;
import Service.WishlistService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


@WebServlet("/WishlistAdd")
public class AddWishlistItemServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ottieni l'id del gioco dal form
        String gameId = request.getParameter("gameId");
        String gameTitle = request.getParameter("gameTitle");
        String gameBanner = request.getParameter("gameBanner");


        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per aggiungere articoli alla wishlist");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }


        try{
            APIInterface api = Utility.getAPI();
            Game game = api.GetGameInfoByIsThereAnyDealID(gameId);
            TagDAO tagDAO = new TagDAO();
            CollectionService cs = new CollectionService();
            List<CollectionItem> collectionItemList = cs.getUserCollection(loggedUser.getID());

            boolean inCollection = false;
            for (CollectionItem collectionItem : collectionItemList) {
                if (collectionItem.getGameId().equals(gameId)) {
                    inCollection = true;
                    break;
                }
            }

            if (!inCollection) {
                AddGameInfoService gi = new AddGameInfoService();
                gi.addGameInfo(loggedUser.getID(), game);
            }
        }catch (APIException | SQLException e){
            System.out.println(e.getMessage());
        }

        Game gameToAdd = new Game(gameId, gameTitle, gameBanner);
        UserManagement userManagement = new UserManagement();

        try {
            userManagement.addGameToWishlist(loggedUser.getID(), gameToAdd, new Date());
            response.sendRedirect("WishlistView");  // Reindirizza alla pagina di visualizzazione della wishlist
        } catch (Exception e){
            System.out.println(e.getMessage());
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nell'aggiungere l'articolo alla wishlist.");
            } else {
                Utility.addError(request, e.getMessage());
            }

            RequestDispatcher rd = request.getRequestDispatcher("Wishlist.jsp");
            rd.forward(request, response);
        }
    }
}
