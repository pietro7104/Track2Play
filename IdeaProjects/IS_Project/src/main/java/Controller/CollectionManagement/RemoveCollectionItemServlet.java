package Controller.CollectionManagement;
import Controller.Utility;
import Model.User;

import Model.UserManagement;
import Service.CollectionService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/Collection/Remove")
public class RemoveCollectionItemServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameId = request.getParameter("gameId");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("username");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per rimuovere giochi dalla collezione");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        UserManagement userManagement = new UserManagement();

        try {
            userManagement.removeGame(loggedUser.getID(), gameId);
            response.sendRedirect("Collection/View");
        } catch (SQLException e) {
            Utility.addError(request, "Errore nella rimozione del gioco dalla collezione");
            RequestDispatcher rd = request.getRequestDispatcher("collection.jsp");
            rd.forward(request, response);
        }
    }
}

