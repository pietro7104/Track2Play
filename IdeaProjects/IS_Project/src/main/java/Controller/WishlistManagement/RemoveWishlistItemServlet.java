package Controller.WishlistManagement;

import Controller.Utility;
import Model.UserManagement;
import Service.WishlistService;
import Model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/WishlistRemove")
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

        UserManagement userManagement = new UserManagement();

        try {
            userManagement.removeFromWishlistById(loggedUser.getID(), gameId);
            response.sendRedirect("Wishlist/View");
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nella rimozione dell'articolo dalla wishlist.");
            } else {
                Utility.addError(request, e.getMessage());
            }

            RequestDispatcher rd = request.getRequestDispatcher("Wishlist.jsp");
            rd.forward(request, response);
        }
    }
}
