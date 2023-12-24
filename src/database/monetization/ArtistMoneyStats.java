package database.monetization;

import database.audio.Song;

import java.util.HashMap;
import java.util.Map;

public class ArtistMoneyStats {
    private double songRevenue;
    private double merchRevenue;
    private Map<Song, Double> payingSongs;

    /* Constructor */
    public ArtistMoneyStats() {
        payingSongs = new HashMap<>();
    }


    /**
     * Adds the new merch revenue to the merchRevenue storage.
     * @param newMerchRevenue Merch revenue to add.
     */
    public void addMerchRevenue(double newMerchRevenue) {
        merchRevenue += newMerchRevenue;
    }

    /* Getters and Setters */
    public Double getSongRevenue() {
        return songRevenue;
    }
    public void setSongRevenue(Double songRevenue) {
        this.songRevenue = songRevenue;
    }
    public Double getMerchRevenue() {
        return merchRevenue;
    }
    public void setMerchRevenue(Double merchRevenue) {
        this.merchRevenue = merchRevenue;
    }
    public Map<Song, Double> getPayingSongs() {
        return payingSongs;
    }
}
