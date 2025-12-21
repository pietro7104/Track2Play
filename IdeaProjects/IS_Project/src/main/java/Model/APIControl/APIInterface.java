package Model.APIControl;

import Model.Game;

import java.util.ArrayList;

public interface APIInterface {

    //public abstract ArrayList<String>

    public abstract ArrayList<Game>SearchByTitle(String title, int maxResults); //maxResults 0-100

    public abstract Game GetGameInfoByIsThereAnyDealID(String isthereanydealGameID);

    public abstract Game GetGameInfoBySteamID(String steamID);

    public abstract ArrayList<ArrayList<String>> GetGamesIDsOnShop(ArrayList<String> isThereAnyDealIDs, int shopID);//Ottiene gli ID di una lista di giochi su un negozio a scelta a partire dall'ID di IsThereAnyDeal, alcuni giochi possono avere più id (es. se sono in bundle esce anche l'id del bundle) per questo il risultato è un array di array

    public abstract ArrayList<Price> GetGamesPrices(ArrayList<String> isThereAnyDealIDs, ArrayList<Integer> shopIDs, String country, boolean onlyDeals, int capacity, boolean vouchers); //massimo 200 giochi alla volta. country = codice di due lettere (ISO 3166-1 alpha-2) .capacità = numero di prezzi per gioco, impostare a 0 per nessun limite. onlyDeals = true restituisce solo prezzi scontati

    //public abstract ArrayList<Price> GetGamesPriceOverview();


}
