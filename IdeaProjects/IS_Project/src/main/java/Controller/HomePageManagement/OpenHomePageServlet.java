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
import java.util.LinkedHashMap;

@WebServlet("/OpenHomePage")
public class OpenHomePageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        APIInterface api = Utility.getAPI();
        try {
            LinkedHashMap<Game, Price> gamePriceHashmap = api.GetDeals("IT", 0, 50, "-hot", false, false, api.GetShopIDs("IT"), "");
            ArrayList<Game> top;
            ArrayList<Game> games = new ArrayList<>();
            LinkedHashMap<String, Price> prices = new LinkedHashMap<>();
            for (Game key : gamePriceHashmap.keySet()) {
                games.add(key);
                prices.put(key.getIsThereAnyDealID(), gamePriceHashmap.get(key));
            }

            ArrayList<Game> topGames = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                topGames.add(games.get(i));
                games.remove(games.get(i));
            }

            request.setAttribute("games", games);
            request.setAttribute("topGames", topGames);
            request.setAttribute("prices", prices);
        } catch (APIException e) {
            Utility.addError(request, "Errore nel caricamento della home page");
            System.out.println(e.getMessage());
        }
        RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
        rd.forward(request, response);
    }
}
