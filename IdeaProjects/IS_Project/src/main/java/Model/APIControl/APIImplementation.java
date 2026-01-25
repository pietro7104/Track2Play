package Model.APIControl;

import Model.APIControl.APIExceptions.APIException;
import Model.Game;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class APIImplementation implements APIInterface {

    private final long timeUntilTimeout = 10;

    private final Duration shopsTimeUntilUpdate = Duration.ofDays(1);

    private String key = "";

    private String getKey() throws FileNotFoundException, IOException {
        if (!key.isEmpty()) return key;

        String rootPath = System.getProperty("user.dir");;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(rootPath + "\\..\\ITADkey.txt"));
        key = bufferedReader.readLine();
        return key;
    }

    static HashMap<String, Shop> shopsHash = new HashMap<>();

    static HashMap<String, HashMap<String, Shop>> shopCountryHash = new HashMap<>();

    static LinkedHashMap<Game, Price> deals = new LinkedHashMap<>();
    static ZonedDateTime soonestExpiry;

    private HttpResponse<String> GETRequest(String url) throws APIException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(timeUntilTimeout)).build();
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .join();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new APIException(e.getMessage());
        }
    }


    public Shop GetShopByName(String shopName, String country, Duration maxTimeSinceLastUpdate) throws APIException {
        if (maxTimeSinceLastUpdate == null) maxTimeSinceLastUpdate = shopsTimeUntilUpdate;
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

    public ArrayList<Game> SearchByTitle(String title, int maxResults) throws APIException //maxResults 0-100
    {
        if (title.isEmpty()){
            return new ArrayList<Game>();
        }
        try (HttpClient client = HttpClient.newHttpClient()) {
            maxResults = Math.clamp(maxResults, 1, 100);
            title = title.replace(" ", "_");
            String url = "";
            try{
                url = "https://api.isthereanydeal.com/games/search/v1?key=" + getKey() + "&title=" + title + "&results=" + maxResults;
            }catch (Exception e){
                throw new APIException("Couldn't get key");
            }

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

                HashMap<String, String> assets = new HashMap<>();
                try{
                    JSONObject assetsJSON = jsonObject.getJSONObject("assets");
                    if (assetsJSON != null){
                        for (String key : assetsJSON.keySet()) {
                            String assetUrl = assetsJSON.getString(key);
                            if (assetUrl != null) assets.put(key, assetUrl);
                        }
                    }
                }catch (JSONException _){}

                Game game = new Game(id, gameTitle, type, mature, assets);
                games.add(game);
            }
            return games;
        }
        catch (Exception e) {
            throw new APIException(e.getMessage());
        }
    }

    public Game GetGameInfoByIsThereAnyDealID(String isthereanydealGameID) throws APIException {
        String url = null;
        try {
            url = "https://api.isthereanydeal.com/games/info/v2?key=" + getKey() + "&id=" + isthereanydealGameID;
        } catch (IOException e) {
            throw new APIException("Couldn't get key");
        }
        HttpResponse<String> response = GETRequest(url);
        if (response == null || response.statusCode() != 200) {
            if (response != null) System.out.println(response.statusCode());
            throw new APIException("Errore nella risposta dell'API");
        }
        JSONObject jsonObject = new JSONObject(response.body());
        String id = jsonObject.getString("id");
        int appid = -1;
        try{
            appid = jsonObject.getInt("appid");
        }catch (Exception _){}
        String slug = jsonObject.getString("slug");
        String title = jsonObject.getString("title");
        String type = "";
        try{
            type = jsonObject.getString("type");
        }catch (JSONException _){};
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


        String releaseDateString = "";
        LocalDate releaseDate = null;
        try {
            releaseDateString = jsonObject.getString("releaseDate");
            releaseDate = LocalDate.parse(releaseDateString);
        }catch (JSONException _){};


        ArrayList<Developer> developers = new ArrayList<>();
        try{
            JSONArray developersJSONArray = jsonObject.getJSONArray("developers");
            if (developersJSONArray != null){
                for (int i = 0; i < developersJSONArray.length(); i++) {
                    JSONObject developer = developersJSONArray.getJSONObject(i);
                    if (developer != null){
                        try{
                            String developerName = developer.getString("name");
                            int developerId = developer.getInt("id");
                            developers.add(new Developer(developerName, developerId));
                        }
                        catch (Exception e){
                            continue;
                        }

                    }
                }
            }
        }catch (JSONException _){};



        ArrayList<Publisher> publishers = new ArrayList<>();
        try{
            JSONArray publishersJSONArray = jsonObject.getJSONArray("publishers");
            if (publishersJSONArray != null){
                for (int i = 0; i < publishersJSONArray.length(); i++) {
                    JSONObject publisher = publishersJSONArray.getJSONObject(i);
                    if (publisher != null){
                        try{
                            String publisherName = publisher.getString("name");
                            int publisherId = publisher.getInt("id");
                            publishers.add(new Publisher(publisherName, publisherId));
                        }
                        catch (Exception e){
                            continue;
                        }

                    }

                }
            }
        }catch (JSONException _){};




        ArrayList<Review> reviews = new ArrayList<>();
        try{
            JSONArray reviewsJSONArray = jsonObject.getJSONArray("reviews");
            if (reviewsJSONArray != null){
                for (int i = 0; i < reviewsJSONArray.length(); i++) {
                    JSONObject review = reviewsJSONArray.getJSONObject(i);
                    if (review != null){
                        try{
                            float score = review.getFloat("score");
                            String source = review.getString("source");
                            int count = review.getInt("count");
                            String reviewUrl = review.getString("url");
                            reviews.add(new Review(score, source, count, reviewUrl));
                        }
                        catch (Exception e){
                            continue;
                        }

                    }
                }
            }
        }catch (JSONException _){};



        HashMap<String, String> assets = new HashMap<>();
        try{
            JSONObject assetsJSON = jsonObject.getJSONObject("assets");
            if (assetsJSON != null){
                for (String key : assetsJSON.keySet()) {
                    String assetUrl = assetsJSON.getString(key);
                    if (assetUrl != null) assets.put(key, assetUrl);
                }
            }
        }catch(JSONException _){};




        int rank = -1;
        int waitlisted = -1;
        int collected = -1;
        try{
            JSONObject stats = jsonObject.getJSONObject("stats");
            rank = stats.getInt("rank");
            waitlisted = stats.getInt("waitlisted");
            collected = stats.getInt("collected");
        }catch(JSONException _){};



        int recent = -1;
        int day = -1;
        int week = -1;
        int peak = -1;
        try{
            JSONObject players = jsonObject.getJSONObject("players");
            recent = players.getInt("recent");
            day = players.getInt("day");
            week = players.getInt("week");
            peak = players.getInt("peak");
        }catch(JSONException _){};



        HashMap<String, String> urls = new HashMap<>();
        try{
            JSONObject urlsJSON = jsonObject.getJSONObject("urls");
            for(String key : urlsJSON.keySet()) {
                String gameUrl = urlsJSON.getString(key);
                if (gameUrl != null) urls.put(key, gameUrl);
            }
        }catch(JSONException _){};

        return new Game(Integer.toString(appid), id, title, slug, type, mature, tags, developers, publishers, reviews, rank, waitlisted, collected, recent, day, week, peak, urls, assets, releaseDate, tradingCards, achievements, earlyAccess);
    }

    public Game GetGameInfoBySteamID(String steamID) throws APIException {
        HttpResponse<String> response = null;
        try {
            response = GETRequest("https://api.isthereanydeal.com/games/lookup/v1?key=" + getKey() + "&appid=" + steamID);
        } catch (IOException e) {
            throw new APIException("Couldn't get key");
        }
        if (response == null || response.statusCode() != 200) {
            throw new APIException("Error in API response");
        }
        JSONObject jsonObject = new JSONObject(response.body());
        if (jsonObject.getBoolean("found")){
            JSONObject gameInfo = jsonObject.getJSONObject("game");
            String itadID = gameInfo.getString("id");

            return GetGameInfoByIsThereAnyDealID(itadID);
        }
        else return null;
    }

    public LinkedHashMap<String, ArrayList<String>> GetGamesIDsOnShop(ArrayList<String> isThereAnyDealIDs, int shopID) throws APIException //Ottiene gli ID di una lista di giochi su un negozio a scelta a partire dall'ID di IsThereAnyDeal, alcuni giochi possono avere più id (es. se sono in bundle esce anche l'id del bundle) per questo il risultato è un array di array
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

            LinkedHashMap<String, ArrayList<String>> ids = new LinkedHashMap<>();
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
        catch (Exception e){
            throw new APIException(e.getMessage());
        }
    }

    private LinkedHashMap<String, Price> GetGamesPricesHelper(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers) throws APIException {
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
            LinkedHashMap<String, Price> prices = new LinkedHashMap<>();
            for (i = 0; i < priceJSONArray.length(); i++) {
                JSONObject jsonObject = priceJSONArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                JSONObject history = jsonObject.getJSONObject("historyLow");
                Cost allTimeLow = null;
                Cost threeMonthLow = null;
                Cost oneYearLow = null;
                if (history != null) {
                    JSONObject all;
                    try{
                        all = history.getJSONObject("all");
                        allTimeLow = new Cost(all.getFloat("amount"), all.getString("currency"));
                    }
                    catch (Exception _){}

                    JSONObject y1;
                    try {
                        y1 = history.getJSONObject("y1");
                        oneYearLow = new Cost(y1.getFloat("amount"), y1.getString("currency"));
                    }
                    catch (Exception _){}

                    JSONObject m3;
                    try{
                        m3 = history.getJSONObject("m3");
                        threeMonthLow = new Cost(m3.getFloat("amount"), m3.getString("currency"));
                    }
                    catch (Exception _){}

                }

                JSONArray dealsJSON = jsonObject.getJSONArray("deals");
                ArrayList<Deal> deals = new ArrayList<>();
                for (int j = 0; j < dealsJSON.length(); j++) {
                    JSONObject deal = dealsJSON.getJSONObject(j);
                    if (deal != null) {
                        int shopID = -1;
                        String shopName = "";
                        try{
                            JSONObject shop = deal.getJSONObject("shop");
                            shopID = shop.getInt("id");
                            shopName = shop.getString("name");
                        }catch (JSONException _){};

                        Cost price = null;
                        try{
                            JSONObject priceJSON = deal.getJSONObject("price");
                            price = new Cost(priceJSON.getFloat("amount"), priceJSON.getString("currency"));
                        }catch (JSONException _){}

                        Cost regularPrice = null;
                        try{
                            JSONObject regularPriceJSON = deal.getJSONObject("regular");
                            regularPrice = new Cost(regularPriceJSON.getFloat("amount"), regularPriceJSON.getString("currency"));
                        }catch (JSONException _){};

                        float cut = 0;
                        try {
                            cut = deal.getFloat("cut");
                        }catch (Exception _){}

                        String voucher = "";
                        try{
                            voucher = deal.getString("voucher") != null ? deal.getString("voucher") : ""; //non so che cos'è
                        } catch (Exception _){}


                        Cost storeLow = null;
                        try{
                            JSONObject storeLowJSON = deal.getJSONObject("storeLow");
                            storeLow = new Cost(storeLowJSON.getFloat("amount"), storeLowJSON.getString("currency"));
                        }catch (Exception _){}

                        String flag = "";
                        try{
                            flag = deal.getString("flag");
                        }
                        catch (Exception _){}


                        ArrayList<DRM> drms = new ArrayList<>();
                        try{
                            JSONArray drm = deal.getJSONArray("drm");
                            for (int k = 0; k < drm.length(); k++) {
                                try{
                                    JSONObject d = drm.getJSONObject(k);
                                    int drmId = d.getInt("id");
                                    String drmName = d.getString("name");
                                    drms.add(new DRM(drmId, drmName));
                                }catch (Exception _){}
                            }
                        }catch (JSONException _){};



                        ArrayList<Platform> platforms = new ArrayList<>();
                        try{
                            JSONArray platformsJSON = deal.getJSONArray("platforms");
                            for (int q = 0; q < platformsJSON.length(); q++) {
                                try{
                                    JSONObject platform = platformsJSON.getJSONObject(q);
                                    Platform p = new Platform(platform.getInt("id"), platform.getString("name"));
                                    platforms.add(p);
                                }catch (Exception _){}
                            }
                        }catch (JSONException _){}

                        String timestampString = "";
                        ZonedDateTime timestamp = null;
                        try{
                            timestampString = deal.getString("timestamp");
                            timestamp = ZonedDateTime.parse(timestampString);
                        }catch (Exception _){};

                        String expiryString = "";
                        ZonedDateTime expiry = null;
                        try{
                            expiryString = deal.getString("expiry");
                            expiry = ZonedDateTime.parse(expiryString);
                        }
                        catch (Exception _){}


                        String gameUrl = "";
                        try{
                            gameUrl = deal.getString("url");
                        }catch (Exception _){}

                        Deal d = new Deal(shopID, shopName, price, regularPrice, cut, voucher, storeLow, flag, drms, platforms, timestamp, expiry, gameUrl);
                        deals.add(d);
                    }
                }
                Price price = new Price(id, allTimeLow, threeMonthLow, oneYearLow, deals);

                prices.put(id, price);
            }
            return prices;
        }
        catch (Exception e) {
            throw new APIException(e.getMessage());
        }
    }

    public LinkedHashMap<String, Price> GetGamesPrices(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers) throws APIException {
        int chunk = 200;
        ArrayList<LinkedHashMap<String, Price>> hashMaps = new ArrayList<>();
        for(int i=0; i < isThereAnyDealIDs.size(); i+=chunk){
            ArrayList<String> subArray = new ArrayList<String>(isThereAnyDealIDs.subList(i, Math.min(isThereAnyDealIDs.size(), i+chunk)));
            LinkedHashMap<String, Price> result = GetGamesPricesHelper(subArray, shopIDs, country, onlyDeals, capacity, vouchers);

            if (result != null){
                hashMaps.add(result);
            }
        }
        LinkedHashMap<String, Price> prices = new LinkedHashMap<>();
        for (LinkedHashMap<String, Price> hashMap : hashMaps){
            prices.putAll(hashMap);
        }
        return prices;
    }


    public ArrayList<Shop> GetShops(String country) throws APIException {
        String url = "https://api.isthereanydeal.com/service/shops/v1?country=" + country;

        HttpResponse<String> response = GETRequest(url);

        if (response == null || response.statusCode() != 200){
            throw new APIException("API response is null");
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

    public ArrayList<Integer> GetShopIDs(String country) throws APIException {
        ArrayList<Shop> shops = GetShops(country);
        ArrayList<Integer> shopIDs = new ArrayList<>();
        for (Shop shop : shops){
            shopIDs.add(shop.getId());
        }
        return shopIDs;
    }

    public ArrayList<Game> GetGamesWithDeals(String country, int offset, int limit, String sort, boolean nondeals, boolean mature, ArrayList<Integer> shopIDs, String filter) throws APIException{
        offset = Math.max(0, offset);
        limit = Math.clamp(limit, 1, 200);
        String url = null;
        try {
            url = "https://api.isthereanydeal.com/deals/v2?country=" + country + "&offset=" + offset + "&limit=" + limit + "&sort=" + sort + "&filter=" + filter + "&nondeals=" + nondeals + "&mature=" + mature + "&key=" + getKey();
        } catch (IOException e) {
            throw new APIException("Couldn't get key");
        }
        System.out.println(url);
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


        HttpResponse<String> response = GETRequest(url);

        if (response == null || response.statusCode() != 200){
            throw new APIException("Errore nella risposta");
        }

        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray list = jsonResponse.getJSONArray("list");

        ArrayList<Game> deals = new ArrayList<>();
        for (int j = 0; j < list.length(); j++) {
            try{
                JSONObject deal = list.getJSONObject(j);
                String dealID = deal.getString("id");
                String dealTitle = deal.getString("title");
                String dealType = deal.getString("type");
                boolean dealMature = deal.getBoolean("mature");
                JSONObject assetsJSON = deal.getJSONObject("assets");
                HashMap<String, String> assets = new HashMap<>();
                if (assetsJSON != null){
                    for (String key : assetsJSON.keySet()) {
                        String assetUrl = assetsJSON.getString(key);
                        if (assetUrl != null) assets.put(key, assetUrl);
                    }
                }
                Game game = new Game(dealID, dealTitle, dealType, dealMature, assets);
                deals.add(game);
            }catch (Exception _){}
        }
        return deals;
    }

    public LinkedHashMap<Game, Price> GetDeals(String country, int offset, int limit, String sort, boolean nondeals, boolean mature, ArrayList<Integer> shopIDs, String filter) throws APIException {

        if (!deals.keySet().isEmpty() && soonestExpiry.isAfter(ZonedDateTime.now())) {
            return deals;
        }

        ArrayList<Game> games = GetGamesWithDeals(country, offset, limit, sort, nondeals, mature, shopIDs, filter);
        ArrayList<String> gameIDs = new ArrayList<>();
        for (Game game : games) {
            gameIDs.add(game.getIsThereAnyDealID());
        }
        HashMap<String, Price> prices = GetGamesPrices(gameIDs, shopIDs, country, true, 100, false);
        LinkedHashMap<Game, Price> deals = new LinkedHashMap<>();
        for (Game game : games) {
            deals.put(game, prices.get(game.getIsThereAnyDealID()));
        }

        APIImplementation.deals = deals;
        for (String key : prices.keySet()){
            Deal d = prices.get(key).GetBestDeal();
            if (soonestExpiry == null || (d.getExpiry() != null && d.getExpiry().isBefore(soonestExpiry))) soonestExpiry = d.getExpiry();
        }
        return deals;
    }
}