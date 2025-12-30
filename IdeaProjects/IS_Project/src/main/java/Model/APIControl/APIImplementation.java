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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

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

    HashMap<String, Shop> shopsHash = new HashMap<>();

    HashMap<String, HashMap<String, Shop>> shopCountryHash = new HashMap<>();

    private HttpResponse<String> GETRequest(String url){
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(timeUntilTimeout)).build();
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .join();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    public Shop GetShopByName(String shopName, String country, Duration maxTimeSinceLastUpdate){
        boolean called = false;
        HashMap<String, Shop> shopsInCountry = shopCountryHash.getOrDefault(country, null);
        if (shopsInCountry == null) {
            GetShops(country);
            shopsInCountry = shopCountryHash.getOrDefault(country, null);
            called = true;
        }
        Shop shop = shopsInCountry.getOrDefault(shopName, null);
        ZonedDateTime currentTime = ZonedDateTime.now();
        if ((shop == null && !called) || (currentTime.isBefore(shop.update.plus(maxTimeSinceLastUpdate)))) {
            GetShops(country);
            shopsInCountry = shopCountryHash.getOrDefault(country, null);
            shop = shopsInCountry.getOrDefault(shopName, null);
            called = true;
        }
        return shop;
    }

    public ArrayList<Game> SearchByTitle(String title, int maxResults) //maxResults 0-100
    {
        try (HttpClient client = HttpClient.newHttpClient()) {
            maxResults = Math.clamp(maxResults, 1, 100);
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
        String url = "https://api.isthereanydeal.com/games/info/v2?key=" + getKey() + "&id=" + isthereanydealGameID;
        HttpResponse<String> response = GETRequest(url);

        if (response == null || response.statusCode() != 200) {
            if (response != null) System.out.println(response.statusCode());
            return null;
        }
        JSONObject jsonObject = new JSONObject(response.body());
        String id = jsonObject.getString("id");
        int appid = jsonObject.getInt("appid");
        String slug = jsonObject.getString("slug");
        String title = jsonObject.getString("title");
        String type = jsonObject.getString("type");
        boolean mature = jsonObject.getBoolean("mature");
        boolean earlyAccess = jsonObject.getBoolean("earlyAccess");
        boolean achievements = jsonObject.getBoolean("achievements");
        boolean tradingCards = jsonObject.getBoolean("tradingCards");

        JSONArray tagsJSONArray = jsonObject.getJSONArray("tags");
        ArrayList<String> tags = new ArrayList<>();
        for (int i = 0; i < tagsJSONArray.length(); i++) {
            tags.add(tagsJSONArray.getString(i));
        }

        String releaseDateString = jsonObject.getString("releaseDate");
        LocalDate releaseDate = LocalDate.parse(releaseDateString);
        JSONArray developersJSONArray = jsonObject.getJSONArray("developers");
        ArrayList<Game.Developer> developers = new ArrayList<>();
        for (int i = 0; i < developersJSONArray.length(); i++) {
            JSONObject developer = developersJSONArray.getJSONObject(i);
            String developerName = developer.getString("name");
            int developerId = developer.getInt("id");
            developers.add(new Game.Developer(developerName, developerId));
        }

        JSONArray publishersJSONArray = jsonObject.getJSONArray("publishers");
        ArrayList<Game.Publisher> publishers = new ArrayList<>();
        for (int i = 0; i < publishersJSONArray.length(); i++) {
            JSONObject publisher = developersJSONArray.getJSONObject(i);
            String publisherName = publisher.getString("name");
            int publisherId = publisher.getInt("id");
            publishers.add(new Game.Publisher(publisherName, publisherId));
        }

        JSONArray reviewsJSONArray = jsonObject.getJSONArray("reviews");
        ArrayList<Game.Review> reviews = new ArrayList<>();
        for (int i = 0; i < reviewsJSONArray.length(); i++) {
            JSONObject review = reviewsJSONArray.getJSONObject(i);
            float score = review.getFloat("score");
            String source = review.getString("source");
            int count = review.getInt("count");
            String reviewUrl = review.getString("url");
            reviews.add(new Game.Review(score, source, count, reviewUrl));
        }

        JSONObject assetsJSON = jsonObject.getJSONObject("assets");
        HashMap<String, String> assets = new HashMap<>();
        for (String key : assetsJSON.keySet()) {
            String assetUrl = assetsJSON.getString(key);
            assets.put(key, assetUrl);
        }

        JSONObject stats = jsonObject.getJSONObject("stats");
        int rank = stats.getInt("rank");
        int waitlisted = stats.getInt("waitlisted");
        int collected = stats.getInt("collected");

        JSONObject players = jsonObject.getJSONObject("players");
        int recent = players.getInt("recent");
        int day = players.getInt("day");
        int week = players.getInt("week");
        int peak = players.getInt("peak");

        JSONObject urlsJSON = jsonObject.getJSONObject("urls");
        HashMap<String, String> urls = new HashMap<>();
        for(String key : urlsJSON.keySet()) {
            String gameUrl = urlsJSON.getString(key);
            urls.put(key, gameUrl);
        }

        return new Game(Integer.toString(appid), id, title, slug, type, mature, tags, developers, publishers, reviews, rank, waitlisted, collected, recent, day, week, peak, urls, assets, releaseDate, tradingCards, achievements, earlyAccess);
    }

    public Game GetGameInfoBySteamID(String steamID)
    {

        return null;
    }

    public HashMap<String, ArrayList<String>> GetGamesIDsOnShop(ArrayList<String> isThereAnyDealIDs, int shopID) //Ottiene gli ID di una lista di giochi su un negozio a scelta a partire dall'ID di IsThereAnyDeal, alcuni giochi possono avere più id (es. se sono in bundle esce anche l'id del bundle) per questo il risultato è un array di array
    {
        try (HttpClient client = HttpClient.newHttpClient()) {
            String url = "https://api.isthereanydeal.com/lookup/shop/" + shopID +"/id/v1?key=" + getKey();

            JSONArray requestBody = new JSONArray(isThereAnyDealIDs);
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create(url))
                    .timeout(Duration.ofSeconds(timeUntilTimeout))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .join();

            HashMap<String, ArrayList<String>> ids = new HashMap<>();
            JSONObject jsonObject = new JSONObject(response.body());
            System.out.println(jsonObject.toString());

            for (String itadid : isThereAnyDealIDs) {
                JSONArray jsonIDs = jsonObject.getJSONArray(itadid);

                if (jsonIDs != null) {
                    ArrayList<String> idList = new ArrayList<>();
                    for (int i = 0; i < jsonIDs.length(); i++) {
                        String id = jsonIDs.getString(i);
                        idList.add(id);
                    }
                    ids.put(itadid, idList);
                }
            }
            return ids;
        }
        //return null;
    }

    private ArrayList<Price> GetGamesPricesHelper(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers){
        try (HttpClient client = HttpClient.newHttpClient()) {
            String url = "https://api.isthereanydeal.com/games/prices/v3";
            url += "?key=" + getKey();
            url += "&country=" + country;
            url += "&capacity=" + capacity;
            url += "&vouchers=" + vouchers;
            String shops = "";
            int i = 0;
            for (Integer shopID : shopIDs) {
                i++;
                shops += shopID;
                if (i < shopIDs.size()) {
                    shops += ",";
                }
            }
            url += "&shops=" + shops;
            url += "&deals=" + onlyDeals;

            JSONArray requestBody = new JSONArray(isThereAnyDealIDs);

            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create(url))
                    .timeout(Duration.ofSeconds(timeUntilTimeout))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .join();

            JSONArray jsonArray = new JSONArray(response.body());
            ArrayList<Price> prices = new ArrayList<>();
            for (i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Price price = new Price();
            }

        }
        return null;
    }

    public ArrayList<Price> GetGamesPrices(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers)
    {

        return null;
    }

    public ArrayList<Shop> GetShops(String country){
        String url = "https://api.isthereanydeal.com/service/shops/v1?country=" + country;

        HttpResponse<String> response = GETRequest(url);

        if (response == null){
          System.out.println("Error");
          return null;
        }
        //System.out.println(response.body());
        JSONArray jsonArray = new JSONArray(response.body());
        ArrayList<Shop> shops = new ArrayList<>();

        if (!shopCountryHash.containsKey(country)) {
            shopCountryHash.put(country, new HashMap<>());
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String ITADupdateString = jsonObject.getString("update");
            ZonedDateTime ITADupdate = ZonedDateTime.parse(ITADupdateString);
            ZonedDateTime update = ZonedDateTime.now();
            Shop shop = new Shop(jsonObject.getInt("id"), jsonObject.getString("title"), jsonObject.getInt("deals"), jsonObject.getInt("games"), ITADupdate, update);

            shopsHash.put(shop.name, shopsHash.get(shop.name));
            shopCountryHash.get(country).put(shop.name, shop);

            shops.add(shop);
        }
        return shops;
    }
}
