package Controller.GamePages;

import Controller.Utility;
import Model.APIControl.APIExceptions.APIException;
import Model.APIControl.APIImplementation;
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
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/OpenGamePage")
public class OpenGamePageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("itadid");
        System.out.println(id);
        Price price = (Price)request.getAttribute("price");
        request.setAttribute("price", price);
        APIInterface api = new APIImplementation();
        try {
            Game info =  api.GetGameInfoByIsThereAnyDealID(id);
            request.setAttribute("info", info);
        } catch (APIException e) {
            Utility.addError(request, "Errore nell'ottenimento delle info del gioco");
            RequestDispatcher rd = request.getRequestDispatcher("Home Page.jsp");
            rd.forward(request, response);
            return;
        }

        if (price == null) {
            try{
                ArrayList<String> ids = new ArrayList<>();
                ids.add(id);
                HashMap<String, Price> prices = api.GetGamesPrices(ids, api.GetShopIDs("IT"), "IT", false, 0, true);
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
