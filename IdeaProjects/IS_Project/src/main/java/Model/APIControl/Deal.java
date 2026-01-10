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

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Cost getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(Cost regularPrice) {
        this.regularPrice = regularPrice;
    }

    public Cost getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(Cost dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Cost getStoreLow() {
        return storeLow;
    }

    public void setStoreLow(Cost storeLow) {
        this.storeLow = storeLow;
    }

    public ZonedDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    public float getCut() {
        return cut;
    }

    public void setCut(float cut) {
        this.cut = cut;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public ArrayList<DRM> getDrms() {
        return drms;
    }

    public void setDrms(ArrayList<DRM> drms) {
        this.drms = drms;
    }

    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<Platform> platforms) {
        this.platforms = platforms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

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
