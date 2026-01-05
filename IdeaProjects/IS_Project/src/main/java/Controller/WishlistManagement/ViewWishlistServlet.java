package Controller.WishlistManagement;

import Controller.Utility;
import Model.WishlistDAO;
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
import java.util.List;

@WebServlet("/Wishlist/View")
public class ViewWishlistServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");

        if (username == null) {
            Utility.addError(request, "Devi effettuare il login per visualizzare la tua wishlist");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        WishlistDAO wishlistDAO = new WishlistDAO();

        try {

            List<WishlistItem> wishlistItems = wishlistDAO.getWishlistItems(username);
            request.setAttribute("wishlistItems", wishlistItems);
            RequestDispatcher rd = request.getRequestDispatcher("wishlist.jsp");
            rd.forward(request, response);
        } catch (SQLException e) {
            Utility.addError(request, "Errore nel recuperare gli articoli della wishlist: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }
}
