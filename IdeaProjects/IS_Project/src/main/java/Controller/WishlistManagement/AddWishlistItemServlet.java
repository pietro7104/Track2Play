package Controller.WishlistManagement;

import Controller.Utility;
import Model.User;
import Model.WishlistItem;
import Model.WishlistItemDAO;
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


@WebServlet("/Wishlist/Add")
public class AddWishlistItemServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ottieni l'id del gioco dal form
        String gameId = request.getParameter("gameId");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("username");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per aggiungere articoli alla wishlist");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        WishlistItemDAO wishlistItemDAO = new WishlistItemDAO();

        try {
            wishlistItemDAO.addItemToWishlist(loggedUser.getID(), gameId, new Date());
            response.sendRedirect("Wishlist/View");  // Reindirizza alla pagina di visualizzazione della wishlist
        } catch (SQLException e) {
            Utility.addError(request, "Errore nell'aggiungere l'articolo alla wishlist");
            RequestDispatcher rd = request.getRequestDispatcher("Wishlist.jsp");
            rd.forward(request, response);
        }
    }
}
