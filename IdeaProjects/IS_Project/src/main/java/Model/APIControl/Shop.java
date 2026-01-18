package Model.APIControl;

import java.time.ZonedDateTime;

public class Shop {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getDeals() {
        return deals;
    }

    public void setDeals(int deals) {
        this.deals = deals;
    }

    public ZonedDateTime getITADupdate() {
        return ITADupdate;
    }

    public void setITADupdate(ZonedDateTime ITADupdate) {
        this.ITADupdate = ITADupdate;
    }

    public ZonedDateTime getUpdate() {
        return update;
    }

    public void setUpdate(ZonedDateTime update) {
        this.update = update;
    }

    public int id;
    public String name;

    public int games;
    public int deals;

    public ZonedDateTime ITADupdate; //forse dovrebbe essere una data
    public ZonedDateTime update;

    public Shop(int id, String name, int games, int deals, ZonedDateTime ITADupdate, ZonedDateTime update) {
        this.id = id;
        this.name = name;
        this.games = games;
        this.deals = deals;
        this.ITADupdate = ITADupdate;
        this.update = update;
    }
}
