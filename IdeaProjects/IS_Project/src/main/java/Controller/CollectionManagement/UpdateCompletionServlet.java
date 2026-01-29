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

@WebServlet("/Collection/UpdateCompletion")
public class UpdateCompletionServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameId = request.getParameter("gameId");
        boolean completed = Boolean.parseBoolean(request.getParameter("completed"));

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per aggiornare lo stato");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        UserManagement userManagement = new UserManagement();

        try {
            userManagement.setCompleted(loggedUser.getID(), gameId, completed);
            response.sendRedirect("CollectionView");
        } catch (Exception e){
            System.out.println(e.getMessage());
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nell'aggiornare lo stato del gioco.");
            } else {
                Utility.addError(request, e.getMessage());
            }

            RequestDispatcher rd = request.getRequestDispatcher("collection.jsp");
            rd.forward(request, response);
        }
    }
}

