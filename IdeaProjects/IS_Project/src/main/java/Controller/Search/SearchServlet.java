package Controller.Search;

import Model.APIControl.APIImplementation;
import Model.APIControl.APIInterface;
import Model.Game;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/Search")
public class SearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        APIInterface api = new APIImplementation();
        ArrayList<Game> games = api.SearchByTitle(query, 100);
        request.setAttribute("search_results", games);
        request.setAttribute("query", query);

        RequestDispatcher rd = request.getRequestDispatcher("Search Result Page.jsp");
        rd.forward(request, response);
    }
}
