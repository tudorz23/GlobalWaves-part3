package database.analytics;

import java.util.Map;
import java.util.TreeMap;

public class ArtistAnalytics extends Analytics {
    private Map<String, Integer> artistTopAlbums;
    private Map<String, Integer> artistTopSongs;
    private Map<String, Integer> artistTopFans;
    private Map<String, Integer> artistTopCities;

    /* Constructor */
    public ArtistAnalytics() {
        artistTopAlbums = new TreeMap<>();
        artistTopSongs = new TreeMap<>();
        artistTopFans = new TreeMap<>();
        artistTopCities = new TreeMap<>();
    }

    public void addAlbum(String albumName) {
        int listens = artistTopAlbums.getOrDefault(albumName, 0);
        artistTopAlbums.put(albumName, listens + 1);
    }

    public void addSong(String songName) {
        int listens = artistTopSongs.getOrDefault(songName, 0);
        artistTopSongs.put(songName, listens + 1);
    }

    public void addFan(String fanName) {
        int listens = artistTopFans.getOrDefault(fanName, 0);
        artistTopFans.put(fanName, listens + 1);
    }

    public void addCity(String cityName) {
        int listens = artistTopCities.getOrDefault(cityName, 0);
        artistTopCities.put(cityName, listens + 1);
    }

    /* Getters and Setters */
    public Map<String, Integer> getArtistTopAlbums() {
        return artistTopAlbums;
    }
    public void setArtistTopAlbums(Map<String, Integer> artistTopAlbums) {
        this.artistTopAlbums = artistTopAlbums;
    }
    public Map<String, Integer> getArtistTopSongs() {
        return artistTopSongs;
    }
    public void setArtistTopSongs(Map<String, Integer> artistTopSongs) {
        this.artistTopSongs = artistTopSongs;
    }
    public Map<String, Integer> getArtistTopFans() {
        return artistTopFans;
    }
    public void setArtistTopFans(Map<String, Integer> artistTopFans) {
        this.artistTopFans = artistTopFans;
    }
    public Map<String, Integer> getArtistTopCities() {
        return artistTopCities;
    }
    public void setArtistTopCities(Map<String, Integer> artistTopCities) {
        this.artistTopCities = artistTopCities;
    }
}
