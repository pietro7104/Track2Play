package Controller.APITest;

import Controller.Utility;
import Model.APIControl.*;
import Model.APIControl.APIExceptions.APIException;
import Model.Game;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/APITest")
public class APITestServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        APIInterface api = Utility.getAPI();
        /*ArrayList<String> list = new ArrayList<>();
        list.add("018d937f-42c6-70a5-a29b-9d8c2e0f7b84");
        list.add("018d937f-6128-7151-8d2e-b4a9ad2e1ce8");
        HashMap<String, ArrayList<String>> h = api.GetGamesIDsOnShop(list, 61);
        for (String key : h.keySet()){
            ArrayList<String> values = h.get(key);
            System.out.println(key);
            for (String value : values){ System.out.println(value); }
        }

        Game g = api.GetGameInfoBySteamID("2513280");
        System.out.println("title: " + g.getTitle() + " appid: " + g.getIsThereAnyDealID());
        System.out.println("tags: ");
        for (String tag : g.getTags()) {
            System.out.println(tag);
        }
        System.out.println("publishers: ");
        for (Game.Publisher publisher: g.getPublishers()){
            System.out.println("name: " + publisher.name);
        }

        System.out.println("urls: ");
        for (String key : g.getUrls().keySet()){
            System.out.println(key + ": " + g.getUrls().get(key));
        }

        System.out.println("reviews: ");
        for (Game.Review review : g.getReviews()){
            System.out.println(review.source + ": " + review.score + " count: " + review.count + " url: " + review.url);
        }
        //api.SearchByTitle("Sonic", 5);*/

        /*ArrayList<String> ids = new ArrayList<>();
        ids.add(api.GetGameInfoBySteamID("2513280").getIsThereAnyDealID());
        ids.add("018d937f-42c6-70a5-a29b-9d8c2e0f7b84");
        ids.add("018d937f-6128-7151-8d2e-b4a9ad2e1ce8");
        System.out.println(api.GetGameInfoByIsThereAnyDealID("018d937f-42c6-70a5-a29b-9d8c2e0f7b84").getTitle());
        ArrayList<Shop> shops = api.GetShops("IT");
        ArrayList<Integer> shopIDs = new ArrayList<>();
        for (Shop shop : shops) {
            shopIDs.add(shop.id);
        }

        System.out.println(shopIDs.size());

        HashMap<String,Price> prices = api.GetGamesPrices(ids, shopIDs, "IT", false, 0, false);
        System.out.println("PRICES: " + prices.keySet().size());
        for(String key : prices.keySet()){
            System.out.println("id: " + key);
            Price price = prices.get(key);
            for (Deal deal : price.deals){
                System.out.println("shopName: " + deal.shopName + " price: " + deal.dealPrice.amount + " " + deal.dealPrice.currency + " cut: " + deal.cut);
            }
        }*/

        try{
            ArrayList<Game> games = api.SearchByTitle("Sonic", 100);

            for (Game game : games) {
                System.out.println(game.getTitle());
                HashMap<String, String> assets = game.getAssets();
                for (String asset : assets.keySet()){
                    System.out.println(asset + ": " + assets.get(asset));
                }
            }
        }
        catch (APIException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}