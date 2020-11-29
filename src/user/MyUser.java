package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyUser {

    private String username;
    private String subscriptionType;
    private Map<String, Integer> history;
    private ArrayList<String> favoriteMovies;
    private Map<String, Double> ratingsMovie = new HashMap<>();
    private ArrayList<String> ratingsShow = new ArrayList<>();
    /**
     * constructor
     */
    public MyUser(final String username, final String subscriptionType,
                  final Map<String, Integer> history, final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = new HashMap<>(history);
        this.favoriteMovies = new ArrayList<>(favoriteMovies);
    }
    /**
     * getRatingsShow
     */
    public ArrayList<String> getRatingsShow() {
        return ratingsShow;
    }
    /**
     * addToRatingShow
     */
    public void addToRatingShow(final String title, final int noSeason) {

        String noSeason2 = String.valueOf(noSeason);
        if (!containsShow(title, noSeason)) {
            this.ratingsShow.add(title);
            this.ratingsShow.add(noSeason2);
        }
    }
    /**
     * addToRatingMovie
     */
    public void addToRatingMovie(final String title, final double grade) {
        this.ratingsMovie.put(title, grade);
    }
    /**
     * getRatingsMovie
     */
    public Map<String, Double> getRatingsMovie() {
        return ratingsMovie;
    }
    /**
     * containsShow
     */
    public boolean containsShow(final String title, final int noSeason) {
        String noSeason2 = String.valueOf(noSeason);

        for (int i = 0; i < this.ratingsShow.size(); i += 2) {
            if (this.ratingsShow.get(i).equals(title)) {
                if (this.ratingsShow.get(i + 1).equals(noSeason2)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * addToHistory
     */
    public void addToHistory(final String movie) {
        this.history.put(movie, 1);
    }
    /**
     * removeFromHistory
     */
    public void removeFromHistory(final String movie) {
        this.history.remove(movie);
    }
    /**
     * incrementToHistory
     */
    public void incrementToHistory(final String movie) {

        for (Map.Entry<String, Integer> entry : this.history.entrySet()) {
            //ma duc pe filmul dat si incrementez value
            if (entry.getKey().equals(movie)) {
                this.history.put(entry.getKey(), entry.getValue() + 1);
                break;
            }
        }
    }
    /**
     * addToFav
     */
    public void addToFav(final String movie) {
        this.favoriteMovies.add(movie);
    }
    /**
     * removeFav
     */
    public void removeFav(final String movie) {
        this.favoriteMovies.remove(movie);
    }
    /**
     * containsFav
     */
    public boolean containsFav(final String movie) {
        boolean contain = false;

        for (int i = 0; i < favoriteMovies.size(); i++) {

            if (movie.equals(favoriteMovies.get(i))) {
                contain = true;
                break;
            }
        }

        return contain;
    }
    /**
     * getHistory
     */
    public Map<String, Integer> getHistory() {
        return history;
    }
    /**
     * setHistory
     */
    public void setHistory(final Map<String, Integer> history) {
        this.history = history;
    }
    /**
     * getFavoriteMovies
     */
    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }
    /**
     * getUsername
     */
    public String getUsername() {
        return username;
    }
    /**
     * setUsername
     */
    public void setUsername(final String username) {
        this.username = username;
    }
    /**
     * getSubscriptionType
     */
    public String getSubscriptionType() {
        return subscriptionType;
    }
    /**
     * setSubscriptionType
     */
    public void setSubscriptionType(final String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
}
