package database.monetization;

import database.audio.Song;

import java.util.HashMap;
import java.util.Map;

public class Monetization {
    private Map<String, ArtistMoneyStats> monetizedArtists;

    /* Constructor */
    public Monetization() {
        monetizedArtists = new HashMap<>();
    }


    /**
     * Adds the new merch revenue to the given artist.
     * If the artist is not in the listenedArtists lists, he is added.
     */
    public void addMerchRevenue(String artistName, Double merchRevenue) {
        ArtistMoneyStats artistMoneyStats = monetizedArtists
                                            .getOrDefault(artistName, new ArtistMoneyStats());

        artistMoneyStats.addMerchRevenue(merchRevenue);

        monetizedArtists.put(artistName, artistMoneyStats);
    }


    /**
     * Adds the new song revenue to the given artist.
     * @param artistName Name of the paid artist.
     * @param song Song that gives the revenue.
     * @param songRevenue Amount of the revenue.
     */
    public void addSongRevenue(String artistName, Song song, double songRevenue) {
        ArtistMoneyStats artistMoneyStats = monetizedArtists
                .getOrDefault(artistName, new ArtistMoneyStats());

        artistMoneyStats.addSongRevenue(song, songRevenue);
        monetizedArtists.put(artistName, artistMoneyStats);
    }


    /**
     * Adds an artist to the monetizedArtists list, if he isn't already part of it.
     * To be called when one of its song is listened to.
     */
    public void addMonetizedArtist(String artistName) {
        ArtistMoneyStats artistMoneyStats = monetizedArtists
                        .getOrDefault(artistName, new ArtistMoneyStats());
        monetizedArtists.put(artistName, artistMoneyStats);
    }


    /* Getters and Setters */
    public Map<String, ArtistMoneyStats> getMonetizedArtists() {
        return monetizedArtists;
    }
}
