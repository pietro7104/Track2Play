package Model.APIControl;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Price {
    String isThereAnyDealGameID;


    Cost allTimeLow;
    Cost threeMonthsLow;
    Cost oneYearLow;

    public ArrayList<Deal> deals;

    public String getIsThereAnyDealGameID() {
        return isThereAnyDealGameID;
    }

    public void setIsThereAnyDealGameID(String isThereAnyDealGameID) {
        this.isThereAnyDealGameID = isThereAnyDealGameID;
    }

    public Cost getAllTimeLow() {
        return allTimeLow;
    }

    public void setAllTimeLow(Cost allTimeLow) {
        this.allTimeLow = allTimeLow;
    }

    public Cost getThreeMonthsLow() {
        return threeMonthsLow;
    }

    public void setThreeMonthsLow(Cost threeMonthsLow) {
        this.threeMonthsLow = threeMonthsLow;
    }

    public Cost getOneYearLow() {
        return oneYearLow;
    }

    public void setOneYearLow(Cost oneYearLow) {
        this.oneYearLow = oneYearLow;
    }

    public ArrayList<Deal> getDeals() {
        return deals;
    }

    public void setDeals(ArrayList<Deal> deals) {
        this.deals = deals;
    }

    public Price(String isThereAnyDealGameID, Cost allTimeLow, Cost threeMonthsLow, Cost oneYearLow, ArrayList<Deal> deals) {
        this.isThereAnyDealGameID = isThereAnyDealGameID;
        this.allTimeLow = allTimeLow;
        this.threeMonthsLow = threeMonthsLow;
        this.oneYearLow = oneYearLow;
        this.deals = deals;
    }

    public Deal GetBestDeal() {
        Deal bestDeal = deals.getFirst();
        for (Deal deal : deals) {
            if (deal.dealPrice.amount < bestDeal.dealPrice.amount) {
                bestDeal = deal;
            }
        }
        return bestDeal;
    }
}
