package Controller.GamePageManagement;

import Controller.HomePageManagement.OpenHomePageServlet;
import Controller.Utility;
import Model.APIControl.APIExceptions.APIException;
import Model.APIControl.APIInterface;
import Model.APIControl.Price;
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

@WebServlet("/OpenGamePage")
public class OpenGamePageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String countryCode;
        if (user == null)  countryCode = "IT";
        else countryCode = user.getCountryISO();

        String id = request.getParameter("itadid");
        Price price = (Price)request.getAttribute("price");
        request.setAttribute("price", price);
        APIInterface api = Utility.getAPI();
        try {
            Game info =  api.GetGameInfoByIsThereAnyDealID(id);
            request.setAttribute("info", info);
        } catch (APIException e) {
            Utility.addError(request, "Errore nell'ottenimento delle info del gioco");
            OpenHomePageServlet openHomePageServlet = new OpenHomePageServlet();
            openHomePageServlet.doGet(request, response);
            return;
            /*RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);*/
        }

        if (price == null) {
            try{
                ArrayList<String> ids = new ArrayList<>();
                ids.add(id);
                HashMap<String, Price> prices = api.GetGamesPrices(ids, api.GetShopIDs(countryCode), countryCode, false, 0, true);
                price = prices.getOrDefault(id, null);
                if (price == null) {
                    Utility.addError(request, "Errore nell'ottenimento dei prezzi del gioco");
                    RequestDispatcher rd = request.getRequestDispatcher("Game Page.jsp");
                    rd.forward(request, response);
                    return;
                }
                request.setAttribute("price", price);
            }
            catch (APIException e){
                Utility.addError(request, "Errore nell'ottenimento dei prezzi del gioco");
                RequestDispatcher rd = request.getRequestDispatcher("Game Page.jsp");
                rd.forward(request, response);
                return;
            }
        }


        RequestDispatcher rd = request.getRequestDispatcher("Game Page.jsp");
        rd.forward(request, response);

    }
}