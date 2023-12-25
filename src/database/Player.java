package database;

import database.audio.Audio;
import database.audio.Song;
import utils.enums.PlayerState;
import utils.enums.RepeatState;

import java.util.HashMap;
import java.util.Map;

public final class Player {
    private Audio currPlaying;
    private PlayerState playerState;
    private RepeatState repeatState;
    private int prevTimeInfo; // Previous time when internal states where updated.
    private boolean shuffle;

    private Map<Song, Integer> listenedAsPremium; // <Song, listens>

    /* Constructor */
    public Player() {
        playerState = PlayerState.EMPTY;
        shuffle = false;
    }

    /**
     * Clears the user's player (i.e. nothing is playing anymore).
     */
    public void emptyPlayer() {
        playerState = PlayerState.EMPTY;
        shuffle = false;
    }

    /**
     * Simulates the passing of the time between two interactions with the player,
     * updating its internal states.
     * @param currTime Current timestamp of the query.
     */
    public void simulateTimePass(final int currTime) {
        if (this.playerState != PlayerState.EMPTY && this.playerState != PlayerState.STOPPED) {
            currPlaying.simulateTimePass(this, currTime);
        }

        // Update previous time from the player.
        this.setPrevTimeInfo(currTime);
    }


    public void initListenedAsPremium() {
        listenedAsPremium = new HashMap<>();
    }


    /**
     * Increments the number of listens as Premium user for the given song.
     */
    public void addListenedAsPremium(Song song) {
        int listens = listenedAsPremium.getOrDefault(song, 0);
        listenedAsPremium.put(song, listens + 1);
    }


    /* Getters and Setters */
    public Audio getCurrPlaying() {
        return currPlaying;
    }
    public void setCurrPlaying(final Audio currPlaying) {
        this.currPlaying = currPlaying;
    }
    public PlayerState getPlayerState() {
        return playerState;
    }
    public void setPlayerState(final PlayerState playerState) {
        this.playerState = playerState;
    }
    public RepeatState getRepeatState() {
        return repeatState;
    }
    public void setRepeatState(final RepeatState repeatState) {
        this.repeatState = repeatState;
    }
    public int getPrevTimeInfo() {
        return prevTimeInfo;
    }
    public void setPrevTimeInfo(final int prevTimeInfo) {
        this.prevTimeInfo = prevTimeInfo;
    }
    public boolean isShuffle() {
        return shuffle;
    }
    public void setShuffle(final boolean shuffle) {
        this.shuffle = shuffle;
    }
    public Map<Song, Integer> getListenedAsPremium() {
        return listenedAsPremium;
    }
    public void setListenedAsPremium(Map<Song, Integer> listenedAsPremium) {
        this.listenedAsPremium = listenedAsPremium;
    }
}
