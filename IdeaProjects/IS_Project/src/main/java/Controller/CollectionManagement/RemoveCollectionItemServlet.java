package Controller.CollectionManagement;
import Controller.Utility;
import Model.*;
import Model.APIControl.APIExceptions.APIException;
import Model.APIControl.APIInterface;

import Service.AddGameInfoService;
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
import java.util.List;

@WebServlet("/CollectionRemove")
public class RemoveCollectionItemServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameId = request.getParameter("gameId");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per rimuovere giochi dalla collezione");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        try{
            APIInterface api = Utility.getAPI();
            Game game = api.GetGameInfoByIsThereAnyDealID(gameId);
            TagDAO tagDAO = new TagDAO();
            WishlistService ws = new WishlistService();
            List<WishlistItem> wishlistItemList = ws.getWishlistedGamesByUserId(loggedUser.getID());

            boolean inWishlist = false;
            for (WishlistItem wishlistItem : wishlistItemList) {
                if (wishlistItem.getGameId().equals(gameId)) {
                    inWishlist = true;
                    break;
                }
            }

            if (!inWishlist) {
                AddGameInfoService gi = new AddGameInfoService();
                gi.removeGameInfo(loggedUser.getID(), game);
            }
        }catch (APIException | SQLException e){
            System.out.println(e.getMessage());
        }

        UserManagement userManagement = new UserManagement();

        try {
            userManagement.removeGameFromCollection(loggedUser.getID(), gameId);
            System.out.println("removed");
            response.sendRedirect("CollectionView");
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nella rimozione del gioco dalla collezione.");
            } else {
                Utility.addError(request, e.getMessage());
            }

            ViewCollectionServlet viewCollectionServlet = new ViewCollectionServlet();
            viewCollectionServlet.doGet(request, response);
        }
    }
}

