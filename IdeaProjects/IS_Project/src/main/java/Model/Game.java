package Model;

import Model.APIControl.Developer;
import Model.APIControl.Publisher;
import Model.APIControl.Review;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    String steamID;
    String isThereAnyDealID;
    String title;
    String slug;
    String type;
    boolean mature;

    public Game(String steamID, String isThereAnyDealID, String title, String slug, String type, boolean mature, ArrayList<String> tags, ArrayList<Developer> developers, ArrayList<Publisher> publishers, ArrayList<Review> reviews, int rank, int waitlisted, int collected, int recentPlayers, int playersPerDay, int playersPerWeek, int peakPlayers, HashMap<String, String> urls, HashMap<String, String> assets, LocalDate releaseDate, boolean hasTradingCards, boolean hasAchievements, boolean isEarlyAccess) {
        this.steamID = steamID;
        this.isThereAnyDealID = isThereAnyDealID;
        this.title = title;
        this.slug = slug;
        this.type = type;
        this.mature = mature;
        this.tags = tags;
        this.developers = developers;
        this.publishers = publishers;
        this.reviews = reviews;
        this.rank = rank;
        this.waitlisted = waitlisted;
        this.collected = collected;
        this.recentPlayers = recentPlayers;
        this.playersPerDay = playersPerDay;
        this.playersPerWeek = playersPerWeek;
        this.peakPlayers = peakPlayers;
        this.urls = urls;
        this.assets = assets;
        this.releaseDate = releaseDate;
        this.hasTradingCards = hasTradingCards;
        this.hasAchievements = hasAchievements;
        this.isEarlyAccess = isEarlyAccess;

    }

    public String getSteamID() {
        return steamID;
    }

    public void setSteamID(String steamID) {
        this.steamID = steamID;
    }

    public String getIsThereAnyDealID() {
        return isThereAnyDealID;
    }

    public void setIsThereAnyDealID(String isThereAnyDealID) {
        this.isThereAnyDealID = isThereAnyDealID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMature() {
        return mature;
    }

    public void setMature(boolean mature) {
        this.mature = mature;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(ArrayList<Developer> developers) {
        this.developers = developers;
    }

    public ArrayList<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(ArrayList<Publisher> publishers) {
        this.publishers = publishers;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getWaitlisted() {
        return waitlisted;
    }

    public void setWaitlisted(int waitlisted) {
        this.waitlisted = waitlisted;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    public int getRecentPlayers() {
        return recentPlayers;
    }

    public void setRecentPlayers(int recentPlayers) {
        this.recentPlayers = recentPlayers;
    }

    public int getPlayersPerDay() {
        return playersPerDay;
    }

    public void setPlayersPerDay(int playersPerDay) {
        this.playersPerDay = playersPerDay;
    }

    public int getPlayersPerWeek() {
        return playersPerWeek;
    }

    public void setPlayersPerWeek(int playersPerWeek) {
        this.playersPerWeek = playersPerWeek;
    }

    public int getPeakPlayers() {
        return peakPlayers;
    }

    public void setPeakPlayers(int peakPlayers) {
        this.peakPlayers = peakPlayers;
    }

    public HashMap<String, String> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, String> urls) {
        this.urls = urls;
    }

    public HashMap<String, String> getAssets() {
        return assets;
    }

    public void setAssets(HashMap<String, String> assets) {
        this.assets = assets;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isHasTradingCards() {
        return hasTradingCards;
    }

    public void setHasTradingCards(boolean hasTradingCards) {
        this.hasTradingCards = hasTradingCards;
    }

    public boolean isHasAchievements() {
        return hasAchievements;
    }

    public void setHasAchievements(boolean hasAchievements) {
        this.hasAchievements = hasAchievements;
    }

    public boolean isEarlyAccess() {
        return isEarlyAccess;
    }

    public void setEarlyAccess(boolean earlyAccess) {
        isEarlyAccess = earlyAccess;
    }

    ArrayList<String> tags;
    ArrayList<Developer> developers;
    ArrayList<Publisher> publishers;

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    ArrayList<Review> reviews;


    int rank;
    int waitlisted;
    int collected;

    int recentPlayers;
    int playersPerDay;
    int playersPerWeek;
    int peakPlayers;

    HashMap<String, String> urls;
    HashMap<String, String> assets;

    LocalDate releaseDate;
    boolean hasTradingCards;
    boolean hasAchievements;
    boolean isEarlyAccess;

    public Game(String steamID, String isThereAnyDealID, String title, String type, boolean mature) {
        this.steamID = steamID;
        this.isThereAnyDealID = isThereAnyDealID;
        this.title = title;
        this.type = type;
        this.mature = mature;
    }

    public Game(String isThereAnyDealID, String title, String type, boolean mature, HashMap<String, String> assets) {
        this.isThereAnyDealID = isThereAnyDealID;
        this.title = title;
        this.type = type;
        this.mature = mature;
        this.assets = assets;
    }

    public Game(String isThereAnyDealID, String title, String banner){
        this.isThereAnyDealID = isThereAnyDealID;
        this.title = title;
        HashMap<String, String> assets = new HashMap<>();
        assets.put("banner", banner);
        this.assets = assets;
    }

    public String GetHighestResolutionBanner(){

        String highestResolution = "";
        int resolution = 0;
        for (String key : assets.keySet()) {
            if (key.contains("banner")){
                String res = key.replace("banner", "");
                int r;
                try{r = Integer.parseInt(res);}
                catch(Exception e){
                    r = 0;
                    System.out.println(title + " - " + key);
                }

                if (r >= resolution) {
                    highestResolution = key;
                    resolution = r;
                }
            }
        }
        if (highestResolution.isEmpty()) return "";
        return assets.get(highestResolution);
    }


}
