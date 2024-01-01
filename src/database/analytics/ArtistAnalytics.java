package database.analytics;

import database.audio.Audio;
import database.audio.Song;

import java.util.HashMap;
import java.util.Map;

public final class ArtistAnalytics extends ContentCreatorAnalytics {
    private Map<String, Integer> artistTopAlbums;
    private Map<Audio, Integer> artistTopSongs;
    private Map<String, Integer> artistTopFans;

    /* Constructor */
    public ArtistAnalytics() {
        artistTopAlbums = new HashMap<>();
        artistTopSongs = new HashMap<>();
        artistTopFans = new HashMap<>();
    }

    public void addAlbum(String albumName) {
        int listens = artistTopAlbums.getOrDefault(albumName, 0);
        artistTopAlbums.put(albumName, listens + 1);
    }

    public void addSong(Song song) {
        int listens = artistTopSongs.getOrDefault(song, 0);
        artistTopSongs.put(song, listens + 1);
    }

    public void addFan(String fanName) {
        int listens = artistTopFans.getOrDefault(fanName, 0);
        artistTopFans.put(fanName, listens + 1);
    }

    /* Getters and Setters */
    public Map<String, Integer> getArtistTopAlbums() {
        return artistTopAlbums;
    }
    public Map<Audio, Integer> getArtistTopSongs() {
        return artistTopSongs;
    }
    public Map<String, Integer> getArtistTopFans() {
        return artistTopFans;
    }
}
