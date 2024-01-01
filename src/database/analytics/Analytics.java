package database.analytics;

import database.audio.Audio;
import database.audio.Playlist;
import database.audio.Song;
import database.records.Merch;
import database.records.Notification;
import database.users.ContentCreator;
import pages.Page;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public final class Analytics {
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


    /**
     * Adds a Song object to User's topSongs map.
     */
    public void addSong(final Song song) {
        int listens = topSongs.getOrDefault(song, 0);
        topSongs.put(song, listens + 1);
    }


    /**
     * Adds an artist (i.e. his name as String) to User's topArtists map.
     */
    public void addArtist(final String artistName) {
        int listens = topArtists.getOrDefault(artistName, 0);
        topArtists.put(artistName, listens + 1);
    }


    /**
     * Adds a genre to User's topGenres map.
     */
    public void addGenre(final String genre) {
        int listens = topGenres.getOrDefault(genre, 0);
        topGenres.put(genre, listens + 1);
    }


    /**
     * Adds an album (i.e. its name as String) to User's topAlbums map.
     */
    public void addAlbum(final String albumName) {
        int listens = topAlbums.getOrDefault(albumName, 0);
        topAlbums.put(albumName, listens + 1);
    }


    /**
     * Adds an episode (i.e. its name as String) to User's topEpisodes map.
     */
    public void addEpisode(final String episode) {
        int listens = topEpisodes.getOrDefault(episode, 0);
        topEpisodes.put(episode, listens + 1);
    }


    /**
     * Adds a new Notification to the notification list.
     */
    public void addNotification(final Notification notification) {
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
    public void subscribeTo(final ContentCreator creator) {
        subscribedToList.add(creator);
    }


    /**
     * Removes the given content creator from the list of creators
     * that the user is subscribed to.
     */
    public void unsubscribeFrom(final ContentCreator creator) {
        subscribedToList.remove(creator);
    }


    /**
     * Checks if the user is subscribed to the given content creator.
     * @return true if he is, false otherwise.
     */
    public boolean isSubscribedTo(final ContentCreator creator) {
        return subscribedToList.contains(creator);
    }


    /**
     * Adds a new merch to the list of merch pieces owned by the user.
     */
    public void addMerch(final Merch merch) {
        merchCollection.add(merch);
    }


    /**
     * Sets the latest recommendation to a new one.
     */
    public void updateLatestRecommendation(final Audio newRecommendation) {
        latestRecommendation = newRecommendation;
    }


    /**
     * Adds a new song recommendation, if it has not been recommended before.
     * @throws IllegalArgumentException if it has already been recommended.
     */
    public void addSongRecommendation(final Song song) throws IllegalArgumentException {
        if (songRecommendations.contains(song)) {
            throw new IllegalArgumentException("Song " + song.getName()
                            + " has already been recommended.");
        }
        songRecommendations.add(song);
    }


    /**
     * Adds a new playlist to User's playlist recommendations list.
     */
    public void addPlaylistRecommendation(final Playlist playlist) {
        playlistRecommendations.add(playlist);
    }


    /**
     * Pushes a page to the page history stack.
     */
    public void pushPageHistory(final Page newPage) {
        pageHistory.addFirst(newPage);
    }


    /**
     * Pops a page from the page history stack.
     * @return Page instance.
     */
    public Page popPageHistory() {
        return pageHistory.pop();
    }


    /**
     * Checks whether the page history is empty.
     * @return true if it is, false otherwise.
     */
    public boolean pageHistoryIsEmpty() {
        return pageHistory.isEmpty();
    }


    /**
     * Pushes a page to the forward page history stack.
     * @param oldPage Page from where "previousPage" command was executed.
     */
    public void pushForwardPageHistory(final Page oldPage) {
        forwardPageHistory.addFirst(oldPage);
    }


    /**
     * Pops a page from the forward page history stack.
     * @return Page instance.
     */
    public Page popForwardPageHistory() {
        return forwardPageHistory.pop();
    }


    /**
     * Checks whether the forward page history is empty.
     * @return true if it is, false otherwise.
     */
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
    public void setTopEpisodes(final Map<String, Integer> topEpisodes) {
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
    public void setForwardPageHistory(final LinkedList<Page> forwardPageHistory) {
        this.forwardPageHistory = forwardPageHistory;
    }
}
