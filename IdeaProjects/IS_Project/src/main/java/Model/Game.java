package Model;

public class Game {
    String steamID;
    String isThereAnyDealID;
    String title;
    String type;
    boolean mature;


    public Game(String steamID, String isThereAnyDealID, String title, String type, boolean mature) {
        this.steamID = steamID;
        this.isThereAnyDealID = isThereAnyDealID;
        this.title = title;
        this.type = type;
        this.mature = mature;
    }

    public Game(String isThereAnyDealID, String title, String type, boolean mature) {
        this.isThereAnyDealID = isThereAnyDealID;
        this.title = title;
        this.type = type;
        this.mature = mature;
    }
}
