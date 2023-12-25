package database.monetization;

import database.audio.Song;

import java.util.HashMap;
import java.util.Map;

public class ArtistMoneyStats {
    private double songRevenue;
    private double merchRevenue;
    private double totalRevenue;
    private Map<Song, Double> payingSongs;

    /* Constructor */
    public ArtistMoneyStats() {
        payingSongs = new HashMap<>();
    }


    /**
     * Adds the new merch revenue to the merchRevenue and totalRevenue storage.
     * @param newMerchRevenue Merch revenue to add.
     */
    public void addMerchRevenue(double newMerchRevenue) {
        merchRevenue += newMerchRevenue;
        totalRevenue += newMerchRevenue;
    }


    /**
     * Adds the new song revenue to the songRevenue and totalRevenue storage.
     * Also adds the song to the paying song lists or
     * updates its amount if it is already there.
     */
    public void addSongRevenue(Song song, double newSongRevenue) {
        songRevenue += newSongRevenue;
        totalRevenue += newSongRevenue;

        double songSpecificRevenue = payingSongs.getOrDefault(song, 0.0);
        songSpecificRevenue += newSongRevenue;
        payingSongs.put(song, songSpecificRevenue);
    }


    /* Getters and Setters */
    public Double getSongRevenue() {
        return songRevenue;
    }
    public Double getMerchRevenue() {
        return merchRevenue;
    }
    public Map<Song, Double> getPayingSongs() {
        return payingSongs;
    }
    public double getTotalRevenue() {
        return totalRevenue;
    }
}
