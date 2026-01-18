package Controller.Search;

import Controller.Utility;
import Model.APIControl.APIExceptions.APIException;
import Model.APIControl.APIInterface;
import Model.APIControl.Price;
import Model.APIControl.Shop;
import Model.Game;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/Search")
public class SearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        APIInterface api = Utility.getAPI();
        ArrayList<Game> games;
        try {
            games = api.SearchByTitle(query, 100);
        }
        catch (APIException e) {
            Utility.addError(request, "Errore nella ricerca");
            RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);
            return;
        }

        ArrayList<String> ids = new ArrayList<>();
        for (Game game : games) {
            ids.add(game.getIsThereAnyDealID());
        }

        ArrayList<Shop> shops = null;
        try {
            shops = api.GetShops("IT");
        } catch (APIException e) {
            Utility.addError(request, "Errore nella ricerca");
            RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);
            return;
        }
        ArrayList<Integer> shopIDs = new ArrayList<>();
        for (Shop shop : shops) {
            System.out.println(shop.name);
            shopIDs.add(shop.id);
        }

        try {
            HashMap<String, Price> prices = api.GetGamesPrices(ids, shopIDs, "IT", false, 0, true);

            request.setAttribute("prices", prices);
            request.setAttribute("search_results", games);
            request.setAttribute("query", query);

        }
        catch (APIException e){
            Utility.addError(request, "Errore nella ricerca");
            RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);
        }

        RequestDispatcher rd = request.getRequestDispatcher("Search Result Page.jsp");
        rd.forward(request, response);
    }
}
