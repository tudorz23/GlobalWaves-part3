package database.audio;

import database.Player;
import fileio.input.SongInput;
import utils.enums.AudioType;
import utils.enums.PlayerState;
import utils.enums.RepeatState;
import java.util.ArrayList;

public final class Song extends Audio {
    private Integer duration;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private String artist;
    private int timePosition; // Song time at last known moment.
    private int likeCnt;

    /* Constructors */
    private Song() {
    }

    public Song(final SongInput songInput) {
        super(songInput.getName());
        this.duration = songInput.getDuration();
        this.album = songInput.getAlbum();
        this.tags = songInput.getTags();
        this.lyrics = songInput.getLyrics();
        this.genre = songInput.getGenre();
        this.releaseYear = songInput.getReleaseYear();
        this.artist = songInput.getArtist();
        this.setType(AudioType.SONG);
        this.likeCnt = 0;
    }

    @Override
    public Song getDeepCopy() {
        Song copy = new Song();
        copy.setName(this.getName());
        copy.setType(this.getType());
        copy.setDuration(this.duration);
        copy.setAlbum(this.album);
        copy.tags = new ArrayList<>();
        copy.tags.addAll(this.tags);
        copy.setLyrics(this.lyrics);
        copy.setGenre(this.genre);
        copy.setReleaseYear(this.releaseYear);
        copy.setArtist(this.artist);
        copy.timePosition = 0;
        copy.likeCnt = this.likeCnt;

        return copy;
    }

    @Override
    public void simulateTimePass(final Player player, final int currTime) {
        if (player.getPlayerState() == PlayerState.PAUSED
            || player.getPlayerState() == PlayerState.STOPPED) {
            return;
        }

        int elapsedTime = currTime - player.getPrevTimeInfo();

        if (player.getRepeatState() == RepeatState.REPEAT_INFINITE) {
            simulateRepeatInfinite(elapsedTime);
            return;
        }

        if (player.getRepeatState() == RepeatState.REPEAT_ONCE) {
            simulateRepeatOnce(player, elapsedTime);
            return;
        }

        simulateNoRepeat(player, elapsedTime);
    }

    /**
     * Simulates the time passing when Repeat Infinite is on.
     */
    private void simulateRepeatInfinite(final int elapsedTime) {
        timePosition = (timePosition + elapsedTime) % duration;
    }

    /**
     * Simulates the time passing when Repeat Once is on.
     */
    private void simulateRepeatOnce(final Player player, final int elapsedTime) {
        int quotient = (timePosition + elapsedTime) / duration;
        int remainder = (timePosition + elapsedTime) % duration;

        if (quotient == 0) {
            // No repeat necessary.
            timePosition = remainder;
            return;
        }

        if (quotient == 1) {
            // Repeat it once and set repeat state to No repeat.
            player.setRepeatState(RepeatState.NO_REPEAT);
            timePosition = remainder;
            return;
        }

        // quotient > 1. Surely, the player needs to be stopped.
        player.setPlayerState(PlayerState.STOPPED);
        player.setRepeatState(RepeatState.NO_REPEAT);
        timePosition = duration;
    }

    /**
     * Simulates the time passing when No repeat is on.
     */
    private void simulateNoRepeat(final Player player, final int elapsedTime) {
        int quotient = (timePosition + elapsedTime) / duration;
        int remainder = (timePosition + elapsedTime) % duration;

        if (quotient == 0) {
            // Song did not end.
            timePosition = remainder;
            return;
        }

        // Surely, song ended.
        player.setPlayerState(PlayerState.STOPPED);
        timePosition = duration;
    }

    /**
     * Sets the time position to 0.
     */
    public void resetTimePosition() {
        timePosition = 0;
    }

    @Override
    public int getRemainedTime() {
        return (duration - timePosition);
    }

    @Override
    public void next(final Player player) {
        player.setPlayerState(PlayerState.STOPPED);
        timePosition = duration;
    }

    @Override
    public void prev(final Player player) {
        player.setPlayerState(PlayerState.PLAYING);
        timePosition = 0;
    }

    @Override
    public String getPlayingTrackName() {
        return getName();
    }

    /**
     * Increments the number of likes.
     */
    public void incrementLikeCnt() {
        likeCnt++;
    }

    /**
     * Decrements the number of likes.
     */
    public void decrementLikeCnt() {
        likeCnt--;
    }

    /* Getters and Setters */
    public Integer getDuration() {
        return duration;
    }
    public void setDuration(final Integer duration) {
        this.duration = duration;
    }
    public String getAlbum() {
        return album;
    }
    public void setAlbum(final String album) {
        this.album = album;
    }
    public ArrayList<String> getTags() {
        return tags;
    }
    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }
    public String getLyrics() {
        return lyrics;
    }
    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(final String genre) {
        this.genre = genre;
    }
    public Integer getReleaseYear() {
        return releaseYear;
    }
    public void setReleaseYear(final Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(final String artist) {
        this.artist = artist;
    }
    public int getTimePosition() {
        return timePosition;
    }
    public void setTimePosition(final int timePosition) {
        this.timePosition = timePosition;
    }
    public int getLikeCnt() {
        return likeCnt;
    }
}
