package Controller.CollectionManagement;

import Controller.Utility;
import Model.CollectionItem;
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
import java.util.List;

@WebServlet("/Collection/View")
public class ViewCollectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            Utility.addError(request, "Devi effettuare il login per visualizzare la collezione");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        UserManagement userManagement = new UserManagement();

        try {
            List<CollectionItem> collection =
                    userManagement.getUserCollection(loggedUser.getID());


            request.setAttribute("collection", collection);
            RequestDispatcher rd =
                    request.getRequestDispatcher("Collection Page.jsp");
            rd.forward(request, response);

        }catch (Exception e){
            System.out.println(e.getMessage());
            if(e.getClass() == SQLException.class){
                Utility.addError(request, "Errore nel caricamento della collezione.");
            } else {
                Utility.addError(request, e.getMessage());
            }

            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }
}

