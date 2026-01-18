package Controller.WishlistManagement;

import Controller.Utility;
import Model.User;
import Model.WishlistItemDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/Wishlist/Remove")
public class RemoveWishlistItemServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ottieni l'ID del gioco nell'item da rimuovere
        String gameId = request.getParameter("gameItemId");

        User loggedUser = ((User) request.getSession().getAttribute("user"));

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per rimuovere articoli dalla wishlist");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        WishlistItemDAO wishlistItemDAO = new WishlistItemDAO();

        try {
            wishlistItemDAO.removeItemFromWishlistByGameId(loggedUser.getID(), gameId);
            response.sendRedirect("Wishlist/View");
        } catch (SQLException e) {
            Utility.addError(request, "Errore nella rimozione dell'articolo dalla wishlist");
            RequestDispatcher rd = request.getRequestDispatcher("Wishlist.jsp");
            rd.forward(request, response);
        }
    }
}
