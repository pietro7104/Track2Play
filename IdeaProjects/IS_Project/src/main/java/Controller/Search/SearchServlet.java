package Controller.Search;

import Controller.HomePageManagement.OpenHomePageServlet;
import Controller.Utility;
import Model.APIControl.APIExceptions.APIException;
import Model.APIControl.APIInterface;
import Model.APIControl.Price;
import Model.APIControl.Shop;
import Model.Game;
import Model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@WebServlet("/Search")
public class SearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String countryCode;
        if (user == null)  countryCode = "IT";
        else countryCode = user.getCountryISO();

        String query = request.getParameter("query");
        if (query == null || query.isEmpty()) {
            Utility.addError(request, "Nessun termine di ricerca");
            OpenHomePageServlet openHomePageServlet = new OpenHomePageServlet();
            openHomePageServlet.doGet(request, response);
            return;
        }
        APIInterface api = Utility.getAPI();
        ArrayList<Game> games;
        try {
            games = api.SearchByTitle(query, 100);
        }
        catch (APIException e) {

            Utility.addError(request, "Errore nella ricerca");
            OpenHomePageServlet openHomePageServlet = new OpenHomePageServlet();
            openHomePageServlet.doGet(request, response);
            /*RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);*/
            return;
        }

        ArrayList<String> ids = new ArrayList<>();
        for (Game game : games) {
            ids.add(game.getIsThereAnyDealID());
        }

        ArrayList<Shop> shops = null;
        try {
            shops = api.GetShops(countryCode);
        } catch (APIException e) {
            Utility.addError(request, "Errore nella ricerca");
            OpenHomePageServlet openHomePageServlet = new OpenHomePageServlet();
            openHomePageServlet.doGet(request, response);
            /*RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);*/
            return;
        }
        ArrayList<Integer> shopIDs = new ArrayList<>();
        for (Shop shop : shops) {
            shopIDs.add(shop.id);
        }

        try {
            LinkedHashMap<String, Price> prices = api.GetGamesPrices(ids, shopIDs, countryCode, false, 0, true);

            request.setAttribute("prices", prices);
            request.setAttribute("search_results", games);
            request.setAttribute("query", query);

        }
        catch (APIException e){
            Utility.addError(request, "Errore nella ricerca");
            OpenHomePageServlet openHomePageServlet = new OpenHomePageServlet();
            openHomePageServlet.doGet(request, response);
            /*RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);*/
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher("Search Result Page.jsp");
        rd.forward(request, response);
        return;
    }
}
