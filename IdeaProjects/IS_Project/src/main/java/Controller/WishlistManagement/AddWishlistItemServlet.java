package Controller.WishlistManagement;

import Controller.Utility;
import Model.WishlistItem;
import Model.WishlistDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/Wishlist/Add")
public class AddWishlistItemServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ottieni i dati dal form
        String name = request.getParameter("name");
        String username = request.getParameter("username");

        if (username == null) {
            Utility.addError(request, "Devi effettuare il login per aggiungere articoli alla wishlist");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        WishlistItem newItem = new WishlistItem();
        newItem.setName(name);
        newItem.setUsername(username);
        item.setAddedDate(new Date());

        WishlistDAO wishlistDAO = new WishlistDAO();

        try {
            wishlistDAO.addItemToWishlist(username, newItem);
            response.sendRedirect("Wishlist/View");  // Reindirizza alla pagina di visualizzazione della wishlist
        } catch (SQLException e) {
            Utility.addError(request, "Errore nell'aggiungere l'articolo alla wishlist");
            RequestDispatcher rd = request.getRequestDispatcher("Wishlist.jsp");
            rd.forward(request, response);
        }
    }
}
