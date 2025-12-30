package Model.APIControl;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Shop {
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
