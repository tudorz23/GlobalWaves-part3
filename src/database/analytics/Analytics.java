package database.analytics;

import database.audio.Audio;
import database.audio.Podcast;
import database.audio.Song;
import database.records.Merch;
import database.records.Notification;
import database.users.ContentCreator;

import java.util.*;

public class Analytics {
    private Map<String, Integer> topArtists;
    private Map<String, Integer> topGenres;
    private Map<Audio, Integer> topSongs;
    private Map<String, Integer> topAlbums;
    private Map<String, Integer> topEpisodes;

    private List<ContentCreator> subscribedToList;
    private List<Notification> notifications;

    private List<Merch> merchCollection;

    /* Constructor */
    public Analytics() {
        topArtists = new HashMap<>();
        topGenres = new HashMap<>();
        topSongs = new HashMap<>();
        topAlbums = new HashMap<>();
        topEpisodes = new HashMap<>();
        subscribedToList = new ArrayList<>();
        notifications = new ArrayList<>();
        merchCollection = new ArrayList<>();
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

    public void addEpisode(String episode) {
        int listens = topEpisodes.getOrDefault(episode, 0);
        topEpisodes.put(episode, listens + 1);
    }


    public void addNotification(Notification notification) {
        notifications.add(notification);
    }


    public void clearNotifications() {
        notifications = new ArrayList<>();
    }


    public void subscribeTo(ContentCreator creator) {
        subscribedToList.add(creator);
    }


    public void unsubscribedFrom(ContentCreator creator) {
        subscribedToList.remove(creator);
    }


    public boolean isSubscribedTo(ContentCreator creator) {
        return subscribedToList.contains(creator);
    }


    public void addMerch(Merch merch) {
        merchCollection.add(merch);
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
    public void setTopEpisodes(Map<String, Integer> topEpisodes) {
        this.topEpisodes = topEpisodes;
    }
    public Map<String, Integer> getTopEpisodes() {
        return topEpisodes;
    }
    public List<ContentCreator> getSubscribedToList() {
        return subscribedToList;
    }
    public void setSubscribedToList(List<ContentCreator> subscribedToList) {
        this.subscribedToList = subscribedToList;
    }
    public List<Merch> getMerchCollection() {
        return merchCollection;
    }
}
