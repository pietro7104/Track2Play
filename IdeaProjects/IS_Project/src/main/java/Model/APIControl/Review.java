package Model.APIControl;

public class Review {
    public float score;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String source;
    public int count;
    public String url;

    public Review(float score, String source, int count, String url) {
        this.score = score;
        this.source = source;
        this.count = count;
        this.url = url;
    }
}
