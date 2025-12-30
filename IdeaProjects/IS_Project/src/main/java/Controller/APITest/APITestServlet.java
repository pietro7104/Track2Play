package Controller.APITest;

import Model.APIControl.APIImplementation;
import Model.APIControl.APIInterface;
import Model.APIControl.Shop;
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
        APIInterface api = new APIImplementation();
        /*ArrayList<String> list = new ArrayList<>();
        list.add("018d937f-42c6-70a5-a29b-9d8c2e0f7b84");
        list.add("018d937f-6128-7151-8d2e-b4a9ad2e1ce8");
        HashMap<String, ArrayList<String>> h = api.GetGamesIDsOnShop(list, 61);
        for (String key : h.keySet()){
            ArrayList<String> values = h.get(key);
            System.out.println(key);
            for (String value : values){ System.out.println(value); }
        }
        //api.SearchByTitle("Sonic", 5);*/

        Game g = api.GetGameInfoByIsThereAnyDealID("018d937f-07fc-72ed-8517-d8e24cb1eb22");
        System.out.println("title: " + g.getTitle() + " appid: " + g.getSteamID());
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
    }
}
