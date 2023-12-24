package database.monetization;

import java.util.HashMap;
import java.util.Map;

public class Monetization {
    private Map<String, ArtistMoneyStats> listenedArtists;

    /* Constructor */
    public Monetization() {
        listenedArtists = new HashMap<>();
    }


    /**
     * Adds the new merch revenue to the given artist.
     * If the artist is not in the listenedArtists lists, he is added.
     */
    public void addMerchRevenue(String artistName, Double merchRevenue) {
        ArtistMoneyStats artistMoneyStats = listenedArtists
                                            .getOrDefault(artistName, new ArtistMoneyStats());

        artistMoneyStats.addMerchRevenue(merchRevenue);
    }

    /* Getters and Setters */
    public Map<String, ArtistMoneyStats> getListenedArtists() {
        return listenedArtists;
    }
}
