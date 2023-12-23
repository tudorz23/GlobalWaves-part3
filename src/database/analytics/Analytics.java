package database.analytics;

import database.audio.Audio;
import database.audio.Podcast;
import database.audio.Song;

import java.util.*;

public class Analytics {
    private List<Notification> notifications;

    private Map<String, Integer> topArtists;
    private Map<String, Integer> topGenres;
    private Map<Audio, Integer> topSongs;
    private Map<String, Integer> topAlbums;
    private Map<Audio, Integer> topPodcasts;

    /* Constructor */
    public Analytics() {
        notifications = new ArrayList<>();
        topArtists = new HashMap<>();
        topGenres = new HashMap<>();
        topSongs = new HashMap<>();
        topAlbums = new HashMap<>();
        topPodcasts = new HashMap<>();
    }


    public void addSong(Song song) {
        int listens = topSongs.getOrDefault(song, 0);
        topSongs.put(song, listens + 1);
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

    public void addPodcast(Podcast podcast) {
        int listens = topPodcasts.getOrDefault(podcast, 0);
        topPodcasts.put(podcast, listens + 1);
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
    public Map<Audio, Integer> getTopSongs() {
        return topSongs;
    }
    public Map<String, Integer> getTopAlbums() {
        return topAlbums;
    }
    public Map<Audio, Integer> getTopPodcasts() {
        return topPodcasts;
    }
}
