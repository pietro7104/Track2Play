package Model.APIControl;

import Model.Game;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Array;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
            title = title.replace(" ", "_");
            String url = "https://api.isthereanydeal.com/games/search/v1?key=" + getKey() + "&title=" + title + "&results=" + maxResults;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(timeUntilTimeout)).build();
            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .join();


            JSONArray jsonArray = new JSONArray(response.body());
            ArrayList<Game> games = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String gameTitle;
                try{
                    gameTitle = jsonObject.getString("title");
                }
                catch (JSONException e){
                    gameTitle = "";
                }

                String type;
                try{
                    type = jsonObject.getString("type");
                }
                catch (JSONException e){
                    type = "";
                }
                boolean mature;
                try{
                    mature = jsonObject.getBoolean("mature");
                }
                catch (JSONException e){
                    mature = false;
                }
                JSONObject assetsJSON = jsonObject.getJSONObject("assets");
                HashMap<String, String> assets = new HashMap<>();
                if (assetsJSON != null){
                    for (String key : assetsJSON.keySet()) {
                        String assetUrl = assetsJSON.getString(key);
                        if (assetUrl != null) assets.put(key, assetUrl);
                    }
                }
                Game game = new Game(id, gameTitle, type, mature, assets);
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
        String type = jsonObject.getString("type") != null ? jsonObject.getString("type") : "";
        boolean mature = jsonObject.getBoolean("mature");
        boolean earlyAccess = jsonObject.getBoolean("earlyAccess");
        boolean achievements = jsonObject.getBoolean("achievements");
        boolean tradingCards = jsonObject.getBoolean("tradingCards");

        JSONArray tagsJSONArray = jsonObject.getJSONArray("tags");
        ArrayList<String> tags = new ArrayList<>();
        if (tagsJSONArray != null){
            for (int i = 0; i < tagsJSONArray.length(); i++) {
                tags.add(tagsJSONArray.getString(i));
            }
        }


        String releaseDateString = jsonObject.getString("releaseDate");
        LocalDate releaseDate = LocalDate.parse(releaseDateString);
        JSONArray developersJSONArray = jsonObject.getJSONArray("developers");
        ArrayList<Game.Developer> developers = new ArrayList<>();
        if (developersJSONArray != null){
            for (int i = 0; i < developersJSONArray.length(); i++) {
                JSONObject developer = developersJSONArray.getJSONObject(i);
                if (developer != null){
                    try{
                        String developerName = developer.getString("name");
                        int developerId = developer.getInt("id");
                        developers.add(new Game.Developer(developerName, developerId));
                    }
                    catch (Exception e){
                        continue;
                    }

                }
            }
        }


        JSONArray publishersJSONArray = jsonObject.getJSONArray("publishers");
        ArrayList<Game.Publisher> publishers = new ArrayList<>();
        if (publishersJSONArray != null){
            for (int i = 0; i < publishersJSONArray.length(); i++) {
                JSONObject publisher = publishersJSONArray.getJSONObject(i);
                if (publisher != null){
                    try{
                        String publisherName = publisher.getString("name");
                        int publisherId = publisher.getInt("id");
                        publishers.add(new Game.Publisher(publisherName, publisherId));
                    }
                    catch (Exception e){
                        continue;
                    }

                }

            }
        }


        JSONArray reviewsJSONArray = jsonObject.getJSONArray("reviews");
        ArrayList<Game.Review> reviews = new ArrayList<>();
        if (reviewsJSONArray != null){
            for (int i = 0; i < reviewsJSONArray.length(); i++) {
                JSONObject review = reviewsJSONArray.getJSONObject(i);
                if (review != null){
                    try{
                        float score = review.getFloat("score");
                        String source = review.getString("source");
                        int count = review.getInt("count");
                        String reviewUrl = review.getString("url");
                        reviews.add(new Game.Review(score, source, count, reviewUrl));
                    }
                    catch (Exception e){
                        continue;
                    }

                }
            }
        }


        JSONObject assetsJSON = jsonObject.getJSONObject("assets");
        HashMap<String, String> assets = new HashMap<>();
        if (assetsJSON != null){
            for (String key : assetsJSON.keySet()) {
                String assetUrl = assetsJSON.getString(key);
                if (assetUrl != null) assets.put(key, assetUrl);
            }
        }


        JSONObject stats = jsonObject.getJSONObject("stats");
        int rank = -1;
        int waitlisted = -1;
        int collected = -1;
        if (stats != null){
            rank = stats.getInt("rank");
            waitlisted = stats.getInt("waitlisted");
            collected = stats.getInt("collected");
        }


        JSONObject players = jsonObject.getJSONObject("players");
        int recent = -1;
        int day = -1;
        int week = -1;
        int peak = -1;
        if (players != null){
            recent = players.getInt("recent");
            day = players.getInt("day");
            week = players.getInt("week");
            peak = players.getInt("peak");
        }


        JSONObject urlsJSON = jsonObject.getJSONObject("urls");
        HashMap<String, String> urls = new HashMap<>();
        if (urlsJSON != null){
            for(String key : urlsJSON.keySet()) {
                String gameUrl = urlsJSON.getString(key);
                if (gameUrl != null) urls.put(key, gameUrl);
            }
        }

        return new Game(Integer.toString(appid), id, title, slug, type, mature, tags, developers, publishers, reviews, rank, waitlisted, collected, recent, day, week, peak, urls, assets, releaseDate, tradingCards, achievements, earlyAccess);
    }

    public Game GetGameInfoBySteamID(String steamID)
    {
        HttpResponse<String> response = GETRequest("https://api.isthereanydeal.com/games/lookup/v1?key=" + getKey() + "&appid=" + steamID);
        if (response == null || response.statusCode() != 200) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(response.body());
        if (jsonObject.getBoolean("found")){
            JSONObject gameInfo = jsonObject.getJSONObject("game");
            String itadID = gameInfo.getString("id");

            return GetGameInfoByIsThereAnyDealID(itadID);
        }
        else return null;
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
    }

    private HashMap<String, Price> GetGamesPricesHelper(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers){
        try (HttpClient client = HttpClient.newHttpClient()) {
            String url = "https://api.isthereanydeal.com/games/prices/v3";
            url += "?key=" + getKey();
            url += "&country=" + country;
            url += "&capacity=" + capacity;
            url += "&vouchers=" + vouchers;
            StringBuilder shops = new StringBuilder();
            int i = 0;
            for (Integer shopID : shopIDs) {
                i++;
                shops.append(shopID);
                if (i < shopIDs.size()) {
                    shops.append(",");
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

            //System.out.println(response.body());

            JSONArray priceJSONArray = new JSONArray(response.body());
            HashMap<String, Price> prices = new HashMap<>();
            for (i = 0; i < priceJSONArray.length(); i++) {
                JSONObject jsonObject = priceJSONArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                JSONObject history = jsonObject.getJSONObject("historyLow");
                Cost allTimeLow = null;
                Cost threeMonthLow = null;
                Cost oneYearLow = null;
                if (history != null) {
                    JSONObject all = history.getJSONObject("all");
                    if (all != null) {
                        allTimeLow = new Cost(all.getFloat("amount"), all.getString("currency"));
                    }
                    JSONObject y1 = history.getJSONObject("y1");
                    if (y1 != null) {
                        oneYearLow = new Cost(y1.getFloat("amount"), y1.getString("currency"));
                    }
                    JSONObject m3 = history.getJSONObject("m3");
                    if (m3 != null) {
                        threeMonthLow = new Cost(m3.getFloat("amount"), m3.getString("currency"));
                    }
                }

                JSONArray dealsJSON = jsonObject.getJSONArray("deals");
                ArrayList<Deal> deals = new ArrayList<>();
                for (int j = 0; j < dealsJSON.length(); j++) {
                    JSONObject deal = dealsJSON.getJSONObject(j);
                    if (deal != null) {
                        JSONObject shop = deal.getJSONObject("shop");
                        int shopID = -1;
                        String shopName = "";
                        if (shop != null){
                            shopID = shop.getInt("id");
                            shopName = shop.getString("name");
                        }
                        JSONObject price = deal.getJSONObject("price");
                        float amount = -1;
                        String currency = "";
                        if (price != null){
                            amount = price.getFloat("amount");
                            currency = price.getString("currency");
                        }
                        JSONObject regularPrice = deal.getJSONObject("regular");
                        float regularAmount = -1;
                        String regularCurrency = "";
                        if (regularPrice != null){
                            regularAmount = regularPrice.getFloat("amount");
                            regularCurrency = regularPrice.getString("currency");
                        }
                        float cut = deal.getFloat("cut");
                        String voucher;
                        try{
                            voucher = deal.getString("voucher") != null ? deal.getString("voucher") : ""; //non so che cos'è
                        }
                        catch (Exception e){
                            voucher = "";
                        }

                        JSONObject storeLow = deal.getJSONObject("storeLow");
                        float storeLowAmount = -1;
                        String storeLowCurrency = "";
                        if (storeLow != null){
                            storeLowAmount = storeLow.getFloat("amount");
                            storeLowCurrency = storeLow.getString("currency");
                        }

                        String flag;
                        try{
                            flag = deal.getString("flag") != null ? deal.getString("flag") : "";
                        }
                        catch (Exception e){
                            flag = "";
                        }

                        JSONArray drm = deal.getJSONArray("drm");
                        ArrayList<DRM> drms = new ArrayList<>();
                        if (drm != null){
                            for (int k = 0; k < drm.length(); k++) {
                                JSONObject d = drm.getJSONObject(k);
                                if (d != null){
                                    int drmId = d.getInt("id");
                                    String drmName = d.getString("name");
                                    drms.add(new DRM(drmId, drmName));
                                }
                            }
                        }
                        JSONArray platformsJSON = deal.getJSONArray("platforms");
                        ArrayList<Platform> platforms = new ArrayList<>();
                        if (platformsJSON != null){
                            for (int q = 0; q < platformsJSON.length(); q++) {
                                JSONObject platform = platformsJSON.getJSONObject(q);
                                if (platform != null) {
                                    Platform p = new Platform(platform.getInt("id"), platform.getString("name"));
                                }
                            }
                        }
                        String timestampString = deal.getString("timestamp");
                        ZonedDateTime timestamp = ZonedDateTime.parse(timestampString);
                        String expiryString = "";
                        try{
                            expiryString = deal.getString("expiry") != null ? deal.getString("expiry") : "";
                        }
                        catch (Exception e){
                            expiryString = "";
                        }

                        ZonedDateTime expiry = null;
                        if (!expiryString.isEmpty()) expiry = ZonedDateTime.parse(expiryString);
                        String gameUrl = deal.getString("url");
                        
                        Deal d = new Deal(shopID, shopName, new Cost(amount, currency), new Cost(regularAmount, regularCurrency), cut, voucher, new Cost(storeLowAmount, storeLowCurrency), flag, drms, platforms, timestamp, expiry, gameUrl);
                        deals.add(d);
                    }
                }
                Price price = new Price(id, allTimeLow, threeMonthLow, oneYearLow, deals);

                prices.put(id, price);
            }
            return prices;
        }
        catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return null;
        }
    }

    public HashMap<String, Price> GetGamesPrices(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers)
    {
        int chunk = 200;
        ArrayList<HashMap<String, Price>> hashMaps = new ArrayList<>();
        for(int i=0; i < isThereAnyDealIDs.size(); i+=chunk){
            ArrayList<String> subArray = new ArrayList<String>(isThereAnyDealIDs.subList(i, Math.min(isThereAnyDealIDs.size(), i+chunk)));
            HashMap<String, Price> result = GetGamesPricesHelper(subArray, shopIDs, country, onlyDeals, capacity, vouchers);

            if (result != null){
                hashMaps.add(result);
            }
        }
        HashMap<String, Price> prices = new HashMap<>();
        for (HashMap<String, Price> hashMap : hashMaps){
            prices.putAll(hashMap);
        }
        return prices;
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
