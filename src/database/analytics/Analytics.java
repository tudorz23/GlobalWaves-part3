package database.analytics;

import database.audio.Audio;
import database.audio.Playlist;
import database.audio.Song;
import database.records.Merch;
import database.records.Notification;
import database.users.ContentCreator;
import pages.Page;

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

    private List<Song> songRecommendations;
    private List<Playlist> playlistRecommendations;
    private Audio latestRecommendation;

    private LinkedList<Page> pageHistory;
    private LinkedList<Page> forwardPageHistory;


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
        songRecommendations = new ArrayList<>();
        playlistRecommendations = new ArrayList<>();
        pageHistory = new LinkedList<>();
        forwardPageHistory = new LinkedList<>();
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


    /**
     * Adds a new Notification to the notification list.
     */
    public void addNotification(Notification notification) {
        notifications.add(notification);
    }


    /**
     * Clears all the notifications stored.
     */
    public void clearNotifications() {
        notifications = new ArrayList<>();
    }


    /**
     * Adds a new content creator to the list of creators
     * that the user is subscribed to.
     */
    public void subscribeTo(ContentCreator creator) {
        subscribedToList.add(creator);
    }


    /**
     * Removes the given content creator from the list of creators
     * that the user is subscribed to.
     */
    public void unsubscribeFrom(ContentCreator creator) {
        subscribedToList.remove(creator);
    }


    /**
     * Checks if the user is subscribed to the given content creator.
     * @return true if he is, false otherwise.
     */
    public boolean isSubscribedTo(ContentCreator creator) {
        return subscribedToList.contains(creator);
    }


    /**
     * Adds a new merch to the list of merch pieces owned by the user.
     */
    public void addMerch(Merch merch) {
        merchCollection.add(merch);
    }


    /**
     * Sets the latest recommendation to a new one.
     */
    public void updateLatestRecommendation(Audio newRecommendation) {
        latestRecommendation = newRecommendation;
    }


    /**
     * Adds a new song recommendation, if it has not been recommended before.
     * @throws IllegalArgumentException if it has already been recommended.
     */
    public void addSongRecommendation(Song song) throws IllegalArgumentException {
        if (songRecommendations.contains(song)) {
            throw new IllegalArgumentException("Song " + song.getName()
                            + " has already been recommended.");
        }
        songRecommendations.add(song);
    }


    public void addPlaylistRecommendation(Playlist playlist) {
        playlistRecommendations.add(playlist);
    }


    public void pushPageHistory(Page newPage) {
        pageHistory.addFirst(newPage);
    }


    public Page popPageHistory() {
        return pageHistory.pop();
    }


    public boolean pageHistoryIsEmpty() {
        return pageHistory.isEmpty();
    }


    public void pushForwardPageHistory(Page oldPage) {
        forwardPageHistory.addFirst(oldPage);
    }


    public Page popForwardPageHistory() {
        return forwardPageHistory.pop();
    }


    public boolean forwardPageHistoryIsEmpty() {
        return forwardPageHistory.isEmpty();
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
    public List<Merch> getMerchCollection() {
        return merchCollection;
    }
    public List<Song> getSongRecommendations() {
        return songRecommendations;
    }
    public List<Playlist> getPlaylistRecommendations() {
        return playlistRecommendations;
    }
    public Audio getLatestRecommendation() {
        return latestRecommendation;
    }

    public LinkedList<Page> getPageHistory() {
        return pageHistory;
    }

    public void setPageHistory(LinkedList<Page> pageHistory) {
        this.pageHistory = pageHistory;
    }

    public LinkedList<Page> getForwardPageHistory() {
        return forwardPageHistory;
    }

    public void setForwardPageHistory(LinkedList<Page> forwardPageHistory) {
        this.forwardPageHistory = forwardPageHistory;
    }
}
