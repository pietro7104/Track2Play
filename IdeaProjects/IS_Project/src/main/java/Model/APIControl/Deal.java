package Model.APIControl;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Deal {
    ZonedDateTime timestamp;

    public int shopID;
    public String shopName;

    public Cost regularPrice;

    public Cost dealPrice;

    public Cost storeLow;


    public ZonedDateTime expiry;

    public float cut;

    public String voucher;

    public ArrayList<DRM> drms;
    public ArrayList<Platform> platforms;

    public String url;

    public String flag; //H = historical low, N = new historical low, S = store low, "" no flag

    public Deal(int shopID, String shopName, Cost dealPrice, Cost regularPrice, float cut, String voucher, Cost storeLow, String flag, ArrayList<DRM> drms, ArrayList<Platform> platforms, ZonedDateTime timestamp, ZonedDateTime expiry, String url) {
        this.shopID = shopID;
        this.shopName = shopName;
        this.dealPrice = dealPrice;
        this.regularPrice = regularPrice;
        this.storeLow = storeLow;
        this.expiry = expiry;
        this.cut = cut;
        this.voucher = voucher;
        this.drms = drms;
        this.platforms = platforms;
        this.timestamp = timestamp;
        this.url = url;
        this.flag = flag;
    }
}
