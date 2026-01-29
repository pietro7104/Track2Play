package Model;

public class UserStats {

    private int totalGames;
    private int completedGames;
    private int notCompletedGames;
    private int purchasedGames;
    private int giftedGames;
    private double totalSpent;
    private String currency;

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getCompletedGames() {
        return completedGames;
    }

    public void setCompletedGames(int completedGames) {
        this.completedGames = completedGames;
    }

    public int getNotCompletedGames() {
        return notCompletedGames;
    }

    public void setNotCompletedGames(int notCompletedGames) {
        this.notCompletedGames = notCompletedGames;
    }

    public double getCompletionPercentage() {
        if (totalGames == 0) return 0;
        return (completedGames * 100.0) / totalGames;
    }

    public int getPurchasedGames() {
        return purchasedGames;
    }

    public void setPurchasedGames(int purchasedGames) {
        this.purchasedGames = purchasedGames;
    }

    public int getGiftedGames() {
        return giftedGames;
    }

    public void setGiftedGames(int giftedGames) {
        this.giftedGames = giftedGames;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


}

