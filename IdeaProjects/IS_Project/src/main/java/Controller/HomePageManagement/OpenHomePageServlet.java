package Controller.HomePageManagement;


import Controller.Utility;
import Model.APIControl.APIExceptions.APIException;
import Model.APIControl.APIInterface;
import Model.APIControl.Price;
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

@WebServlet("/OpenHomePage")
public class OpenHomePageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        APIInterface api = Utility.getAPI();
        try {
            HashMap<Game, Price> gamePriceHashmap = api.GetDeals("IT", 0, 200, "-cut", true, true, api.GetShopIDs("IT"), "");
            ArrayList<Game> games = new ArrayList<>();
            HashMap<String, Price> prices = new HashMap<>();
            for (Game key : gamePriceHashmap.keySet()) {
                games.add(key);
                prices.put(key.getIsThereAnyDealID(), gamePriceHashmap.get(key));
            }
            request.setAttribute("games", games);
            request.setAttribute("prices", prices);
        } catch (APIException e) {
            Utility.addError(request, "Errore nel caricamento della home page");
        }
        RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
        rd.forward(request, response);
    }
}
