package Model;

import java.util.Date;

public class WishlistItem {
    private String username;   // Nome utente che ha aggiunto l'articolo
    private String name;       // Nome del gioco
    private Date addedDate;    // Data di aggiunta

    // Costruttore vuoto
    public WishlistItem() {}

    // Costruttore con tutti i campi
    public WishlistItem(String username, String name, Date addedDate) {
        this.username = username;
        this.name = name;
        this.addedDate = addedDate;
    }

    // Getter e Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
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
