package Model.APIControl;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Price {
    String isThereAnyDealGameID;


    Cost allTimeLow;
    Cost threeMonthsLow;
    Cost oneYearLow;

    public ArrayList<Deal> deals;

    public Price(String isThereAnyDealGameID, Cost allTimeLow, Cost threeMonthsLow, Cost oneYearLow, ArrayList<Deal> deals) {
        this.isThereAnyDealGameID = isThereAnyDealGameID;
        this.allTimeLow = allTimeLow;
        this.threeMonthsLow = threeMonthsLow;
        this.oneYearLow = oneYearLow;
        this.deals = deals;

    }
}
