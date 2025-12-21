package Model.APIControl;

import java.util.ArrayList;

public class Price {
    String isThereAnyDealGameID;

    String timestamp; //forse dovrebbe essere una data

    float allTimeLow;
    float oneYearLow;
    float threeMonthsLow;
    ArrayList<Deal> deals;
}
