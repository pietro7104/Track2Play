package Model.APIControl;

import Model.APIControl.APIExceptions.APIException;
import Model.Game;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

public interface APIInterface {

    //public abstract ArrayList<String>

    public Shop GetShopByName(String shopName, String country, Duration maxTimeSinceLastUpdate) throws APIException;
    //maxTimeSinceLastUpdate indica il tempo massimo dall'ultimo controllo dei dati dello shop. Se il tempo passato dall'ultimo controllo è superiore a questo valore, viene eseguita una chiamata all'API per aggiornare i dati. Lasciare null per ignorare

    public ArrayList<Game>SearchByTitle(String title, int maxResults) throws APIException; //maxResults 0-100

    public Game GetGameInfoByIsThereAnyDealID(String isthereanydealGameID) throws APIException;

    public Game GetGameInfoBySteamID(String steamID) throws APIException;

    public HashMap<String, ArrayList<String>> GetGamesIDsOnShop(ArrayList<String> isThereAnyDealIDs, int shopID) throws APIException;//Ottiene gli ID di una lista di giochi su un negozio a scelta a partire dall'ID di IsThereAnyDeal, alcuni giochi possono avere più id (es. se sono in bundle esce anche l'id del bundle)

    public HashMap<String, Price> GetGamesPrices(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers) throws APIException; //massimo 200 giochi alla volta. country = codice di due lettere (ISO 3166-1 alpha-2) .capacità = numero di prezzi per gioco, impostare a 0 per nessun limite. onlyDeals = true restituisce solo prezzi scontati

    //public abstract ArrayList<Price> GetGamesPriceOverview();

    public ArrayList<Shop> GetShops(String country) throws APIException;

    public ArrayList<Integer> GetShopIDs(String country) throws APIException;

}
