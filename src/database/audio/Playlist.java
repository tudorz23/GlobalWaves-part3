package database.audio;

import database.users.Artist;
import database.users.User;
import utils.enums.AudioType;
import utils.enums.PlaylistVisibility;

public final class Playlist extends SongCollection {
    private final String owner;
    private PlaylistVisibility visibility;
    private int followersCnt;

    /* Constructor */
    public Playlist(final String name, final String owner) {
        super(name);
        this.owner = owner;
        this.visibility = PlaylistVisibility.PUBLIC;
        this.setType(AudioType.PLAYLIST);
        this.followersCnt = 0;
    }

    @Override
    public Playlist getDeepCopy() {
        Playlist copy = new Playlist(this.getName(), this.getOwner());

        for (Song song : this.getSongs()) {
            copy.addSong(song.getDeepCopy());
        }

        copy.setPlayingSongIndex(0);
        copy.followersCnt = this.followersCnt;
        copy.initializeShuffleArray();

        return copy;
    }

    /**
     * Increments the number of followers.
     */
    public void incrementFollowersCnt() {
        followersCnt++;
    }

    /**
     * Decrements the number of followers.
     */
    public void decrementFollowersCnt() {
        followersCnt--;
    }


    @Override
    public void updateAnalytics() {
        User listener = getListener();

        listener.getAnalytics().addArtist(getSongs().get(getPlayingSongIndex()).getArtist());
        listener.getAnalytics().addGenre(getSongs().get(getPlayingSongIndex()).getGenre());
        listener.getAnalytics().addSong(getPlayingTrackName());
        listener.getAnalytics().addAlbum(getSongs().get(getPlayingSongIndex()).getAlbum());

        updateArtistAnalytics();
    }


    private void updateArtistAnalytics() {
        Song currSong = getSongs().get(getPlayingSongIndex());
        Artist artist;
        try {
            artist = getListener().getDatabase().searchArtistInDatabase(currSong.getArtist());
        } catch (IllegalArgumentException exception) {
            return;
        }

        artist.getArtistAnalytics().addAlbum(currSong.getAlbum());
        artist.getArtistAnalytics().addSong(currSong.getName());
        artist.getArtistAnalytics().addFan(getListener().getUsername());
    }

    /* Getters and Setters */
    public String getOwner() {
        return owner;
    }
    public PlaylistVisibility getVisibility() {
        return visibility;
    }
    public void setVisibility(final PlaylistVisibility visibility) {
        this.visibility = visibility;
    }
    public int getFollowersCnt() {
        return followersCnt;
    }
}
