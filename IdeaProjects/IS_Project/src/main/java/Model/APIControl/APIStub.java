package Model.APIControl;

import Model.APIControl.APIExceptions.APIException;
import Model.Game;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

public class APIStub implements APIInterface{

    private Game getTestGame(){
        return new Game("test id", "test id", "Game", "test slug", "test type", false, null, null, null, null, 0, 0, 0, 0, 0, 0, 0, null, null, null, false, false, false);
    }

    private Price getTestPrice(){
        return new Price("test id", null, null, null, null);
    }

    public Shop GetShopByName(String shopName, String country, Duration maxTimeSinceLastUpdate) throws APIException
    {
        return new Shop(0, "Shop", 0, 0, null, null);
    }

    public ArrayList<Game> SearchByTitle(String title, int maxResults) throws APIException
    {
        ArrayList<Game> games = new ArrayList<>();
        Game game = getTestGame();
        games.add(game);
        return games;
    }

    public Game GetGameInfoByIsThereAnyDealID(String isthereanydealGameID) throws APIException{
        return getTestGame();
    }

    public Game GetGameInfoBySteamID(String steamID) throws APIException
    {
        return getTestGame();
    }

    public HashMap<String, ArrayList<String>> GetGamesIDsOnShop(ArrayList<String> isThereAnyDealIDs, int shopID) throws APIException
    {
        HashMap<String, ArrayList<String>> gamesIDsOnShop = new HashMap<>();
        ArrayList<String> ids = new ArrayList<>();
        ids.add("test id");
        gamesIDsOnShop.put("test shop", ids);
        return gamesIDsOnShop;
    }

    public HashMap<String, Price> GetGamesPrices(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers) throws APIException
    {
        HashMap<String, Price> gamesPrices = new HashMap<>();
        gamesPrices.put("test id", getTestPrice());
        return gamesPrices;
    }


    public ArrayList<Shop> GetShops(String country) throws APIException
    {
        ArrayList<Shop> shops = new ArrayList<>();
        shops.add(GetShopByName("skd g", "gewnj", null));
        return shops;
    }

    public ArrayList<Integer> GetShopIDs(String country) throws APIException{
        ArrayList<Integer> shopIDs = new ArrayList<>();
        shopIDs.add(0);
        return shopIDs;
    }
}
