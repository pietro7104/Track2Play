package Controller.Search;

import Model.APIControl.APIImplementation;
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
import java.util.Hashtable;

@WebServlet("/Search")
public class SearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        APIInterface api = new APIImplementation();
        ArrayList<Game> games = api.SearchByTitle(query, 100);
        ArrayList<String> ids = new ArrayList<>();
        for (Game game : games) {
            ids.add(game.getIsThereAnyDealID());
        }
        ArrayList<Shop> shops = api.GetShops("IT");
        ArrayList<Integer> shopIDs = new ArrayList<>();
        for (Shop shop : shops) {
            shopIDs.add(shop.id);
        }
        HashMap<String, Price> prices = api.GetGamesPrices(ids, shopIDs, "IT", false, 0, true);

        System.out.println("seach results: " + games.size());
        System.out.println("prices: " + prices.keySet().size());
        request.setAttribute("prices", prices);
        request.setAttribute("search_results", games);
        request.setAttribute("query", query);

        RequestDispatcher rd = request.getRequestDispatcher("Search Result Page.jsp");
        rd.forward(request, response);
    }
}
