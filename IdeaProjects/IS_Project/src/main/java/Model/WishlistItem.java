package Model;

import java.util.Date;

public class WishlistItem {
    private String gameImageURL;   // URL della copertina del gioco
    private String gameTitle;       // Titolo del gioco
    private Date addedDate;    // Data di aggiunta alla wishlist
    private String gameId;

    public WishlistItem() { }

    // Getter e Setter

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameImageURL() {
        return gameImageURL;
    }

    public void setGameImageURL(String gameImageURL) {
        this.gameImageURL = gameImageURL;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }


    /*@Override
    public String toString() {
        return "WishlistItem{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", addedDate=" + addedDate +
                '}';
    }*/
}
