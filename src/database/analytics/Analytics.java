package database.analytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Analytics {
    private List<Notification> notifications;

    private Map<String, Integer> topArtists;
    private Map<String, Integer> topGenres;
    private Map<String, Integer> topSongs;
    private Map<String, Integer> topAlbums;
    private Map<String, Integer> topPodcasts;

    /* Constructor */
    public Analytics() {
        notifications = new ArrayList<>();
        topArtists = new TreeMap<>();
        topGenres = new TreeMap<>();
        topSongs = new TreeMap<>();
        topAlbums = new TreeMap<>();
        topPodcasts = new TreeMap<>();
    }


    public void addSong(String songName) {
        int listens = topSongs.getOrDefault(songName, 0);
        topSongs.put(songName, listens + 1);
    }

    public void addArtist(String artistName) {
        int listens = topArtists.getOrDefault(artistName, 0);
        topArtists.put(artistName, listens + 1);
    }

    public void addGenre(String genre) {
        int listens = topGenres.getOrDefault(genre, 0);
        topGenres.put(genre, listens + 1);
    }

    public void addAlbum(String albumName) {
        int listens = topAlbums.getOrDefault(albumName, 0);
        topAlbums.put(albumName, listens + 1);
    }

    public void addPodcast(String podcastName) {
        int listens = topPodcasts.getOrDefault(podcastName, 0);
        topPodcasts.put(podcastName, listens + 1);
    }

    /* Getters and Setters */
    public List<Notification> getNotifications() {
        return notifications;
    }
    public Map<String, Integer> getTopArtists() {
        return topArtists;
    }
    public Map<String, Integer> getTopGenres() {
        return topGenres;
    }
    public Map<String, Integer> getTopSongs() {
        return topSongs;
    }
    public Map<String, Integer> getTopAlbums() {
        return topAlbums;
    }
    public Map<String, Integer> getTopPodcasts() {
        return topPodcasts;
    }
}
