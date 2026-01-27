package Model.APIControl;

import Model.APIControl.APIExceptions.APIException;
import Model.Game;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface APIInterface {

    public Shop GetShopByName(String shopName, String country, Duration maxTimeSinceLastUpdate) throws APIException;
    //maxTimeSinceLastUpdate indica il tempo massimo dall'ultimo controllo dei dati dello shop. Se il tempo passato dall'ultimo controllo è superiore a questo valore, viene eseguita una chiamata all'API per aggiornare i dati. Lasciare null per usare la durate default (1 giorno)

    public ArrayList<Game>SearchByTitle(String title, int maxResults) throws APIException; //maxResults 0-100
    //Ricerca i giochi in base al titolo. Non ottiene tutte le informazioni dei giochi (solo titolo, id, tipo, maturo e assets). Per le informazioni complete usare GetGameInfo.

    public Game GetGameInfoByIsThereAnyDealID(String isthereanydealGameID) throws APIException;
    //ottiene le informazioni complete di un gioco identificato dall'ID di esso su IsThereAnyDeal


    public Game GetGameInfoBySteamID(String steamID) throws APIException;
    //ottiene le informazioni complete di un gioco identificato dall'ID di esso su Steam

    public LinkedHashMap<String, ArrayList<String>> GetGamesIDsOnShop(ArrayList<String> isThereAnyDealIDs, int shopID) throws APIException;//Ottiene gli ID di una lista di giochi su un negozio a scelta a partire dall'ID di IsThereAnyDeal, alcuni giochi possono avere più id (es. se sono in bundle esce anche l'id del bundle)
    //"traduce" l'ID di un gioco su IsThereAnyDeal a ID sullo shop specificato. Può restituire più ID.

    public LinkedHashMap<String, Price> GetGamesPrices(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers) throws APIException; //massimo 200 giochi alla volta. country = codice di due lettere (ISO 3166-1 alpha-2) .capacità = numero di prezzi per gioco, impostare a 0 per nessun limite. onlyDeals = true restituisce solo prezzi scontati
    //Ottiene le informazioni su i prezzi di una lista di giochi identificati da ID su IsThereAnyDeal.
    // Le restituisce in una LinkedHashMap in cui l'ID del gioco è la chiave, che si usa per accedere al suo prezzo

    public ArrayList<Shop> GetShops(String country) throws APIException;
    //Ottiene la lista di Shop disponibili in una nazione specificata dall'ISO country code


    public ArrayList<Integer> GetShopIDs(String country) throws APIException;
    //Ottiene la lista degli ID degli Shop disponibili in una nazione specificata dall'ISO country code

    public ArrayList<Game> GetGamesWithDeals(String country, int offset, int limit, String sort, boolean nondeals, boolean mature, ArrayList<Integer> shopIDs, String filter) throws APIException;
    //Ottiene la lista di giochi con deal, ma senza informazioni sui prezzi e deal.
    //Usata più che altro come helper per l'implementazione di GetDeals

    public LinkedHashMap<Game, Price> GetDeals(String country, int offset, int limit, String sort, boolean nondeals, boolean mature, ArrayList<Integer> shopIDs, String filter) throws APIException;
    //Ottiene la lista di giochi con deal, insieme alle informazioni dei prezzi e dei deal.
    //Restituisce una LinkedHashMap che usa i Game come chiave per accedere al rispettivo prezzo.

}