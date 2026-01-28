package Controller.WishlistManagement;

import Controller.Utility;
import Model.APIControl.APIExceptions.APIException;
import Model.APIControl.APIInterface;
import Model.APIControl.Price;
import Model.UserManagement;
import Service.WishlistService;
import Model.User;
import Model.WishlistItem;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@WebServlet("/Wishlist/View")
public class ViewWishlistServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per visualizzare la tua wishlist");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        UserManagement userManagement = new UserManagement();

        try {
            List<WishlistItem> wishlistItems = userManagement.getWishlistedGamesByUserId(loggedUser.getID());
            // List<WishlistItem> wishlistItems = wishlistItemDAO.getWishlistItemsByUsername(loggedUser.getUsername());
            APIInterface api = Utility.getAPI();
            ArrayList<String> ids = new ArrayList<>();
            for (WishlistItem wishlistItem : wishlistItems) {
                ids.add(wishlistItem.getGameId());
            }
            LinkedHashMap<String, Price> prices = api.GetGamesPrices(ids, api.GetShopIDs(loggedUser.getCountryISO()), loggedUser.getCountryISO(), false, 100, false);
            request.setAttribute("prices", prices);
            session.setAttribute("prices", prices);
            request.setAttribute("wishlistItems", wishlistItems);
            RequestDispatcher rd = request.getRequestDispatcher("Wishlist.jsp");
            rd.forward(request, response);
        } catch (Exception e){
            System.out.println(e.getMessage());
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nel recuperare gli articoli della wishlist: " + e.getMessage());
            } else {
                Utility.addError(request, e.getMessage());
            }

            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        } catch (APIException e) {
            throw new RuntimeException(e);
        }
    }
}
