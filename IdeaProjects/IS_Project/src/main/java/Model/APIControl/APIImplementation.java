package Model.APIControl;

import Model.Game;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

public class APIImplementation implements APIInterface {

    private long timeUntilTimeout = 10;

    //Nascodi dal github in qualche modo?
    private String key = "";

    private String getKey() {
        if (!key.isEmpty()) return key;
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader("..\\ITADkey.txt"));
            key = bufferedReader.readLine();
            return key;
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
        return null;
    }

    public ArrayList<Game> SearchByTitle(String title, int maxResults) //maxResults 0-100
    {
        try (HttpClient client = HttpClient.newHttpClient()) {
            String url = "https://api.isthereanydeal.com/games/search/v1?key=" + getKey() + "&title=" + title + "&results=" + maxResults;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(timeUntilTimeout)).build();
            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .join();

            //System.out.println(response.body());
            JSONArray jsonArray = new JSONArray(response.body());
            ArrayList<Game> games = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Game game = new Game(jsonObject.getString("id"), jsonObject.getString("title"), jsonObject.getString("type"), jsonObject.getBoolean("mature"));
                games.add(game);
            }
            return games;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public Game GetGameInfoByIsThereAnyDealID(String isthereanydealGameID)
    {
        return null;
    }

    public Game GetGameInfoBySteamID(String steamID)
    {

        return null;
    }

    public ArrayList<ArrayList<String>> GetGamesIDsOnShop(ArrayList<String> isThereAnyDealIDs, int shopID) //Ottiene gli ID di una lista di giochi su un negozio a scelta a partire dall'ID di IsThereAnyDeal, alcuni giochi possono avere più id (es. se sono in bundle esce anche l'id del bundle) per questo il risultato è un array di array
    {
        try (HttpClient client = HttpClient.newHttpClient()) {
            String url = "https://api.isthereanydeal.com/lookup/shop/" + shopID + "/id/v1?key=" + getKey();

            JSONArray requestBody = new JSONArray(isThereAnyDealIDs);
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create(url))
                    .timeout(Duration.ofSeconds(timeUntilTimeout))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .join();

            System.out.println(response.body());
        }
        return null;
    }
    public ArrayList<Price> GetGamesPrices(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers)
    {
        return null;
    }
}
