package database.audio;

import database.Player;
import database.Searchable;
import database.users.User;
import utils.enums.AudioType;
import utils.enums.SearchableType;

import java.util.Map;

/**
 * Describes audio objects that can be searched and loaded by the user.
 * To be extended by classes Song, SongCollection and Podcast.
 */
public abstract class Audio extends Searchable {
    private String name;
    private AudioType type;
    private User listener; // The user that listens to the audio file.

    /* Constructors */
    public Audio() {
        setSearchableType(SearchableType.AUDIO);
    }

    public Audio(final String name) {
        this.name = name;
        setSearchableType(SearchableType.AUDIO);
    }

    /**
     * @return Deep copy of Audio object.
     */
    public abstract Audio getDeepCopy();


    /**
     * Simulates the passing of the time between last update moment
     * and current timestamp.
     */
    public abstract void simulateTimePass(Player player, int currTime);


    /**
     * @return Time remained until track ends.
     */
    public abstract int getRemainedTime();


    /**
     * Moves the player to the next track.
     */
    public abstract void next(Player player);


    /**
     * Moves the player to the previous track.
     */
    public abstract void prev(Player player);


    /**
     * @return Name of the currently playing track.
     */
    public abstract String getPlayingTrackName();


    /**
     * Updates the analytics for the listener for one single listen to this audio.
     */
    public abstract void updateAnalytics();


    /**
     * Introduces an advertisement into the player after the currently playing song,
     * saving the current audio instance, with an eye to reload it after the ad ends.
     */
    protected void introduceAd(final Player player, final int currTime) {
        // Compute the monetization generated between current and previous adds.
        Map<Song, Integer> listenedBetweenAds = player.getListenedBetweenAds();

        Map<Song, Double> songMonetization = getListener().getDatabase()
                .computeSongMonetization(listenedBetweenAds, player.getLastAdPrice());

        getListener().getDatabase().updateArtistMonetization(songMonetization);

        player.initListenedBetweenAds();

        Song ad = getListener().getDatabase().getAdvertisementFromDatabase();

        player.setListeningBeforeAd(this);
        player.setCurrPlaying(ad);
        player.simulateTimePass(currTime);
    }


    /* Getters and Setters */
    /**
     * Getter for name field.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name field.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Getter for type field.
     */
    public AudioType getType() {
        return type;
    }

    /**
     * Setter for type field.
     */
    public void setType(final AudioType type) {
        this.type = type;
    }

    /**
     * Getter for listened field.
     */
    public User getListener() {
        return listener;
    }

    /**
     * Setter for listener field.
     */
    public void setListener(final User listener) {
        this.listener = listener;
    }
}
